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

import java.util.Vector;

public class ChannelCoder {

    public enum channelCoderCode {parity_bit, inversed, manchester, hamming};

    private Vector<BinaryNumber> source_symbols = null;
    private channelCoderCode using_code = null;
    private Vector<BinaryNumber> channel_sequence = new Vector<BinaryNumber>();

    public ChannelCoder(Vector symbols, channelCoderCode code_type)
    {
	this.source_symbols = symbols;
	this.using_code = code_type;
    }

    public void doEncode()
    {
	this.channel_sequence.clear();
	switch (this.using_code)
	{
	    case parity_bit:
		ChannelCoderParityBit channel_coder_parity_bit = new ChannelCoderParityBit(this.source_symbols);
		channel_coder_parity_bit.doEncode();
		this.channel_sequence = channel_coder_parity_bit.getSequence();
		break;
	    case inversed:
		ChannelCoderInversed channel_coder_inversed = new ChannelCoderInversed(this.source_symbols);
		channel_coder_inversed.doEncode();
		this.channel_sequence = channel_coder_inversed.getSequence();
		break;
	    case manchester:
		ChannelCoderManchester channel_coder_manchester = new ChannelCoderManchester(this.source_symbols);
		channel_coder_manchester.doEncode();
		this.channel_sequence = channel_coder_manchester.getSequence();
		break;
	    case hamming:
		ChannelCoderHamming channel_coder_hamming = new ChannelCoderHamming(this.source_symbols);
		channel_coder_hamming.doEncode();
		this.channel_sequence = channel_coder_hamming.getSequence();
		break;
	    default:
		break;
	}
    }

    public Vector getSequence()
    {
	return this.channel_sequence;
    }

    public String getStringSequence()
    {
	String out = "<html>";
	boolean trigger = false;
	for (BinaryNumber bn: this.channel_sequence)
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
