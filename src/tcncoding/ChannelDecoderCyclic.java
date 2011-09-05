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
 * Allows using cyclic code (8,5) decoder
 * @author Oleksandr Natalenko aka post-factum
 */
public class ChannelDecoderCyclic implements ChannelDecoder
{
    private List<BinaryNumber> inputSequence;
    private List<BinaryNumber> outputSequence = new ArrayList<BinaryNumber>();
    private List<BinaryNumber> errorVector = new ArrayList<BinaryNumber>();
    private List<BinaryNumber> syndromeSequence = new ArrayList<BinaryNumber>();
    private List<Integer> lengthMap;
    private int headLength;

    /**
     * Creates decoder with cyclic code for given input sequence of binary numbers
     * @param _inputSequence list of input binary numbers
     * @param _headLength length of trailing head in the sequence
     * @param _lengthMap map of blocks' length
     */
    public ChannelDecoderCyclic(List<BinaryNumber> _inputSequence, int _headLength, List<Integer> _lengthMap)
    {
	inputSequence = _inputSequence;
	headLength = _headLength;
	lengthMap = _lengthMap;
    }

    /**
     * Runs decoding
     * @return decoded sequence
     */
    public List<BinaryNumber> getSequence()
    {
	List<BinaryNumber> pre1Sequence = new ArrayList<BinaryNumber>();
	List<BinaryNumber> pre2Sequence = new ArrayList<BinaryNumber>();

	for (BinaryNumber bn: inputSequence)
	{
	    //calculates cyclic code syndrome
            BinaryNumber syndrome = bn.divmod2(new BinaryNumber("1011")).leaveRight(3);
            String syndromeString = syndrome.getStringSequence();
            int errorBit;
            if (syndromeString.equals("001"))
                errorBit = 1;
            else
            if (syndromeString.equals("101"))
                errorBit = 2;
            else
            if (syndromeString.equals("111"))
                errorBit = 3;
            else
            if (syndromeString.equals("110"))
                errorBit = 4;
            else
            if (syndromeString.equals("011"))
                errorBit = 5;
            else
            if (syndromeString.equals("100"))
                errorBit = 6;
            else
            if (syndromeString.equals("010"))
                errorBit = 7;
            else
                errorBit = 0;
	    syndromeSequence.add(syndrome);

	    //creates error vector based on calculated syndrome
	    boolean[] errorVectorArray = new boolean[bn.getLength()];
	    for (int i = 0; i < errorVectorArray.length; i++)
		errorVectorArray[i] = errorBit - 1 == i;
	    errorVector.add(new BinaryNumber(errorVectorArray));

	    //recovers code sequence using calculated integer value of syndrome
	    boolean[] preDecodedBlock = bn.getBinaryArray();
	    if (errorBit > 0)
		preDecodedBlock[errorBit - 1] = !bn.getDigit(errorBit - 1);
	    boolean[] decodedBlock = new boolean[preDecodedBlock.length - 3];
	    System.arraycopy(preDecodedBlock, 0, decodedBlock, 0, decodedBlock.length);
	    pre1Sequence.add(new BinaryNumber(decodedBlock));
	}

	//truncates alignment head of the whole code sequence
	pre2Sequence.add(pre1Sequence.get(0).truncLeft(headLength));
	for (int i = 1; i < pre1Sequence.size(); i++)
	    pre2Sequence.add(pre1Sequence.get(i));

	//recovers original blocks of sequence
	Splitter recovery = new Splitter(pre2Sequence, lengthMap);
	outputSequence = recovery.getMappedBlocks();
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

	out += java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SYNDROMES SEQUENCE:") + "<br/>";
	trigger = false;
	for (BinaryNumber bn: syndromeSequence)
	{
	    out += (trigger ? fontBlue : fontGreen) + bn.getStringSequence() + "</font> ";
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
	    out += " ";
	    trigger = !trigger;
	}

	out += "<br/>" + java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("DECODED SEQUENCE:") + "<br/>";
	trigger = false;
	for (int i = 0; i < outputSequence.size(); i++)
	{
	    out += (trigger ? fontBlue : fontGreen) + outputSequence.get(i).getStringSequence() + "</font> ";
	    trigger = !trigger;
	}

	out += "</html>";

	return out;
    }
}
