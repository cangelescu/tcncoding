/*

 Copyright (C) 2009-2010 Oleksandr Natalenko aka post-factum

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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author post-factum
 */
public class ChannelDecoderInversed
{
    private List<BinaryNumber> sequence;
    private List<BinaryNumber> outputSequence = new ArrayList<BinaryNumber>();
    private List<BinaryNumber> errorVector = new ArrayList<BinaryNumber>();
    private List<BinaryNumber> checkingSequence = new ArrayList<BinaryNumber>();

    /**
     * Creates decoder with inversed code for given input sequence of binary numbers
     * @param _inputSequence list of input binary numbers
     */
    public ChannelDecoderInversed(List<BinaryNumber> _inputSequence)
    {
	sequence = _inputSequence;
    }

    /**
     * Runs decoding
     */
    public void doDecode()
    {
	for (BinaryNumber bn: sequence)
	{
	    BinaryNumber truncated = bn.truncRight(bn.getLength() / 2);
	    BinaryNumber checked = bn.truncRight(bn.getLength() / 2).shl2(bn.getLength() / 2).sum2(truncated.getWeight() % 2 == 0 ? truncated : truncated.not2());
	    boolean correct = checked.toInt() == bn.toInt();
	    boolean[] newErrorVector = new boolean[bn.getLength()];
	    for (int i = 0; i < newErrorVector.length; i++)
		newErrorVector[i] = !correct;
	    errorVector.add(new BinaryNumber(newErrorVector));
	    outputSequence.add(truncated);
	    checkingSequence.add(checked);
	}
    }

    /**
     * Returns decoded list of binary numbers
     * @return
     */
    public List<BinaryNumber> getSequence()
    {
	return outputSequence;
    }

    /**
     * Returns HTML report of sequence decoding
     * @return
     */
    public String getReport()
    {
	final String fontBlue = "<font color=\"blue\" size=\"5\">";
	final String fontGreen = "<font color=\"green\" size=\"5\">";
	final String fontRed = "<font color=\"red\" size=\"5\">";

	String out = "<html>";

	out += "Прийнята послідовність:<br/>";
	boolean trigger = false;
	for (BinaryNumber bn: sequence)
	{
	    if (trigger)
		out += fontBlue + bn.getStringSequence() + " </font>";
	    else
		out += fontGreen + bn.getStringSequence() + " </font>";
	    trigger = !trigger;
	}

	out += "<br/>Перевірочна послідовність:<br/>";
	trigger = false;
	for (BinaryNumber bn: checkingSequence)
	{
	    out += (trigger ? fontBlue : fontGreen) + bn.getStringSequence() + " </font>";
	    trigger = !trigger;
	}

	out += "<br/>Вектор помилок:<br/>";
	trigger = false;
	for (BinaryNumber bn: errorVector)
	{
	    if (bn.getWeight() > 0)
		out += fontRed + bn.getStringSequence() + " </font>";
	    else
		out += (trigger ? fontBlue : fontGreen) + bn.getStringSequence() + " </font>";
	    trigger = !trigger;
	}

	out += "<br/>Декодована послідовність:<br/>";
	trigger = false;
	for (int i = 0; i < outputSequence.size(); i++)
	{
	    if (errorVector.get(i).getWeight() > 0)
		out += fontRed + outputSequence.get(i).getStringSequence() + " </font>";
	    else
	    out += (trigger ? fontBlue : fontGreen) + outputSequence.get(i).getStringSequence() + " </font>";
	    trigger = !trigger;
	}

	out += "</html>";

	return out;
    }
}
