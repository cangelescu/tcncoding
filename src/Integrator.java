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

import flanagan.integration.*;
import java.util.Vector;

public class Integrator {
    private Vector<MultiplierSignal> signals;
    private Vector<Double> out = new Vector<Double>();

    public Integrator(Vector<MultiplierSignal> new_signals)
    {
	this.signals = new_signals;
    }

    public void doIntegrating()
    {
	for (MultiplierSignal cs: this.signals)
	    this.out.add(Integration.gaussQuad(cs, cs.getStart(), cs.getEnd(), 1000));
    }

    public Vector<Double> getIntegrals()
    {
	return out;
    }
}
