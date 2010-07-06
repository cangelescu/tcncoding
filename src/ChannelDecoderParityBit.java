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
public class ChannelDecoderParityBit
{
    private List<BinaryNumber> sequence;
    private List<BinaryNumber> outputSequence = new ArrayList<BinaryNumber>();

    /**
     * Creates decoder for given input sequence of binary numbers with parity bit checking
     * @param _inputSequence list of input binary numbers
     */
    public ChannelDecoderParityBit(List<BinaryNumber> _inputSequence)
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
	    BinaryNumber truncated = bn.truncRight();
	    outputSequence.add(truncated);
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
