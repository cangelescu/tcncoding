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
public class MultiplierSignal extends Signal
{
    private double noise = 0;
    private double ethalonFrequency = 0;
    private double ethalonAmplitude = 0;
    private double ethalonPhase = 0;

    /**
     * Creates multiplier signal
     * @param _frequency signal frequency, Hz
     * @param _amplitude signal amplitude, V
     * @param _phase signal phase, rad
     * @param _noise noise amplitude, V
     * @param _ethalonFrequency ethalon frequency, Hz
     * @param _ethalonAmplitude ethalon amplitude, V
     * @param _ethalonPhase ethalon phase, rad
     * @param _start start time, s
     * @param _end end time, s
     */
    public MultiplierSignal(double _frequency, double _amplitude, double _phase, double _noise, double _ethalonFrequency, double _ethalonAmplitude, double _ethalonPhase, double _start, double _end)
    {
        frequency = _frequency;
        amplitude = _amplitude;
        phase = _phase;
	noise = _noise;
	ethalonFrequency = _ethalonFrequency;
	ethalonAmplitude = _ethalonAmplitude;
	ethalonPhase = _ethalonPhase;
	maxValue = (_amplitude + _noise) * _ethalonAmplitude;
	minValue = -(_amplitude + _noise) * _ethalonAmplitude;
	xStart = _start;
	xEnd = _end;
    }

    /**
     * Returns f(x) for current signal
     * @param x time variable, s
     * @return
     */
    @Override
    public double function(double x)
    {
	Random noise_generator = new Random();
	return (amplitude * Math.sin(2 * Math.PI * frequency * x + phase) + noise * noise_generator.nextGaussian()) *
	        (ethalonAmplitude * Math.sin(2 * Math.PI * ethalonFrequency * x + ethalonPhase));
    }

    /**
     * Returns noise amplitude, V
     * @return
     */
    public double getNoise()
    {
	return noise;
    }
}
