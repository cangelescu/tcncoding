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
public class VideoCreator
{
    private List<BinaryNumber> inputSequence;
    private List<List<List<FunctionStep>>> outputSequence = new ArrayList<List<List<FunctionStep>>>();
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
	    List<List<FunctionStep>> newBlock = new ArrayList<List<FunctionStep>>();
	    for (boolean cm: matrix)
	    {
		List<FunctionStep> newStep = new ArrayList<FunctionStep>();
		if (cm)
		    newStep.add(new FunctionStep(cx, impulseLevel));
		else
		    newStep.add(new FunctionStep(cx, 0));
		newBlock.add(newStep);
		cx += impulseLength;
	    }
	    outputSequence.add(newBlock);
	}
    }

    /**
     * Returns resulted videosequence
     * @return
     */
    public List<List<List<FunctionStep>>> getVideoSequence()
    {
	return outputSequence;
    }

    /**
     * Returns step size
     * @return
     */
    public double getStepSize()
    {
	return impulseLength;
    }
}
