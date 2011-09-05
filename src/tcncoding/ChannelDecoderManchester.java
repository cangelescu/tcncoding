/*

 Copyright (C) 2009-2011 Oleksandr Natalenko aka post-factum

 This program is free software; you can redistribute it and/or modify
 it under the terms of the Universal Program License as published by
 Oleksandr Natalenko aka post-factum; see file COPYING for details.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

 You should have received a copy of the Universal Program
 License along with this program; if not, write to
 pfactum@gmail.com

*/

package tcncoding;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows using Manchester code decoder
 * @author Oleksandr Natalenko aka post-factum
 */
public class ChannelDecoderManchester implements ChannelDecoder
{
    private List<BinaryNumber> inputSequence;
    private List<BinaryNumber> outputSequence = new ArrayList<BinaryNumber>();
    private List<BinaryNumber> errorVector = new ArrayList<BinaryNumber>();
    private List<BinaryNumber> checkingSequence = new ArrayList<BinaryNumber>();

    /**
     * Creates Manchester decoder for given input sequence of binary numbers
     * @param _inputSequence list of input binary numbers
     */
    public ChannelDecoderManchester(List<BinaryNumber> _inputSequence)
    {
	inputSequence = _inputSequence;
    }

    /**
     * Runs decoding
     * @return decoded sequence
     */
    public List<BinaryNumber> getSequence()
    {
	for (BinaryNumber bn: inputSequence)
	{
	    boolean[] currentNumberArray = bn.getBinaryArray();
	    boolean[] resultNumber = new boolean[bn.getLength() / 2];
	    int index = 0;
	    for (int i = 0; i < currentNumberArray.length; i += 2)
		resultNumber[index++] = currentNumberArray[i];
	    BinaryNumber ready = new BinaryNumber(resultNumber);

	    boolean[] checked = new boolean[bn.getLength()];
	    boolean[] newErrorVector = new boolean[bn.getLength()];
	    index = 0;
	    for (int i = 0; i < resultNumber.length; i++)
	    {
		checked[index] = resultNumber[i];
		checked[index + 1] = !resultNumber[i];
		boolean correct = (checked[index] != currentNumberArray[index]) || (checked[index + 1] != currentNumberArray[index + 1]);
		newErrorVector[index] = correct;
		newErrorVector[index + 1] = correct;
		index += 2;
	    }
	    BinaryNumber checkedNumber = new BinaryNumber(checked);
	    errorVector.add(new BinaryNumber(newErrorVector));

	    outputSequence.add(ready);
	    checkingSequence.add(checkedNumber);
	}
        return outputSequence;
    }

    /**
     * Returns HTML report of sequence decoding
     * @return string representation of HTML-formatted report
     */
    public String getReport()
    {
	final String fontBlue = "<font color=\"blue\" size=\"5\">";
	final String fontGreen = "<font color=\"green\" size=\"5\">";
	final String fontRed = "<font color=\"red\" size=\"5\">";

	String out = "";
	boolean trigger = false;

	out += java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("CHECKING SEQUENCE:") + "<br/>";
	trigger = false;
	for (BinaryNumber bn: checkingSequence)
	{
	    out += (trigger ? fontBlue : fontGreen) + bn.getStringSequence() + "</font>&#09;";
	    trigger = !trigger;
	}

	out += "<br/>" + java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("ERRORS VECTOR:") + "<br/>";
	trigger = false;
	for (BinaryNumber bn: errorVector)
	{
	    for (boolean cb: bn.getBinaryArray())
	    {
		if (cb)
		    out += fontRed + "1" + "</font>";
		else
		    out += (trigger ? fontBlue : fontGreen) + "0" + "</font>";
	    }
	    out += "&#09;";
	    trigger = !trigger;
	}

	out += "<br/>" + java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("DECODED SEQUENCE:") + "<br/>";
	trigger = false;
	for (int i = 0; i < outputSequence.size(); i++)
	{
	    if (errorVector.get(i).getWeight() > 0)
		out += fontRed + outputSequence.get(i).getStringSequence() + "</font>&#09;";
	    else
	    out += (trigger ? fontBlue : fontGreen) + outputSequence.get(i).getStringSequence() + "</font>&#09;";
	    trigger = !trigger;
	}

	out += "</html>";

	return out;
    }
}
