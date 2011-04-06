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

import java.util.List;

/**
 * Allows using cyclic code (8,5)
 * @author Oleksandr Natalenko aka post-factum
 */
public class ChannelCoderCyclic extends ChannelCoder
{
    private int headLength;

    /**
     * Creates cyclic coder for given input sequence of binary numbers
     * @param _inputSequence list of input binary numbers
     */
    public ChannelCoderCyclic(List<BinaryNumber> _inputSequence)
    {
	inputSequence = _inputSequence;
    }

    /**
     * Runs encoding
     */
    public void doEncode()
    {
	//makes blocks of equal size
	Splitter cyclicBlocker = new Splitter(inputSequence, 5);
	cyclicBlocker.doSplitting();
	headLength = cyclicBlocker.getLeadingZeroesCount();
	List<BinaryNumber> blockedSequence = cyclicBlocker.getBlocks();
	//encodes made blocks
	for (BinaryNumber bn: blockedSequence)
	{
            BinaryNumber shifted = bn.shl2(3);
            //System.out.println(shifted.getStringSequence());
            BinaryNumber syndrome = shifted.divmod2(new BinaryNumber("1011"));
            //System.out.println(shifted.getStringSequence());
	    outputSequence.add(shifted.sum2(syndrome));
            //System.out.println(shifted.getStringSequence());
	}
    }

    /**
     * Returns added head length
     * @return integer value of trailing head length
     */
    public int getHeadLength()
    {
	return headLength;
    }

    /**
     * Returns HTML-formatted report
     * @return string representation of HTML-formatted report
     */
    public String getReport()
    {
	String out = "<html>";
	boolean trigger = false;
	for (BinaryNumber bn: outputSequence)
	{
	    if (trigger)
		out += "<font color=\"blue\" size=\"5\">" + bn.getStringSequence() + " </font>";
	    else
		out += "<font color=\"green\" size=\"5\">" + bn.getStringSequence() + " </font>";
	    trigger = !trigger;
	}

	String symbolsText = "";
	if (headLength >= 10 && headLength <= 19)
	    symbolsText = java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SYMBOLS");
	else
	{
	    String headLengthText = String.valueOf(headLength);
	    int headLengthTextLength = headLengthText.length();
	    int lastDigit = Integer.valueOf(headLengthText.substring(headLengthTextLength - 1));

	    if ((lastDigit == 0) || (lastDigit >= 5 && lastDigit <= 9))
		symbolsText = java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SYMBOLS");
	    else
	    if (lastDigit == 1)
	        symbolsText = java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SYMBOL");
	    else
	    if (lastDigit >= 2 && lastDigit <= 4)
		symbolsText = java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SYMBOLS2");
	}

	if (headLength > 0)
	    out += "<br/>" + java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("ADDED") + " " + String.valueOf(headLength) + " " + symbolsText + " " + java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("TO HEAD OF SEQUENCE FOR BLOCKS ALIGNING");

	out += "</html>";
	return out;
    }
}
