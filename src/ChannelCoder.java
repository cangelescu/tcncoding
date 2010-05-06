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
     * @param symbols input symbols
     * @param code_type code to use
     */
    public ChannelCoder(List symbols, ChannelCoderCode code_type)
    {
	this.sourceSymbols = symbols;
	this.usingCode = code_type;
    }

    /**
     * Runs encoding
     */
    public void doEncode()
    {
	this.channelSequence.clear();
	switch (this.usingCode)
	{
	    case PARITY_BIT:
		ChannelCoderParityBit channelCoderParityBit = new ChannelCoderParityBit(this.sourceSymbols);
		channelCoderParityBit.doEncode();
		this.channelSequence = channelCoderParityBit.getSequence();
		break;
	    case INVERSED:
		ChannelCoderInversed channelCoderInversed = new ChannelCoderInversed(this.sourceSymbols);
		channelCoderInversed.doEncode();
		this.channelSequence = channelCoderInversed.getSequence();
		break;
	    case MANCHESTER:
		ChannelCoderManchester channelCoderManchester = new ChannelCoderManchester(this.sourceSymbols);
		channelCoderManchester.doEncode();
		this.channelSequence = channelCoderManchester.getSequence();
		break;
	    case HAMMING:
		ChannelCoderHamming channelCoderHamming = new ChannelCoderHamming(this.sourceSymbols);
		channelCoderHamming.doEncode();
		this.channelSequence = channelCoderHamming.getSequence();
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
	return this.channelSequence;
    }

    /**
     * Returns encoded sequence length
     * @return
     */
    public int getSequenceLength()
    {
	int out = 0;
	for (BinaryNumber cbn: channelSequence)
	    out += cbn.getAlignment();
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
	for (BinaryNumber bn: this.channelSequence)
	{
	    if (trigger)
		out += "<font color=\"blue\">" + bn.getStringSequence() + "</font>";
	    else
		out += "<font color=\"green\">" + bn.getStringSequence() + "</font>";
	    trigger = !trigger;
	}
	out += "</html>";
	return out;
    }
}
