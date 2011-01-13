/*

 Copyright (C) 2009-2011 Oleksandr Natalenko aka post-factum

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

package tcncoding;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates videosequence from given binary sequence
 * @author Oleksandr Natalenko aka post-factum
 */
public class VideoCreator
{
    private List<BinaryNumber> inputSequence;
    private List<List<DigitalSignal>> outputSequence = new ArrayList<List<DigitalSignal>>();
    private double impulseLength, impulseLevel;

    /**
     * Creates videosequence
     * @param _channelSymbols list of source symbols
     * @param _impulseLength length of each impulse, s
     * @param _impulseLevel level of each impulse, V
     */
    public VideoCreator(List<BinaryNumber> _channelSymbols, double _impulseLength, double _impulseLevel)
    {
	inputSequence = _channelSymbols;
	impulseLength = _impulseLength;
	impulseLevel = _impulseLevel;
    }

    /**
     * Runs videosequence creating
     */
    public void doVideoSequence()
    {
	outputSequence.clear();
	double cx = 0;
	for (BinaryNumber cbn: inputSequence)
	{
	    boolean[] matrix = cbn.getBinaryArray();
	    List<DigitalSignal> newBlock = new ArrayList<DigitalSignal>();
	    for (boolean cm: matrix)
	    {
		List<Sample> newBit = new ArrayList<Sample>();
		if (cm)
		    newBit.add(new Sample(cx, impulseLevel));
		else
		    newBit.add(new Sample(cx, 0));
		DigitalSignal newDigitalSignal = new DigitalSignal(newBit);
		newDigitalSignal.setEnd(cx + impulseLength);
		newBlock.add(newDigitalSignal);
		cx += impulseLength;
	    }
	    outputSequence.add(newBlock);
	}
    }

    /**
     * Returns resulted videosequence
     * @return digital functions
     */
    public List<List<DigitalSignal>> getVideoSequence()
    {
	return outputSequence;
    }
}
