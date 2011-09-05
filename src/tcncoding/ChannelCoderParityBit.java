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
 * Allows using code with parity bit checking
 * @author Oleksandr Natalenko aka post-factum
 */
public class ChannelCoderParityBit implements ChannelCoder
{
    private List<BinaryNumber> inputSequence;
    private List<BinaryNumber> outputSequence = new ArrayList<BinaryNumber>();
    /**
     * Creates coder for given input sequence of binary numbers with parity bit checking
     * @param _inputSequence list of input binary numbers
     */
    public ChannelCoderParityBit(List<BinaryNumber> _inputSequence)
    {
	inputSequence = _inputSequence;
    }

    /**
     * Runs encoding
     * @return encoded sequence
     */
    public List<BinaryNumber> getSequence()
    {
	for (BinaryNumber bn: inputSequence)
	{
	    BinaryNumber shifted = bn.shl2();
	    if (bn.getWeight() % 2 == 0)
		outputSequence.add(shifted);
	    else
	    {
		BinaryNumber one = new BinaryNumber(1);
		outputSequence.add(shifted.sum2(one));
	    }
	}
        return outputSequence;
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
	out += "</html>";
	return out;
    }

    /**
     * 
     * @return
     */
    public int getHeadLength()
    {
        return 0;
    }
}
