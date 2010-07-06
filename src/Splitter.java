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
     * Rectifies binary number sequence into one linear number
     * @param _sequence source list of binary numbers
     */
    public Splitter(List<BinaryNumber> _sequence)
    {
	sequence = _sequence;
	blockLength = 0;
	for (BinaryNumber cb: _sequence)
	    blockLength += cb.getLength();
    }

    /**
     * Reorganize binary numbers list according to length map
     * @param _sequence source list of binary numbers
     */
    public Splitter(List<BinaryNumber> _sequence, List<Integer> _lengthMap)
    {
	sequence = _sequence;
	lengthMap = _lengthMap;
    }

    /**
     * Creates splitter of binary number into equal parts
     * @param _sequence source list of binary numbers
     * @param _align width of each block to split to
     */
    public Splitter(BinaryNumber _sequence, int _align)
    {
	List<BinaryNumber> tmpList = new ArrayList<BinaryNumber>();
	tmpList.add(_sequence);
	sequence = tmpList;
	blockLength = _align;
    }

    /**
     * Rectifies binary number into one linear number
     * @param _sequence source list of binary numbers
     */
    public Splitter(BinaryNumber _sequence)
    {
	List<BinaryNumber> tmpList = new ArrayList<BinaryNumber>();
	tmpList.add(_sequence);
	sequence = tmpList;
	blockLength = _sequence.getLength();
    }

    /**
     * Runs splitting
     */
    public void doSplitting()
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
	{
	    boolean[] currentBits = bn.getBinaryArray();
	    for (int i = 0; i < currentBits.length; i++)
		bitFlow[index++] = currentBits[i];
	}
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
    }

    /**
     * Recovers sequence blocking according to length map
     */
    public void doRecovering()
    {
	Splitter linearSplitter = new Splitter(sequence);
	linearSplitter.doSplitting();
	boolean[] linearSequence = linearSplitter.getBlocks().get(0).getBinaryArray();

	int index = 0;
	for (Integer ci: lengthMap)
	{
	    boolean[] newBlock = new boolean[ci];
	    System.arraycopy(linearSequence, index, newBlock, 0, ci);
	    outputBlocks.add(new BinaryNumber(newBlock));
	    index += ci;
	}
    }

    /**
     * Returns list of splitted blocks with fixed width
     * @return
     */
    public List<BinaryNumber> getBlocks()
    {
	return outputBlocks;
    }

    public int getLeadingZeroesCount()
    {
	return leadingZeroes;
    }
}
