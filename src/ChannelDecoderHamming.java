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
 * Allows using Hamming code (7,4) decoder
 * @author post-factum
 */
public class ChannelDecoderHamming
{
    private List<BinaryNumber> inputSequence = new ArrayList<BinaryNumber>();
    private List<BinaryNumber> syndromeSequence = new ArrayList<BinaryNumber>();
    private List<BinaryNumber> outputSequence = new ArrayList<BinaryNumber>();
    private List<BinaryNumber> errorSequence = new ArrayList<BinaryNumber>();
    private List<Integer> lengthMap;
    private int headLength;

    /**
     * Creates decoder with Hamming code for given input sequence of binary numbers
     * @param _inputSequence list of input binary numbers
     */
    public ChannelDecoderHamming(List<BinaryNumber> _inputSequence, int _headLength, List<Integer> _lengthMap)
    {
	inputSequence = _inputSequence;
	headLength = _headLength;
	lengthMap = _lengthMap;
    }

    /**
     * Runs decoding
     */
    public void doDecode()
    {
	List<BinaryNumber> pre1Sequence = new ArrayList<BinaryNumber>();
	List<BinaryNumber> pre2Sequence = new ArrayList<BinaryNumber>();

	for (BinaryNumber bn: inputSequence)
	{
	    //calculates Hamming code syndrome
	    boolean syndrome[] = new boolean[3];
	    syndrome[0] = bn.getDigit(3) ^ bn.getDigit(4) ^ bn.getDigit(5) ^ bn.getDigit(6);
	    syndrome[1] = bn.getDigit(1) ^ bn.getDigit(2) ^ bn.getDigit(5) ^ bn.getDigit(6);
	    syndrome[2] = bn.getDigit(0) ^ bn.getDigit(2) ^ bn.getDigit(4) ^ bn.getDigit(6);
	    BinaryNumber syndromeNumber = new BinaryNumber(syndrome);
	    int errorBit = (int)syndromeNumber.toInt();
	    syndromeSequence.add(syndromeNumber);

	    //creates error vector based on calculated syndrome
	    boolean[] errorVector = new boolean[bn.getLength()];
	    for (int i = 0; i < errorVector.length; i++)
		errorVector[i] = errorBit - 1 == i;
	    errorSequence.add(new BinaryNumber(errorVector));

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
	recovery.doRecovering();
	outputSequence = recovery.getBlocks();
    }

    /**
     * Returns decoded list of binary numbers
     * @return list of decoded binary numbers
     */
    public List<BinaryNumber> getSequence()
    {
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

	String out = "<html>";

	out += "Прийнята послідовність:<br/>";
	boolean trigger = false;
	for (int i = 0; i < inputSequence.size(); i++)
	{
	    if (trigger)
		out += fontBlue;
	    else
		out += fontGreen;

	    for (int j = 0; j < inputSequence.get(i).getLength(); j++)
	    {
		if (errorSequence.get(i).getDigit(j))
		    out += fontRed + (inputSequence.get(i).getDigit(j) ? "1" : "0") + "</font>";
		else
		    out += inputSequence.get(i).getDigit(j) ? "1" : "0";
	    }

	    out += "</font> ";

	    trigger = !trigger;
	}

	out += "<br/>Послідовність синдромів:<br/>";
	trigger = false;
	for (BinaryNumber bn: syndromeSequence)
	{
	    out += (trigger ? fontBlue : fontGreen) + bn.getStringSequence() + "</font> ";
	    trigger = !trigger;
	}

	out += "<br/>Вектор помилок:<br/>";
	trigger = false;
	for (BinaryNumber bn: errorSequence)
	{
	    for (boolean cb: bn.getBinaryArray())
	    {
		if (cb)
		    out += fontRed + "1</font>";
		else
		    out += (trigger ? fontBlue : fontGreen) + "0</font>";
	    }
	    out += " ";
	    trigger = !trigger;
	}

	out += "<br/>Декодована послідовність:<br/>";
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
