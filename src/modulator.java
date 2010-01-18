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

import java.util.Vector;

public class modulator {

    public enum ModulationType {AMn, FMn, PMn, RPMn};

    private ModulationType using_method = null;
    private Vector sequence = null;
    private int alignment;

    Vector<FunctionStep> modulated_sequence = new Vector<FunctionStep>();

    public modulator(ModulationType mod_type, Vector symbols, int align)
    {
	this.using_method = mod_type;
	this.sequence = symbols;
	this.alignment = align;
    }

    private class BearerFunction implements MathToolsFunction
    {
	private double frequency, amplitude, phase;

	public BearerFunction(double freq, double ampl, double ph)
	{
	    this.frequency = freq;
	    this.amplitude = ampl;
	    this.phase = ph;
	}

	public double function(double x)
	{
	    return amplitude * Math.sin(2 * Math.PI * frequency * x + phase);
	}
    }

    public void doModulation()
    {
	Vector<Vector<FunctionStep>> res = new Vector<Vector<FunctionStep>>();
	this.modulated_sequence.clear();

	int len = this.alignment;

	mathTools mtools = new mathTools();

	double freq1 = 100E3;
	double freq2 = 200E3;
	double ampl = 50;
	int prev_phase = 1;

	BearerFunction amn0 = new BearerFunction(0, 0, 0);
	BearerFunction amn1 = new BearerFunction(freq1, ampl, 0);
	BearerFunction fmn0 = new BearerFunction(freq1, ampl, 0);
	BearerFunction fmn1 = new BearerFunction(freq2, ampl, 0);
	BearerFunction pmn0 = new BearerFunction(freq1, ampl, 0);
	BearerFunction pmn1 = new BearerFunction(freq1, ampl, -Math.PI);

	for (int j = 0; j < this.sequence.size(); j++)
	{
	    binaryNumber working_number = (binaryNumber)this.sequence.get(j);
	    boolean[] seq = working_number.toBinaryArray(this.alignment);

	    for (int i = 0; i < len; i++)
	    {
		switch (this.using_method)
		{
		    case AMn:
			if (!seq[i])
			    res.add(mtools.tabulate(amn0, 0, 1/freq1));
			else
			    res.add(mtools.tabulate(amn1, 0, 1/freq1));
			break;
		    case FMn:
			if (!seq[i])
			    res.add(mtools.tabulate(fmn0, 0, 1/freq1));
			else
			    res.add(mtools.tabulate(fmn1, 0, 1/freq1));
			break;
		    case PMn:
			if (!seq[i])
			    res.add(mtools.tabulate(pmn0, 0, 1/freq1));
			else
			    res.add(mtools.tabulate(pmn1, 0, 1/freq1));
			break;
		    case RPMn:
			if (!seq[i])
			{
			    if (prev_phase == 1)
			    {
				res.add(mtools.tabulate(pmn0, 0, 1/freq1));
				prev_phase = 1;
			    } else
			    if (prev_phase == -1)
			    {
				res.add(mtools.tabulate(pmn1, 0, 1/freq1));
				prev_phase = -1;
			    }
			} else
			{
			    if (prev_phase == 1)
			    {
				res.add(mtools.tabulate(pmn1, 0, 1/freq1));
				prev_phase = -1;
			    } else
			    if (prev_phase == -1)
			    {
				res.add(mtools.tabulate(pmn0, 0, 1/freq1));
				prev_phase = 1;
			    }
			}
			break;
		    default:
			break;
		}
	    }
	}

	double cx = 0;
	for (Vector<FunctionStep> cvfs: res)
	{
	    for (FunctionStep cfs: cvfs)
	    {
		cx += cfs.x;
		this.modulated_sequence.add(new FunctionStep(cx, cfs.y));
	    }
	}
    }

    public Vector<FunctionStep> getModulatedArray()
    {
	return this.modulated_sequence;
    }
}
