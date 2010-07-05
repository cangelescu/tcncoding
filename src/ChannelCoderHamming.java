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
public class ChannelCoderHamming
{
    private List<BinaryNumber> sequence;
    private List<BinaryNumber> outputSequence = new ArrayList<BinaryNumber>();

    /**
     * Creates Hamming coder for given input sequence of binary numbers
     * @param _inputSequence list of input binary numbers
     */
    public ChannelCoderHamming(List<BinaryNumber> _inputSequence)
    {
	sequence = _inputSequence;
    }

    /**
     * Runs encoding
     */
    public void doEncode()
    {
	//makes blocks of equal size
	Splitter hammingBlocker = new Splitter(sequence, 4);
	hammingBlocker.doSplitting();
	List<BinaryNumber> blockedSequence = hammingBlocker.getBlocks();
	//encodes made blocks
	for (BinaryNumber bn: blockedSequence)
	{
	    boolean[] currentNumberArray = bn.getBinaryArray();
	    boolean[] resultNumber = new boolean[bn.getLength() + 3];
	    System.arraycopy(currentNumberArray, 0, resultNumber, 0, currentNumberArray.length);

	    resultNumber[currentNumberArray.length] = currentNumberArray[0] ^ currentNumberArray[1] ^ currentNumberArray[3];
	    resultNumber[currentNumberArray.length + 1] = currentNumberArray[0] ^ currentNumberArray[2] ^ currentNumberArray[3];
	    resultNumber[currentNumberArray.length + 2] = currentNumberArray[1] ^ currentNumberArray[2] ^ currentNumberArray[3];
	    BinaryNumber ready = new BinaryNumber(resultNumber);
	    outputSequence.add(ready);
	}
    }

    /**
     * Returns encoded list of binary numbers
     * @return
     */
    public List<BinaryNumber> getSequence()
    {
	return outputSequence;
    }
}
