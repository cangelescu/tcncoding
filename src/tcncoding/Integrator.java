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
 * Models integrator device
 * @author post-factum
 */
public class Integrator
{
    private List<List<MultiplierSignal>> signals;
    private List<List<DigitalSignal>> out = new ArrayList<List<DigitalSignal>>();
    private double step;

    /**
     * Creates integrator for input signals
     * @param _signals list of input signals
     * @param _maxFrequency maximum frequency of a signal
     * @param _maxWidth maximum width of visible area
     */
    public Integrator(List<List<MultiplierSignal>> _signals, double _maxFrequency, double _maxWidth)
    {
	signals = _signals;

	int lastBlock = _signals.size() - 1;
	int lastSignal = _signals.get(lastBlock).size() - 1;
	double end = _signals.get(lastBlock).get(lastSignal).getEnd();

	//uses display width
	double step1 = end / _maxWidth;
	//Kotelnokov's teorem works here not in boundary case
	double step2 = 1 / (3 * _maxFrequency);

	//takes less step for more accuracy
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
	    List<DigitalSignal> newBlock = new ArrayList<DigitalSignal>();
	    for (MultiplierSignal cms: clms)
	    {
		List<Sample> newSymbol = new ArrayList<Sample>();
		double sum = 0;
		double sp = cms.getStart();
		while (sp <= cms.getEnd())
		{
		    //integrate using method of rectangles
		    double area = (cms.function(sp) + cms.function(sp + step)) * step / 2;
		    newSymbol.add(new Sample(sp, sum));
		    sum += area;
		    sp += step;
		}
		DigitalSignal newDigitalSignal = new DigitalSignal(newSymbol);
		newBlock.add(newDigitalSignal);
	    }
	    out.add(newBlock);
	}
    }

    /**
     * Returns list of lists of digital signals
     * @return digital integration result of input signal
     */
    public List<List<DigitalSignal>> getIntegrals()
    {
	return out;
    }
}
