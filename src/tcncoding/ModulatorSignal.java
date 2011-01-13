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
 * Model of modulator signal
 * @author Oleksandr Natalenko aka post-factum
 */
public class ModulatorSignal extends AnalogSignal
{
    /**
     * Creates modulator signal
     * @param _frequency frequency, Hz
     * @param _amplitude amplitude, V
     * @param _phase phase, rad
     * @param _start start time, s
     * @param _end end time, s
     */
    public ModulatorSignal(double _frequency, double _amplitude, double _phase, double _start, double _end)
    {
        frequency = _frequency;
        amplitude = _amplitude;
        phase = _phase;
	maxValue = _amplitude;
	minValue = -_amplitude;
	xStart = _start;
	xEnd = _end;
    }

    /**
     * Returns f(x) for current signal
     * @param _x time variable, s
     * @return real value of signal function in x point
     */
    @Override
    public double function(double _x)
    {
	return amplitude * Math.sin(2 * Math.PI * frequency * _x + phase);
    }
}
