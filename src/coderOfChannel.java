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

public class coderOfChannel {

    public enum channelCoderCode {parity_bit, inversed, manchester, hamming};

    private Vector source_symbols = null;
    public int alignment = 0;
    private channelCoderCode using_code = null;
    private Vector channel_sequence = new Vector();

    public coderOfChannel(Vector symbols, channelCoderCode code_type, int alignment)
    {
	this.source_symbols = symbols;
	this.using_code = code_type;
	switch (code_type)
	{
	    case parity_bit:
		this.alignment = alignment + 1;
		break;
	    case inversed:
		this.alignment = alignment * 2;
		break;
	    case manchester:
		this.alignment = alignment * 2;
		break;
	    case hamming:
		this.alignment = alignment + 3;
		break;
	    default:
		break;
	}


    }

    public void doEncode()
    {
	this.channel_sequence.clear();
	switch (this.using_code)
	{
	    case parity_bit:
		for (Object bn: this.source_symbols)
		{
		    binaryNumber current_number = (binaryNumber)bn;
		    binaryNumber shifted = current_number.shl2();
		    if (current_number.getWeight() % 2 == 0)
		    {
			this.channel_sequence.add(shifted);
		    } else
		    {
			binaryNumber one = new binaryNumber(1);
			this.channel_sequence.add(shifted.sum2(one));
		    }
		}
		break;
	    case inversed:
		for (Object bn: this.source_symbols)
		{
		    binaryNumber current_number = (binaryNumber)bn;
		    binaryNumber shifted = current_number.shl2(this.alignment / 2);
		    if (current_number.getWeight() % 2 == 0)
		    {
			this.channel_sequence.add(shifted.sum2(current_number));
		    } else
		    {
			binaryNumber inversed = current_number.not2(this.alignment / 2);
			this.channel_sequence.add(shifted.sum2(inversed));
		    }
		}
		break;
	    case manchester:
		for (Object bn: this.source_symbols)
		{
		    binaryNumber current_number = (binaryNumber)bn;
		    boolean[] current_number_array = current_number.toBinaryArray(this.alignment / 2);
		    boolean[] result_number = new boolean[this.alignment];
		    int index = 0;
		    for (boolean current_symbol: current_number_array)
		    {
			result_number[index] = current_symbol;
			result_number[index + 1] = !current_symbol;
			index += 2;
		    }
		    binaryNumber ready = new binaryNumber(result_number);
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
	    binaryNumber number = (binaryNumber)bn;
	    if (trigger)
		out += "<font color=\"blue\">" + number.getString(this.alignment) + "</font>";
	    else
		out += "<font color=\"green\">" + number.getString(this.alignment) + "</font>";
	    trigger = !trigger;
	}
	out += "</html>";
	return out;
    }
}
