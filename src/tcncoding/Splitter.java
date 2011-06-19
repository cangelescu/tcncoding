/*

 Copyright (C) 2009-2011 Oleksandr Natalenko aka post-factum

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

package tcncoding;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates blocks of binary numbers from given blocks of binary numbers
 * @author Oleksandr Natalenko aka post-factum
 */
public class Splitter
{
    private List<BinaryNumber> sequence;
    private List<BinaryNumber> outputBlocks = new ArrayList<BinaryNumber>();
    private int blockLength = 0, leadingZeroes = 0;
    private List<Integer> lengthMap;

    /**
     * Creates splitter of binary numbers' list into equal parts
     * @param _sequence source list of binary numbers
     * @param _align width of each block to split to
     */
    public Splitter(List<BinaryNumber> _sequence, int _align)
    {
	sequence = _sequence;
	blockLength = _align;
    }

    /**
     * Reorganize binary numbers list according to length map
     * @param _sequence source list of binary numbers
     * @param _lengthMap list of blocks' length
     */
    public Splitter(List<BinaryNumber> _sequence, List<Integer> _lengthMap)
    {
	sequence = _sequence;
	lengthMap = _lengthMap;
    }

    /**
     * Runs splitting
     * @return splitted sequence
     */
    public List<BinaryNumber> getEqualBlocks()
    {
	//gets common sequence length
	int sequenceLength = 0;
	for (BinaryNumber bn: sequence)
	    sequenceLength += bn.getLength();
	//adds leading zeroes
	int mod = sequenceLength % blockLength;
	if (mod > 0)
	    leadingZeroes = blockLength - mod;
	boolean[] bitFlow = new boolean[leadingZeroes + sequenceLength];
	for (int i = 0; i < leadingZeroes; i++)
	    bitFlow[i] = false;
	//forms linear bit array
	int index = leadingZeroes;
	for (BinaryNumber bn: sequence)
            for (int i = 0; i < bn.getLength(); i++)
                bitFlow[index++] = bn.getDigit(i);
	//splits formed array into separate blocks
	int k = 0;
	while (k < bitFlow.length)
	{
	    boolean[] piece = new boolean[blockLength];
	    for (int i = 0; i < blockLength; i++)
		piece[i] = bitFlow[k + i];
	    outputBlocks.add(new BinaryNumber(piece, blockLength));
	    k += blockLength;
	}
        return outputBlocks;
    }

    /**
     * Recovers sequence blocking according to length map
     * @return recovered sequence
     */
    public List<BinaryNumber> getMappedBlocks()
    {
        BitsRectifier rectifier = new BitsRectifier(sequence);
        boolean[] linearSequence = rectifier.getBits();

        int index = 0;
	for (Integer ci: lengthMap)
	{
	    boolean[] newBlock = new boolean[ci];
	    System.arraycopy(linearSequence, index, newBlock, 0, ci);
	    outputBlocks.add(new BinaryNumber(newBlock));
	    index += ci;
	}
        return outputBlocks;
    }

    /**
     * Returns leading zeroes count
     * @return integer value of leading zeroes count in recoded sequence
     */
    public int getLeadingZeroesCount()
    {
	return leadingZeroes;
    }
}
