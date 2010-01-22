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

import java.util.Random;

public class BearerFunction implements MathToolsFunction
{
    private double frequency;
    private double amplitude;
    private double phase;
    private double noise = 0;
    private double ethalon_frequency = 0;
    private double ethalon_amplitude = 0;
    private double ethalon_phase = 0;
    private double max_value;

    public BearerFunction(double freq, double ampl, double ph)
    {
        this.frequency = freq;
        this.amplitude = ampl;
        this.phase = ph;
	this.max_value = ampl;
    }

    public BearerFunction(double freq, double ampl, double ph, double ns)
    {
        this.frequency = freq;
        this.amplitude = ampl;
        this.phase = ph;
	this.noise = ns;
	this.max_value = ampl + ns;
    }

    public BearerFunction(double freq, double ampl, double ph, double ns, double efreq, double eampl, double eph)
    {
        this.frequency = freq;
        this.amplitude = ampl;
        this.phase = ph;
	this.noise = ns;
	this.ethalon_frequency = efreq;
	this.ethalon_amplitude = eampl;
	this.ethalon_phase = eph;
	this.max_value = (ampl + ns) * eampl;
    }

    public double function(double x)
    {
	if (this.noise == 0)
	    return this.amplitude * Math.sin(2 * Math.PI * this.frequency * x + this.phase);
	else
	{
	    Random noise_generator = new Random();
	    if (this.ethalon_frequency == 0 && this.ethalon_amplitude == 0 && this.ethalon_phase == 0)
		return this.amplitude * Math.sin(2 * Math.PI * this.frequency * x + this.phase) + this.noise * noise_generator.nextGaussian();
	    else
		return (this.amplitude * Math.sin(2 * Math.PI * this.frequency * x + this.phase) + this.noise * noise_generator.nextGaussian()) *
		        (this.ethalon_amplitude * Math.sin(2 * Math.PI * this.ethalon_frequency * x + this.ethalon_phase));
	}
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

    public double getNoise()
    {
	return this.noise;
    }

    public double getMaxValue()
    {
	return this.max_value;
    }
}
