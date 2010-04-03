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

public class Signal implements IntegralFunction {
    double frequency;
    double amplitude;
    double phase;
    double maxValue;
    double xStart, xEnd;

    public double function(double x)
    {
	return 0;
    }

    public double getFrequency()
    {
	return this.frequency;
    }

    public double getAmplitude()
    {
	return this.amplitude;
    }

    public double getPhase()
    {
	return this.phase;
    }

    public double getMaxValue()
    {
	return this.maxValue;
    }

    public double getStart()
    {
	return this.xStart;
    }

    public double getEnd()
    {
	return this.xEnd;
    }
}
