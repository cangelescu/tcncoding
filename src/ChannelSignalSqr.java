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

/**
 *
 * @author post-factum
 */
public class ChannelSignalSqr extends Signal
{
    private double noise;

    /**
     * Creates channel signal^2 with given parameters
     * @param _frequency frequency of signal, Hz
     * @param _amplitude amplitude of signal, V
     * @param _phase phase of signal, rad
     * @param _noise amplitude of noise, V
     * @param _start start time, s
     * @param _end end time, s
     */
    public ChannelSignalSqr(double _frequency, double _amplitude, double _phase, double _noise, double _start, double _end)
    {
        frequency = _frequency;
        amplitude = _amplitude;
        phase = _phase;
	noise = _noise;
	maxValue = _amplitude + Math.pow(_noise, 2);
	xStart = _start;
	xEnd = _end;
    }

    /**
     * Returns f(x) value for this signal
     * @param x time variable, s
     * @return
     */
    @Override
    public double function(double x)
    {
	Random noise_generator = new Random();
	return Math.pow(amplitude * Math.sin(2 * Math.PI * frequency * x + phase) + noise * noise_generator.nextGaussian(), 2);
    }

    /**
     * Returns amplitude of noise
     * @return
     */
    public double getNoise()
    {
	return noise;
    }
}
