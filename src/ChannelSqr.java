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

public class ChannelSqr {
    private Vector<ModulatorSignal> input_signals;
    private Vector<ChannelSignalSqr> output_signals = new Vector<ChannelSignalSqr>();

    public ChannelSqr(Vector<ModulatorSignal> new_input_signals)
    {
	this.input_signals = new_input_signals;
    }

    public void doNoising()
    {
	for(ModulatorSignal cs: this.input_signals)
	{

	    ChannelSignalSqr ncfs = new ChannelSignalSqr(cs.getFrequency(), cs.getAmplitude(), cs.getPhase(), 4, cs.getStart(), cs.getEnd());
	    this.output_signals.add(ncfs);
	}
    }

    public Vector<ChannelSignalSqr> getSignals()
    {
	return this.output_signals;
    }
}
