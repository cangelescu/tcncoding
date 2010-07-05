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
    private double impulseLength, step, impulseLevel;

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
	step = Math.pow(Math.sqrt(3) * Math.E, Math.log(_impulseLength));
    }

    /**
     * Runs videsequence creating
     */
    public void doVideoSequence()
    {
	outputSequence.clear();
	double cx = 0, sp = 0;
	for (BinaryNumber cbn: inputSequence)
	{
	    List<List<FunctionStep>> newBlock = new ArrayList<List<FunctionStep>>();
	    boolean[] matrix = cbn.getBinaryArray();
	    for (boolean cm: matrix)
	    {
		List<FunctionStep> newSymbol = new ArrayList<FunctionStep>();
		sp = 0;
		while (sp <= impulseLength)
		{
		    if (cm)
			newSymbol.add(new FunctionStep(cx, impulseLevel));
		    else
			newSymbol.add(new FunctionStep(cx, 0));
		    sp += step;
		    cx += step;
		}
		newBlock.add(newSymbol);
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
}
