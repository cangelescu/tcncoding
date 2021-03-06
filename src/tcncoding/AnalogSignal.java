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
 * Base class of analog signal model
 * @author Oleksandr Natalenko aka post-factum
 */
public interface AnalogSignal
{
    /**
     * Returns f(x) for current signal
     * @param _x time variable, s
     * @return real value of signal function in x point
     */
    public double function(double _x);

    /**
     * Returns signal frequency, Hz
     * @return real value of signal frequency
     */
    public double getFrequency();

    /**
     * Returns signal amplitude, V
     * @return real value of signal amplitude
     */
    public double getAmplitude();

    /**
     * Returns signal phase, rad
     * @return real value of signal phase
     */
    public double getPhase();

    /**
     * Returns signal maximum value, V
     * @return real maximum signal value
     */
    public double getMaxValue();

    /**
     * Returns signal minimum value, V
     * @return real minimum signal value
     */
    public double getMinValue();

    /**
     * Returns signal start point, s
     * @return real signal start time
     */
    public double getStart();

    /**
     * Returns signal end point, s
     * @return real signal end time
     */
    public double getEnd();
}
