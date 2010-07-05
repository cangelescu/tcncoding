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
public class ChannelCoderInversed
{
    private List<BinaryNumber> sequence;
    private List<BinaryNumber> outputSequence = new ArrayList<BinaryNumber>();

    /**
     * Creates coder with inversed code for given input sequence of binary numbers
     * @param _inputSequence list of input binary numbers
     */
    public ChannelCoderInversed(List<BinaryNumber> _inputSequence)
    {
	sequence = _inputSequence;
    }

    /**
     * Runs encoding
     */
    public void doEncode()
    {
	for (BinaryNumber bn: sequence)
	{
	    BinaryNumber shifted = bn.shl2(bn.getLength());
	    if (bn.getWeight() % 2 == 0)
		outputSequence.add(shifted.sum2(bn));
	    else
	    {
		BinaryNumber inversed = bn.not2();
		outputSequence.add(shifted.sum2(inversed));
	    }
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
