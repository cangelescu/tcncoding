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

public class ChannelSignal {
    private ChannelSignalFunction bearer_function = null;
    private double x_start, x_end;

    public ChannelSignal(ChannelSignalFunction new_bearer_function, double new_x_start, double new_x_end)
    {
	this.bearer_function = new_bearer_function;
	this.x_start = new_x_start;
	this.x_end = new_x_end;
    }

    public ChannelSignalFunction getFunction()
    {
	return this.bearer_function;
    }

    public double getStart()
    {
	return x_start;
    }

    public double getEnd()
    {
	return x_end;
    }
}
