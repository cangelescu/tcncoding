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
 * Allows using Manchester code
 * @author Oleksandr Natalenko aka post-factum
 */
public class ChannelCoderManchester implements ChannelCoder
{
    private List<BinaryNumber> inputSequence;
    private List<BinaryNumber> outputSequence = new ArrayList<BinaryNumber>();
    /**
     * Creates Manchester coder for given input sequence of binary numbers
     * @param _inputSequence list of input binary numbers
     */
    public ChannelCoderManchester(List<BinaryNumber> _inputSequence)
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
	    boolean[] currentNumberArray = bn.getBinaryArray();
	    boolean[] resultNumber = new boolean[bn.getLength() * 2];
	    int index = 0;
	    for (boolean currentSymbol: currentNumberArray)
	    {
		resultNumber[index++] = currentSymbol;
		resultNumber[index++] = !currentSymbol;
	    }
	    BinaryNumber ready = new BinaryNumber(resultNumber);
	    outputSequence.add(ready);
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
