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

import flanagan.integration.*;

public class ModulatorSignalFunction implements IntegralFunction
{
    private double frequency;
    private double amplitude;
    private double phase;
    private double max_value;

    public ModulatorSignalFunction(double freq, double ampl, double ph)
    {
        this.frequency = freq;
        this.amplitude = ampl;
        this.phase = ph;
	this.max_value = ampl;
    }

    public double function(double x)
    {
	return this.amplitude * Math.sin(2 * Math.PI * this.frequency * x + this.phase);
    }

    public double getFrequency()
    {
	return this.frequency;
    }

    public double getAmplitude()
    {
	return this.amplitude;
    }

    public double getPhase()
    {
	return this.phase;
    }

    public double getMaxValue()
    {
	return this.max_value;
    }
}
