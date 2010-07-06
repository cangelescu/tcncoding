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
public class ChannelDecoderHamming
{
    private List<BinaryNumber> sequence = new ArrayList<BinaryNumber>();
    private List<BinaryNumber> outputSequence = new ArrayList<BinaryNumber>();
    private List<Integer> lengthMap;

    /**
     * Creates decoder with Hamming code for given input sequence of binary numbers
     * @param _inputSequence list of input binary numbers
     */
    public ChannelDecoderHamming(List<BinaryNumber> _inputSequence, int _headLength, List<Integer> _lengthMap)
    {
	sequence.add(_inputSequence.get(0).truncLeft(_headLength));
	for (int i = 1; i < _inputSequence.size(); i++)
	    sequence.add(_inputSequence.get(i));
	lengthMap = _lengthMap;
    }

    /**
     * Runs decoding
     */
    public void doDecode()
    {
	List<BinaryNumber> preSequence = new ArrayList<BinaryNumber>();
	for (BinaryNumber bn: sequence)
	{
	    BinaryNumber truncated = bn.truncRight(3);
	    preSequence.add(truncated);
	}

	Splitter recovery = new Splitter(preSequence, lengthMap);
	recovery.doRecovering();
	outputSequence = recovery.getBlocks();
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
