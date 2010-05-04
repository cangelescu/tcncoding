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
 *
 * @author post-factum
 */
public class FunctionStep
{
    private double x, y;

    /**
     * Creates step of function
     * @param _x x value of function
     * @param _y y value of function
     */
    public FunctionStep(double _x, double _y)
    {
	this.x = _x;
	this.y = _y;
    }

    /**
     * Returns x value
     * @return
     */
    public double getX()
    {
	return this.x;
    }

    /**
     * Returns y value
     * @return
     */
    public double getY()
    {
	return this.y;
    }
}
