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
    private List<BinaryNumber> output_sequence = new ArrayList<BinaryNumber>();

    public ChannelCoderManchester(List<BinaryNumber> input_sequence)
    {
	this.sequence = input_sequence;
    }

    public void doEncode()
    {
	for (BinaryNumber bn: this.sequence)
	{
	    boolean[] current_number_array = bn.getAlignedBinaryArray();
	    boolean[] result_number = new boolean[bn.getAlignment() * 2];
	    int index = 0;
	    for (boolean current_symbol: current_number_array)
	    {
		result_number[index] = current_symbol;
		result_number[index + 1] = !current_symbol;
		index += 2;
	    }
	    BinaryNumber ready = new BinaryNumber(result_number);
	    this.output_sequence.add(ready);
	}
    }

    public List<BinaryNumber> getSequence()
    {
	return this.output_sequence;
    }
}
