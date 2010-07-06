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
public class ChannelDecoderManchester
{
    private List<BinaryNumber> sequence;
    private List<BinaryNumber> outputSequence = new ArrayList<BinaryNumber>();

    /**
     * Creates Manchester decoder for given input sequence of binary numbers
     * @param _inputSequence list of input binary numbers
     */
    public ChannelDecoderManchester(List<BinaryNumber> _inputSequence)
    {
	sequence = _inputSequence;
    }

    /**
     * Runs decoding
     */
    public void doDecode()
    {
	for (BinaryNumber bn: sequence)
	{
	    boolean[] currentNumberArray = bn.getBinaryArray();
	    boolean[] resultNumber = new boolean[bn.getLength() / 2];
	    int index = 0;
	    for (int i = 0; i < currentNumberArray.length; i += 2)
		resultNumber[index++] = currentNumberArray[i];

	    BinaryNumber ready = new BinaryNumber(resultNumber);
	    outputSequence.add(ready);
	}
    }

    /**
     * Returns decoded list of binary numbers
     * @return
     */
    public List<BinaryNumber> getSequence()
    {
	return outputSequence;
    }
}
