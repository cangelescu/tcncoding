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
     * Rectifies binary numbers' list into array
     * @param _sequence source list of binary numbers
     */
    public Rectifier(List<BinaryNumber> _sequence)
    {
	sequence = _sequence;
    }

    /**
     * Runs rectifying
     */
    public void doRectifying()
    {
	//gets common sequence length
	int sequenceLength = 0;
	for (BinaryNumber bn: sequence)
	    sequenceLength += bn.getLength();

	outputArray = new boolean[sequenceLength];

	int index = 0;
	for (BinaryNumber cbn: sequence)
	{
	    boolean[] currentArray = cbn.getBinaryArray();
	    for (int i = 0; i < currentArray.length; i++)
		outputArray[index++] = currentArray[i];
	}
    }

    /**
     * Returns formed array
     * @return
     */
    public boolean[] getArray()
    {
	return outputArray;
    }
}
