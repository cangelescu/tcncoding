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
 * Models digital sample
 * @author Oleksandr Natalenko aka post-factum
 */
public class Sample
{
    private double x, y;

    /**
     * Creates digital sample
     * @param _x x value of function
     * @param _y y value of function
     */
    public Sample(double _x, double _y)
    {
	x = _x;
	y = _y;
    }

    /**
     * Returns x value
     * @return real x value of sample
     */
    public double getX()
    {
	return x;
    }

    /**
     * Returns y value
     * @return real y value of sample
     */
    public double getY()
    {
	return y;
    }
}
