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

public class Integrator {
    private List<MultiplierSignal> signals;
    private List<List<FunctionStep>> out = new ArrayList<List<FunctionStep>>();

    public Integrator(List<MultiplierSignal> newSignals)
    {
	this.signals = newSignals;
    }

    public void doIntegrating()
    {
	this.out.clear();
	double cx = 0;
	for (MultiplierSignal cs: this.signals)
	{
	    cx += cs.getStart();
	    List<FunctionStep> current = new ArrayList<FunctionStep>();
	    double length = cs.getEnd() - cs.getStart();
	    double step = Math.pow(Math.sqrt(3) * Math.E, Math.log(length));
	    double sum = 0;
	    double sp = cs.getStart();
	    while (sp <= cs.getEnd())
	    {
		double area = cs.function(cx) * step;
		sum += area;
		current.add(new FunctionStep(cx, sum));
		sp += step;
		if (sp > cs.getEnd())
		    break;
		cx += step;
	    }
	    this.out.add(current);
	}
    }

    public List<List<FunctionStep>> getIntegrals()
    {
	return this.out;
    }
}
