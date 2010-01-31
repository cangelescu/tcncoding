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

public class ChannelCoderHamming {
    private Vector<BinaryNumber> sequence;
    private Vector<BinaryNumber> output_sequence = new Vector<BinaryNumber>();

    public ChannelCoderHamming(Vector<BinaryNumber> input_sequence)
    {
	this.sequence = input_sequence;
    }

    public void doEncode()
    {
	Blocker hamming_blocker = new Blocker(this.sequence, 4);
	hamming_blocker.doBlocking();
	Vector<BinaryNumber> blocked_sequence = hamming_blocker.getBlocks();
	for (BinaryNumber bn: blocked_sequence)
	{
	    boolean[] current_number_array = bn.getAlignedBinaryArray();
	    boolean[] result_number = new boolean[bn.getAlignment() + 3];
	    for (int i = 0; i < current_number_array.length; i++)
		result_number[i] = current_number_array[i];

	    result_number[current_number_array.length] = current_number_array[0] ^ current_number_array[1] ^ current_number_array[3];
	    result_number[current_number_array.length + 1] = current_number_array[0] ^ current_number_array[2] ^ current_number_array[3];
	    result_number[current_number_array.length + 2] = current_number_array[1] ^ current_number_array[2] ^ current_number_array[3];
	    BinaryNumber ready = new BinaryNumber(result_number);
	    this.output_sequence.add(ready);
	}
    }

    public Vector<BinaryNumber> getSequence()
    {
	return this.output_sequence;
    }
}
