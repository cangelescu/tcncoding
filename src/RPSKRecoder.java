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
public class RPSKRecoder
{
    private boolean[] sequence, outputArray;

    /**
     * Rectifies binary numbers' list into array
     * @param inputSequence source list of binary numbers
     */
    public RPSKRecoder(boolean[] inputSequence)
    {
	this.sequence = inputSequence;
    }

    /**
     * Runs rectifying
     */
    public void doRecoding()
    {
	//shifts array
	boolean[] temporaryArray = new boolean[this.sequence.length + 1];
	outputArray = new boolean[this.sequence.length + 1];
	temporaryArray[0] = false;
	outputArray[0] = false;
	System.arraycopy(this.sequence, 0, temporaryArray, 1, this.sequence.length);
	//recodes array
	for (int i = 1; i < temporaryArray.length; i++)
	    outputArray[i] = temporaryArray[i] && temporaryArray[i - 1];
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
