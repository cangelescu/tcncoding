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
public class Resolver
{
    private List<List<FunctionStep>> summatorSignal;
    Modulator.ModulationType modulationType;
    private double bearerAmplitude, bearerFrequency, impulseLength;
    private List<Boolean> outputSequence;

    /**
     * Creates resolver
     * @param _summatorSignal signal from summator to operate on
     * @param _modulationType type of using modulation
     */
    public Resolver(List<List<FunctionStep>> _summatorSignal, Modulator.ModulationType _modulationType, double _bearerAmplitude, double _bearerFrequency, double _impulseLength)
    {
	summatorSignal = _summatorSignal;
	modulationType = _modulationType;
	bearerAmplitude = _bearerAmplitude;
	bearerFrequency = _bearerFrequency;
	impulseLength = _impulseLength;
    }

    /**
     * Runs summing
     */
    public void doResolving()
    {
	outputSequence = new ArrayList<Boolean>();
	for (List<FunctionStep> currentSymbol: summatorSignal)
	{
	    double threshold;
	    double value = currentSymbol.get(currentSymbol.size() - 1).getY();
	    switch (modulationType)
	    {
		case ASK:
		    double w = 2 * Math.PI * bearerFrequency;
		    threshold = -0.25 * (Math.pow(bearerAmplitude, 2) * (Math.cos(w * impulseLength) * Math.sin(w * impulseLength) - w * impulseLength)) / w;
		    break;
		case FSK:
		    threshold = 0;
		    break;
		case PSK:
		    threshold = 0;
		    break;
		case RPSK:
		    threshold = 0;
		    break;
		default:
		    threshold = 0;
		    break;
	    }
	    System.out.println(threshold);
	    outputSequence.add(value > threshold);
	}
    }

    /**
     * Returns binary list
     * @return
     */
    public List<Boolean> getBinaryList()
    {
	return outputSequence;
    }
}
