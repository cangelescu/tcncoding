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

/**
 * Model of multiplier signal
 * @author post-factum
 */
public class MultiplierSignal extends AnalogSignal
{
    private ChannelSignal channelSignal;
    private ModulatorSignal ethalonSignal;
    private double noise;

    /**
     * Creates multiplier signal
     * @param _channelSignal channel signal
     * @param _ethalonSignal signal from ethalon generator
     */
    public MultiplierSignal(ChannelSignal _channelSignal, ModulatorSignal _ethalonSignal)
    {
	channelSignal = _channelSignal;
	ethalonSignal = _ethalonSignal;

        frequency = channelSignal.getFrequency();
        amplitude = channelSignal.getAmplitude() * ethalonSignal.getAmplitude();
        phase = channelSignal.getPhase();
	noise = channelSignal.getNoise();
	maxValue = Math.abs(_channelSignal.getMaxValue() * _ethalonSignal.getMaxValue());
	minValue = -Math.abs(_channelSignal.getMinValue() * _ethalonSignal.getMinValue());
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
	return channelSignal.function(_x) * ethalonSignal.function(_x);
    }

    /**
     * Returns noise level, V
     * @return real value of noise level
     */
    public double getNoise()
    {
	return noise;
    }
}
