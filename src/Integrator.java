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
    private List<MultiplierSignal> signals;
    private List<List<FunctionStep>> out = new ArrayList<List<FunctionStep>>();

    /**
     * Creates integrator for input signals
     * @param _signals list of input signals
     */
    public Integrator(List<MultiplierSignal> _signals)
    {
	signals = _signals;
    }

    /**
     * Runs integrating
     */
    public void doIntegrating()
    {
	out.clear();
	for (MultiplierSignal cs: signals)
	{
	    List<FunctionStep> current = new ArrayList<FunctionStep>();
	    double length = cs.getEnd() - cs.getStart();
	    double step = Math.pow(Math.sqrt(3) * Math.E, Math.log(length));
	    double sum = 0;
	    double sp = cs.getStart();
	    while (sp <= cs.getEnd())
	    {
		double area = cs.function(sp) * step;
		sum += area;
		current.add(new FunctionStep(sp, sum));
		sp += step;
	    }
	    out.add(current);
	}
    }

    /**
     * Returns list of lists of function steps
     * @return
     */
    public List<List<FunctionStep>> getIntegrals()
    {
	return out;
    }
}
