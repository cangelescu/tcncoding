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

import java.util.List;

/**
 *
 * @author post-factum
 */
public class Rectifier
{
    private List<BinaryNumber> sequence;
    private boolean[] outputArray;

    /**
     * Creates rectifier of binary numbers' list into array
     * @param inputSequence source list of binary numbers
     */
    public Rectifier(List<BinaryNumber> inputSequence)
    {
	this.sequence = inputSequence;
    }

    /**
     * Runs rectifying
     */
    public void doRectifying()
    {
	//gets common sequence length
	int sequenceLength = 0;
	for (BinaryNumber bn: this.sequence)
	    sequenceLength += bn.getAlignment();

	this.outputArray = new boolean[sequenceLength];

	int index = 0;
	for (BinaryNumber cbn: this.sequence)
	{
	    boolean[] currentArray = cbn.getAlignedBinaryArray();
	    for (int i = 0; i < currentArray.length; i++)
		this.outputArray[index++] = currentArray[i];
	}
    }

    /**
     * Returns formed array
     * @return
     */
    public boolean[] getArray()
    {
	return this.outputArray;
    }
}
