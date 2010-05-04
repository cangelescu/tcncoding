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

import flanagan.integration.IntegralFunction;

/**
 *
 * @author post-factum
 */
public class Signal implements IntegralFunction
{
    double frequency;
    double amplitude;
    double phase;
    double maxValue;
    double xStart, xEnd;

    /**
     * Returns f(x) for current signal
     * @param x time variable, s
     * @return
     */
    public double function(double x)
    {
	return 0;
    }

    /**
     * Returns signal frequency, Hz
     * @return
     */
    public double getFrequency()
    {
	return this.frequency;
    }

    /**
     * Returns signal amplitude, V
     * @return
     */
    public double getAmplitude()
    {
	return this.amplitude;
    }

    /**
     * Returns signal phase, rad
     * @return
     */
    public double getPhase()
    {
	return this.phase;
    }

    /**
     * Returns signal maximum value, V
     * @return
     */
    public double getMaxValue()
    {
	return this.maxValue;
    }

    /**
     * Returns signal start point, s
     * @return
     */
    public double getStart()
    {
	return this.xStart;
    }

    /**
     * Returns signal end point, s
     * @return
     */
    public double getEnd()
    {
	return this.xEnd;
    }
}
