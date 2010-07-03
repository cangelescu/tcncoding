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

/**
 *
 * @author post-factum
 */
public class ModulatorRPSKRecoder
{
    private boolean[] sequence, outputArray;

    /**
     * Rectifies binary numbers' list into array
     * @param _sequence source list of binary numbers
     */
    public ModulatorRPSKRecoder(boolean[] _sequence)
    {
	sequence = _sequence;
    }

    /**
     * Runs encoding
     */
    public void doEncoding()
    {
	//shifts array
	outputArray = new boolean[sequence.length + 1];
	outputArray[0] = true;
	System.arraycopy(sequence, 0, outputArray, 1, sequence.length);
	//recodes array
	for (int i = 1; i < outputArray.length; i++)
	    outputArray[i] = sequence[i - 1] ^ outputArray[i - 1];
    }

    /**
     * Runs decoding
     */
    public void doDecoding()
    {
	outputArray = new boolean[sequence.length - 1];
	for (int i = 1; i < sequence.length; i++)
	    outputArray[i - 1] = sequence[i] ^ sequence[i - 1];
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
