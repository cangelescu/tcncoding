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

import java.util.Vector;

public class mathTools {
    private double step;

    public mathTools(double precision)
    {
	this.step = precision;
    }

    public mathTools()
    {
	this.step = 1.0E-9;
    }

    public Vector<FunctionStep> tabulate(MathToolsFunction func, double x_begin, double x_end)
    {
	double current_x = x_begin;
	Vector<FunctionStep> out = new Vector<FunctionStep>();
	while (current_x <= x_end)
	{
	    out.add(new FunctionStep(current_x, func.function(current_x)));
	    current_x += this.step;
	}
	return out;
    }

    public double integrate(Vector<FunctionStep> func)
    {
	double out = 0;
	for (FunctionStep n: func)
	    out += n.y * this.step;
	return out;
    }

    public Vector<FunctionStep> multiply(Vector<FunctionStep> func1, Vector<FunctionStep> func2)
    {
	Vector<FunctionStep> out = new Vector<FunctionStep>();
	for (int i = 0; i < func1.size(); i++)
	{
	    FunctionStep result = null;
	    result.x = func1.elementAt(i).x;
	    result.y = func1.elementAt(i).y * func2.elementAt(i).y;
	    out.add(result);
	}
	return out;
    }
}
