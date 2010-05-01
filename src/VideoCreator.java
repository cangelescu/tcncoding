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

public class VideoCreator {
    private List<BinaryNumber> inputSequence;
    private List<List<FunctionStep>> outputSequence = new ArrayList<List<FunctionStep>>();
    private double impulseLength, step;

    public VideoCreator(List<BinaryNumber> channelSymbols, double _impulseLength)
    {
	this.inputSequence = channelSymbols;
	this.impulseLength = _impulseLength;
	this.step = Math.pow(Math.sqrt(3) * Math.E, Math.log(_impulseLength));
    }

    public void doVideoSequence()
    {
	this.outputSequence.clear();
	double cx = 0, sp = 0;
	for (BinaryNumber cbn: this.inputSequence)
	{
	    List <FunctionStep> current = new ArrayList<FunctionStep>();
	    boolean[] matrix = cbn.getAlignedBinaryArray();
	    for (boolean cm: matrix)
	    {
		sp = 0;
		while (sp <= this.impulseLength)
		{
		    if (cm)
		    {
			current.add(new FunctionStep(cx, 1));
		    } else
		    {
			current.add(new FunctionStep(cx, 0));
		    }
		    sp += this.step;
		    cx += this.step;
		}
	    }
	    this.outputSequence.add(current);
	}
    }

    public List<List<FunctionStep>> getVideoSequence()
    {
	return this.outputSequence;
    }
}
