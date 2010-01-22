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

public class channel {
    private Vector<Signal> input_signals;
    private Vector<Signal> output_signals = new Vector<Signal>();

    public channel(Vector<Signal> new_input_signals)
    {
	this.input_signals = new_input_signals;
    }

    public void doNoising()
    {
	for(Signal cs: this.input_signals)
	{
	    SignalFunction csf = cs.getFunction();
	    SignalFunction ncfs = new SignalFunction(csf.getFrequency(), csf.getAmplitude(), csf.getPhase(), 4);
	    this.output_signals.add(new Signal(ncfs, cs.getStart(), cs.getEnd()));
	}
    }

    public Vector<Signal> getSignals()
    {
	return this.output_signals;
    }
}
