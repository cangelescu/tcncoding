package tcncoding;

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
 * Model of channel signal
 * @author post-factum
 */
public class ChannelSignal extends AnalogSignal
{
    private double noise;

    /**
     * Creates channel signal with given parameters
     * @param _modulatorSignal modulator signal on the input of the channel
     * @param _noise amplitude of noise, V
     */
    public ChannelSignal(ModulatorSignal _modulatorSignal, double _noise)
    {
        frequency = _modulatorSignal.getFrequency();
        amplitude = _modulatorSignal.getAmplitude();
        phase = _modulatorSignal.getPhase();
	noise = _noise;
	maxValue = amplitude + 3 * Math.sqrt(noise);
	minValue = -(amplitude + 3 * Math.sqrt(noise));
	xStart = _modulatorSignal.getStart();
	xEnd = _modulatorSignal.getEnd();
    }

    /**
     * Returns f(x) value for this signal
     * @param _x time variable, s
     * @return real value of signal function in x point
     */
    @Override
    public double function(double _x)
    {
	Random noiseGenerator = new Random();
	return amplitude * Math.sin(2 * Math.PI * frequency * _x + phase) + Math.sqrt(noise) * noiseGenerator.nextGaussian();
    }

    /**
     * Returns amplitude of noise
     * @return real value of noise
     */
    public double getNoise()
    {
	return noise;
    }
}
