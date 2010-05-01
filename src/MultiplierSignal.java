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

public class MultiplierSignal extends Signal
{
    private double noise = 0;
    private double ethalonFrequency = 0;
    private double ethalonAmplitude = 0;
    private double ethalonPhase = 0;

    public MultiplierSignal(double freq, double ampl, double ph, double ns, double efreq, double eampl, double eph, double start, double end)
    {
        this.frequency = freq;
        this.amplitude = ampl;
        this.phase = ph;
	this.noise = ns;
	this.ethalonFrequency = efreq;
	this.ethalonAmplitude = eampl;
	this.ethalonPhase = eph;
	this.maxValue = (ampl + ns) * eampl;
	this.xStart = start;
	this.xEnd = end;
    }

    @Override
    public double function(double x)
    {
	Random noise_generator = new Random();
	return (this.amplitude * Math.sin(2 * Math.PI * this.frequency * x + this.phase) + this.noise * noise_generator.nextGaussian()) *
	        (this.ethalonAmplitude * Math.sin(2 * Math.PI * this.ethalonFrequency * x + this.ethalonPhase));
    }

    public double getNoise()
    {
	return this.noise;
    }
}