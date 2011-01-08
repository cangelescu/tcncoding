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
 * @author post-factum
 */
public class MultiplierSignal extends AnalogSignal
{
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
}
