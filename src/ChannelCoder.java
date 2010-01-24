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

    private Vector source_symbols = null;
    private channelCoderCode using_code = null;
    private Vector channel_sequence = new Vector();

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
		for (Object bn: this.source_symbols)
		{
		    BinaryNumber current_number = (BinaryNumber)bn;
		    BinaryNumber shifted = current_number.shl2();
		    if (current_number.getWeight() % 2 == 0)
		    {
			this.channel_sequence.add(shifted);
		    } else
		    {
			BinaryNumber one = new BinaryNumber(1);
			this.channel_sequence.add(shifted.sum2(one));
		    }
		}
		break;
	    case inversed:
		for (Object bn: this.source_symbols)
		{
		    BinaryNumber current_number = (BinaryNumber)bn;
		    BinaryNumber shifted = current_number.shl2(current_number.alignment * 2);
		    if (current_number.getWeight() % 2 == 0)
		    {
			this.channel_sequence.add(shifted.sum2(current_number));
		    } else
		    {
			BinaryNumber inversed = current_number.not2(current_number.alignment * 2);
			this.channel_sequence.add(shifted.sum2(inversed));
		    }
		}
		break;
	    case manchester:
		for (Object bn: this.source_symbols)
		{
		    BinaryNumber current_number = (BinaryNumber)bn;
		    boolean[] current_number_array = current_number.toForceAlignedBinaryArray(current_number.alignment * 2);
		    boolean[] result_number = new boolean[current_number.alignment * 2];
		    int index = 0;
		    for (boolean current_symbol: current_number_array)
		    {
			result_number[index] = current_symbol;
			result_number[index + 1] = !current_symbol;
			index += 2;
		    }
		    BinaryNumber ready = new BinaryNumber(result_number);
		    this.channel_sequence.add(ready);
		}
		break;
	    case hamming:
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
	for (Object bn: this.channel_sequence)
	{
	    BinaryNumber number = (BinaryNumber)bn;
	    if (trigger)
		out += "<font color=\"blue\">" + number.getAlignedString() + "</font>";
	    else
		out += "<font color=\"green\">" + number.getAlignedString() + "</font>";
	    trigger = !trigger;
	}
	out += "</html>";
	return out;
    }
}
