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

public class ChannelSignalSqr extends Signal
{
    private double noise;

    public ChannelSignalSqr(double freq, double ampl, double ph, double ns, double start, double end)
    {
        this.frequency = freq;
        this.amplitude = ampl;
        this.phase = ph;
	this.noise = ns;
	this.maxValue = ampl + Math.pow(ns, 2);
	this.xStart = start;
	this.xEnd = end;
    }

    @Override
    public double function(double x)
    {
	Random noise_generator = new Random();
	return Math.pow(this.amplitude * Math.sin(2 * Math.PI * this.frequency * x + this.phase) + this.noise * noise_generator.nextGaussian(), 2);
    }

    public double getNoise()
    {
	return this.noise;
    }
}