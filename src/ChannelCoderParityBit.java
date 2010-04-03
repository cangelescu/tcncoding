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

public class ChannelCoderParityBit {
    private List<BinaryNumber> sequence;
    private List<BinaryNumber> output_sequence = new ArrayList<BinaryNumber>();

    public ChannelCoderParityBit(List<BinaryNumber> input_sequence)
    {
	this.sequence = input_sequence;
    }

    public void doEncode()
    {
	for (BinaryNumber bn: this.sequence)
	{
	    BinaryNumber shifted = bn.shl2();
	    if (bn.getWeight() % 2 == 0)
	    {
		this.output_sequence.add(shifted);
	    } else
	    {
		BinaryNumber one = new BinaryNumber(1);
		this.output_sequence.add(shifted.sum2(one));
	    }
	}
    }

    public List<BinaryNumber> getSequence()
    {
	return this.output_sequence;
    }
}
