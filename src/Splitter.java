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
public class Splitter
{
    private List<BinaryNumber> sequence;
    private List<BinaryNumber> outputBlocks = new ArrayList<BinaryNumber>();
    private int blockLength;

    /**
     * Creates splitter of binary numbers' list into equal parts
     * @param inputSequence source list of binary numbers
     * @param align width of each block to split to
     */
    public Splitter(List<BinaryNumber> inputSequence, int align)
    {
	this.sequence = inputSequence;
	this.blockLength = align;
    }

    /**
     * Runs splitting
     */
    public void doSplitting()
    {
	//gets common sequence length
	int sequenceLength = 0;
	for (BinaryNumber bn: this.sequence)
	    sequenceLength += bn.getAlignment();
	//adds leading zeroes
	int leadingZeroes = this.blockLength - (sequenceLength % this.blockLength);
	boolean[] bit_flow = new boolean[leadingZeroes + sequenceLength];
	for (int i = 0; i < leadingZeroes; i++)
	    bit_flow[i] = false;
	//forms linear bit array
	int index = leadingZeroes;
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
	    boolean[] piece = new boolean[this.blockLength];
	    for (int i = 0; i < this.blockLength; i++)
		piece[i] = bit_flow[k + i];
	    this.outputBlocks.add(new BinaryNumber(piece, this.blockLength));
	    k += this.blockLength;
	}
    }

    /**
     * Returns list of splitted blocks with fixed width
     * @return
     */
    public List<BinaryNumber> getBlocks()
    {
	return this.outputBlocks;
    }
}
