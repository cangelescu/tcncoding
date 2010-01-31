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

public class Blocker {
    private Vector<BinaryNumber> sequence;
    private Vector<BinaryNumber> output_blocks = new Vector<BinaryNumber>();
    private int block_length;

    public Blocker(Vector<BinaryNumber> input_sequence, int align)
    {
	this.sequence = input_sequence;
	this.block_length = align;
    }

    public void doBlocking()
    {
	//gets common sequence length
	int sequence_length = 0;
	for (BinaryNumber bn: this.sequence)
	    sequence_length += bn.getAlignment();
	//adds leading zeroes
	int leading_zeroes = this.block_length - (sequence_length % this.block_length);
	boolean[] bit_flow = new boolean[leading_zeroes + sequence_length];
	for (int i = 0; i < leading_zeroes; i++)
	    bit_flow[i] = false;
	//forms linear bit array
	int index = leading_zeroes;
	for (BinaryNumber bn: this.sequence)
	{
	    boolean[] current_bits = bn.getAlignedBinaryArray();
	    for (int i = 0; i < current_bits.length; i++)
	    {
		bit_flow[index] = current_bits[i];
		index++;
	    }
	}
	//splits formed array into separate blocks
	int k = 0;
	while (k < bit_flow.length)
	{
	    boolean[] piece = new boolean[this.block_length];
	    for (int i = 0; i < this.block_length; i++)
		piece[i] = bit_flow[k + i];
	    this.output_blocks.add(new BinaryNumber(piece, this.block_length));
	    k += this.block_length;
	}
    }

    public Vector<BinaryNumber> getBlocks()
    {
	return this.output_blocks;
    }
}
