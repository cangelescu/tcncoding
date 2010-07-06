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
public class ChannelDecoder
{

    private List<BinaryNumber> inputSequence = null;
    private ChannelCoder.ChannelCoderCode usingCode = null;
    private List<BinaryNumber> outputSequence = new ArrayList<BinaryNumber>();
    private int headLength;
    private List<Integer> lengthMap;

    /**
     * Creates channel decoder with given code and symbols on its input
     * @param _symbols input symbols
     * @param _codeType code to use
     */
    public ChannelDecoder(List<BinaryNumber> _symbols, ChannelCoder.ChannelCoderCode _codeType, int _headLength, List<Integer> _lengthMap)
    {
	inputSequence = _symbols;
	usingCode = _codeType;
	headLength = _headLength;
	lengthMap = _lengthMap;
    }

    /**
     * Runs decoding
     */
    public void doDecode()
    {
	outputSequence.clear();
	switch (usingCode)
	{
	    case PARITY_BIT:
		ChannelDecoderParityBit channelDecoderParityBit = new ChannelDecoderParityBit(inputSequence);
		channelDecoderParityBit.doDecode();
		outputSequence = channelDecoderParityBit.getSequence();
		break;
	    case INVERSED:
		ChannelDecoderInversed channelDecoderInversed = new ChannelDecoderInversed(inputSequence);
		channelDecoderInversed.doDecode();
		outputSequence = channelDecoderInversed.getSequence();
		break;
	    case MANCHESTER:
		ChannelDecoderManchester channelDecoderManchester = new ChannelDecoderManchester(inputSequence);
		channelDecoderManchester.doDecode();
		outputSequence = channelDecoderManchester.getSequence();
		break;
	    case HAMMING:
		ChannelDecoderHamming channelDecoderHamming = new ChannelDecoderHamming(inputSequence, headLength, lengthMap);
		channelDecoderHamming.doDecode();
		outputSequence = channelDecoderHamming.getSequence();
		break;
	    default:
		break;
	}
    }

    /**
     * Returns decoded sequence
     * @return
     */
    public List<BinaryNumber> getSequence()
    {
	return outputSequence;
    }

    /**
     * Returns decoded sequence length
     * @return
     */
    public int getSequenceLength()
    {
	int out = 0;
	for (BinaryNumber cbn: outputSequence)
	    out += cbn.getLength();
	return out;
    }

    /**
     * Returns HTML-formatted encoded string sequence
     * @return
     */
    public String getStringSequence()
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
}
