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

public class ChannelCoderManchester {
    private List<BinaryNumber> sequence;
    private List<BinaryNumber> outputSequence = new ArrayList<BinaryNumber>();

    public ChannelCoderManchester(List<BinaryNumber> inputSequence)
    {
	this.sequence = inputSequence;
    }

    public void doEncode()
    {
	for (BinaryNumber bn: this.sequence)
	{
	    boolean[] currentNumberArray = bn.getAlignedBinaryArray();
	    boolean[] resultNumber = new boolean[bn.getAlignment() * 2];
	    int index = 0;
	    for (boolean currentSymbol: currentNumberArray)
	    {
		resultNumber[index] = currentSymbol;
		resultNumber[index + 1] = !currentSymbol;
		index += 2;
	    }
	    BinaryNumber ready = new BinaryNumber(resultNumber);
	    this.outputSequence.add(ready);
	}
    }

    public List<BinaryNumber> getSequence()
    {
	return this.outputSequence;
    }
}