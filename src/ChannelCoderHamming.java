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
 * Allows using Hamming code (7,4)
 * @author post-factum
 */
public class ChannelCoderHamming
{
    private List<BinaryNumber> sequence;
    private List<BinaryNumber> outputSequence = new ArrayList<BinaryNumber>();
    private int headLength;

    /**
     * Creates Hamming coder for given input sequence of binary numbers
     * @param _inputSequence list of input binary numbers
     */
    public ChannelCoderHamming(List<BinaryNumber> _inputSequence)
    {
	sequence = _inputSequence;
    }

    /**
     * Runs encoding
     */
    public void doEncode()
    {
	//makes blocks of equal size
	Splitter hammingBlocker = new Splitter(sequence, 4);
	hammingBlocker.doSplitting();
	headLength = hammingBlocker.getLeadingZeroesCount();
	List<BinaryNumber> blockedSequence = hammingBlocker.getBlocks();
	//encodes made blocks
	for (BinaryNumber bn: blockedSequence)
	{
	    boolean[] currentNumberArray = bn.getBinaryArray();
	    boolean[] resultNumber = new boolean[bn.getLength() + 3];
	    System.arraycopy(currentNumberArray, 0, resultNumber, 0, currentNumberArray.length);

	    resultNumber[currentNumberArray.length] = currentNumberArray[1] ^ currentNumberArray[2] ^ currentNumberArray[3];
	    resultNumber[currentNumberArray.length + 1] = currentNumberArray[0] ^ currentNumberArray[2] ^ currentNumberArray[3];
	    resultNumber[currentNumberArray.length + 2] = currentNumberArray[0] ^ currentNumberArray[1] ^ currentNumberArray[3];
	    BinaryNumber ready = new BinaryNumber(resultNumber);
	    outputSequence.add(ready);
	}
    }

    /**
     * Returns encoded list of binary numbers
     * @return list of encoded binary numbers
     */
    public List<BinaryNumber> getSequence()
    {
	return outputSequence;
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
	    symbolsText = "символів";
	else
	{
	    String headLengthText = String.valueOf(headLength);
	    int headLengthTextLength = headLengthText.length();
	    int lastDigit = Integer.valueOf(headLengthText.substring(headLengthTextLength - 1));

	    if ((lastDigit == 0) || (lastDigit >= 5 && lastDigit <= 9))
		symbolsText = "символів";
	    else
	    if (lastDigit == 1)
	        symbolsText = "символ";
	    else
	    if (lastDigit >= 2 && lastDigit <= 4)
		symbolsText = "символи";
	}

	if (headLength > 0)
	    out += "<br/>На початок вхідної послідовності додано " + String.valueOf(headLength) + " " + symbolsText + " для вирівнювання блоків";

	out += "</html>";
	return out;
    }
}
