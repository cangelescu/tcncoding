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

public class multiplier {
    private Vector<FunctionStep> ethalon = new Vector<FunctionStep>();
    private Vector<FunctionStep> received_signal = new Vector<FunctionStep>();
    private Vector<FunctionStep> output = new Vector<FunctionStep>();

    mathTools mtools = new mathTools();

    public multiplier(double freq, double amplitude, double phase, Vector<FunctionStep> signal)
    {
	this.ethalon = mtools.tabulate(new BearerFunction(freq, amplitude, phase), 0, 1 / freq);
	this.received_signal = signal;
    }

    public void doMultiply()
    {
	int index = 0;
	for (int i = 0; i < this.received_signal.size(); i++)
	{
	    this.output.add(new FunctionStep(this.received_signal.elementAt(i).x, this.received_signal.elementAt(i).y * this.ethalon.elementAt(index).y));
	    index++;
	    if (index > this.ethalon.size() - 1)
		index = 0;
	}
    }

    public Vector<FunctionStep> getSignal()
    {
	return this.output;
    }
}
