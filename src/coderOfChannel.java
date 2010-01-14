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

    public enum channelCoderCode {parity_bit, inversed, hamming};

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
	String out = "";
	for (Object bn: this.channel_sequence)
	{
	    binaryNumber num = (binaryNumber)bn;
	    out += num.getString(this.alignment) + " ";
	}
	return out;
    }
}
