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
 * Model of multiplier signal
 * @author Oleksandr Natalenko aka post-factum
 */
public class MultiplierSignal implements AnalogSignal
{
    double frequency;
    double amplitude;
    double phase;
    double maxValue;
    double minValue;
    double xStart, xEnd;
    
    private ChannelSignal channelSignal;
    private ModulatorSignal referenceSignal;

    /**
     * Creates multiplier signal
     * @param _channelSignal channel signal
     * @param _referenceSignal signal from reference generator
     */
    public MultiplierSignal(ChannelSignal _channelSignal, ModulatorSignal _referenceSignal)
    {
	channelSignal = _channelSignal;
	referenceSignal = _referenceSignal;

        frequency = channelSignal.getFrequency();
        amplitude = channelSignal.getAmplitude() * referenceSignal.getAmplitude();
        phase = channelSignal.getPhase();
	maxValue = Math.abs(_channelSignal.getMaxValue() * _referenceSignal.getMaxValue());
	minValue = -Math.abs(_channelSignal.getMinValue() * _referenceSignal.getMinValue());
	xStart = channelSignal.getStart();
	xEnd = channelSignal.getEnd();
    }

    /**
     * Returns f(x) for current signal
     * @param _x time variable, s
     * @return real value of signal function in x point
     */
    @Override
    public double function(double _x)
    {
	return channelSignal.function(_x) * referenceSignal.function(_x);
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
