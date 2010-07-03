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
public class ChannelCoder
{

    /**
     * List of possible channel coder codes
     */
    public enum ChannelCoderCode
    {

	/**
	 * Code with parity bit checking
	 */
	PARITY_BIT,
	/**
	 * Inversed code
	 */
	INVERSED,
	/**
	 * Manchester code
	 */
	MANCHESTER,
	/**
	 * Hamming code
	 */
	HAMMING;
    };

    private List<BinaryNumber> sourceSymbols = null;
    private ChannelCoderCode usingCode = null;
    private List<BinaryNumber> channelSequence = new ArrayList<BinaryNumber>();

    /**
     * Creates channel coder with given code and symbols on its input
     * @param _symbols input symbols
     * @param _codeType code to use
     */
    public ChannelCoder(List _symbols, ChannelCoderCode _codeType)
    {
	sourceSymbols = _symbols;
	usingCode = _codeType;
    }

    /**
     * Runs encoding
     */
    public void doEncode()
    {
	channelSequence.clear();
	switch (usingCode)
	{
	    case PARITY_BIT:
		ChannelCoderParityBit channelCoderParityBit = new ChannelCoderParityBit(sourceSymbols);
		channelCoderParityBit.doEncode();
		channelSequence = channelCoderParityBit.getSequence();
		break;
	    case INVERSED:
		ChannelCoderInversed channelCoderInversed = new ChannelCoderInversed(sourceSymbols);
		channelCoderInversed.doEncode();
		channelSequence = channelCoderInversed.getSequence();
		break;
	    case MANCHESTER:
		ChannelCoderManchester channelCoderManchester = new ChannelCoderManchester(sourceSymbols);
		channelCoderManchester.doEncode();
		channelSequence = channelCoderManchester.getSequence();
		break;
	    case HAMMING:
		ChannelCoderHamming channelCoderHamming = new ChannelCoderHamming(sourceSymbols);
		channelCoderHamming.doEncode();
		channelSequence = channelCoderHamming.getSequence();
		break;
	    default:
		break;
	}
    }

    /**
     * Returns encoded sequence
     * @return
     */
    public List getSequence()
    {
	return channelSequence;
    }

    /**
     * Returns encoded sequence length
     * @return
     */
    public int getSequenceLength()
    {
	int out = 0;
	for (BinaryNumber cbn: channelSequence)
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
	for (BinaryNumber bn: channelSequence)
	{
	    if (trigger)
		out += "<font color=\"blue\" size=\"5\">" + bn.getStringSequence() + "</font>";
	    else
		out += "<font color=\"green\" size=\"5\">" + bn.getStringSequence() + "</font>";
	    trigger = !trigger;
	}
	out += "</html>";
	return out;
    }
}
