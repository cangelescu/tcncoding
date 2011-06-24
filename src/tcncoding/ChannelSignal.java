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

/**
 * Model of channel signal
 * @author Oleksandr Natalenko aka post-factum
 */
public class ChannelSignal implements AnalogSignal
{
    double frequency;
    double amplitude;
    double phase;
    double maxValue;
    double minValue;
    double xStart, xEnd;
    
    private NoiseSignal noiseSignal;

    /**
     * Creates channel signal with given parameters
     * @param _modulatorSignal modulator signal on the input of the channel
     * @param _noiseSignal noise signal
     */
    public ChannelSignal(ModulatorSignal _modulatorSignal, NoiseSignal _noiseSignal)
    {
        frequency = _modulatorSignal.getFrequency();
        amplitude = _modulatorSignal.getAmplitude();
        phase = _modulatorSignal.getPhase();
	noiseSignal = _noiseSignal;
	maxValue = amplitude + noiseSignal.getMaxValue();
	minValue = -(amplitude + noiseSignal.getMaxValue());
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
	return amplitude * Math.sin(2 * Math.PI * frequency * _x + phase) + noiseSignal.function(_x);
    }
    
    /**
     * Returns signal frequency, Hz
     * @return real value of signal frequency
     */
    public double getFrequency()
    {
	return frequency;
    }

    /**
     * Returns signal amplitude, V
     * @return real value of signal amplitude
     */
    public double getAmplitude()
    {
	return amplitude;
    }

    /**
     * Returns signal phase, rad
     * @return real value of signal phase
     */
    public double getPhase()
    {
	return phase;
    }

    /**
     * Returns signal maximum value, V
     * @return real maximum signal value
     */
    public double getMaxValue()
    {
	return maxValue;
    }

    /**
     * Returns signal minimum value, V
     * @return real minimum signal value
     */
    public double getMinValue()
    {
	return minValue;
    }

    /**
     * Returns signal start point, s
     * @return real signal start time
     */
    public double getStart()
    {
	return xStart;
    }

    /**
     * Returns signal end point, s
     * @return real signal end time
     */
    public double getEnd()
    {
	return xEnd;
    }
}
