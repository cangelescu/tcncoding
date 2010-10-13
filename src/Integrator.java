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
public class Integrator
{
    private List<List<MultiplierSignal>> signals;
    private List<List<List<FunctionStep>>> out = new ArrayList<List<List<FunctionStep>>>();
    private double step;

    /**
     * Creates integrator for input signals
     * @param _signals list of input signals
     */
    public Integrator(List<List<MultiplierSignal>> _signals, double _maxFrequency, double _maxWidth)
    {
	signals = _signals;
	double end = _signals.get(_signals.size() - 1).get(_signals.get(_signals.size() - 1).size() - 1).getEnd();
	double step1 = end / _maxWidth;
	double step2 = 1 / (3 * _maxFrequency);
	step = Math.min(step1, step2);
    }

    /**
     * Runs integrating
     */
    public void doIntegrating()
    {
	out.clear();
	for (List<MultiplierSignal> clms: signals)
	{
	    List<List<FunctionStep>> newBlock = new ArrayList<List<FunctionStep>>();
	    for (MultiplierSignal cms: clms)
	    {
		List<FunctionStep> newSymbol = new ArrayList<FunctionStep>();
		double sum = 0;
		double sp = cms.getStart();
		while (sp <= cms.getEnd())
		{
		    double area = cms.function(sp) * step;
		    sum += area;
		    newSymbol.add(new FunctionStep(sp, sum));
		    sp += step;
		}
		newBlock.add(newSymbol);
	    }
	    out.add(newBlock);
	}
    }

    /**
     * Returns list of lists of function steps
     * @return
     */
    public List<List<List<FunctionStep>>> getIntegrals()
    {
	return out;
    }

    /**
     * Returns step size
     * @return
     */
    public double getStepSize()
    {
	return step;
    }
}
