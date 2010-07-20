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
public class TwistedInteger
{

    private int x1, x2;

    /**
     * Creates twisted integer
     * @param _x1
     * @param _x2
     */
    public TwistedInteger(int _x1, int _x2)
    {
	x1 = _x1;
	x2 = _x2;
    }

    /**
     * Gets first component
     * @return
     */
    public int getX1()
    {
	return x1;
    }

    /**
     * Gets second component
     * @return
     */
    public int getX2()
    {
	return x2;
    }
}
