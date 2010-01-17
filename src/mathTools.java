
import java.util.Vector;

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

    public double integrate(MathToolsFunction func, double x_begin, double x_end)
    {
	double a1 = (x_end - x_begin) / 2;
	double f1 = func.function(((x_begin + x_end) / 2) - ((x_end - x_begin) / Math.sqrt(12)));
	double f2 = func.function(((x_begin + x_end) / 2) + ((x_end - x_begin) / Math.sqrt(12)));
	return a1 * (f1 + f2);
    }
}
