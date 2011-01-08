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

package tcncoding;

import java.util.Random;

/**
 * Model of channel signal
 * @author post-factum
 */
public class NoiseSignal extends AnalogSignal
{
    private double power;
    Random noiseGenerator = new Random();

    /**
     * Creates channel signal with given parameters
     * @param _power power of noise, W
     * @param _start begin of noise signal, s
     * @param _end end of noise signal, s
     */
    public NoiseSignal(double _power, double _start, double _end)
    {
        power = _power;
        amplitude = Math.sqrt(power);
	maxValue = Math.PI * amplitude;
	minValue = -Math.PI * amplitude;
        xStart = _start;
	xEnd = _end;
    }

    /**
     * Returns f(x) value for this signal
     * @param _x time variable, s
     * @return real value of signal function in x point
     */
    @Override
    public double function(double _x)
    {
	return amplitude * noiseGenerator.nextGaussian();
    }
}
