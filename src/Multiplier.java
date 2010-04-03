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

import java.util.ArrayList;
import java.util.List;

public class Multiplier {
    private double ethalon_frequency, ethalon_amplitude, ethalon_phase;
    private List<ChannelSignal> received_signal;
    private List<MultiplierSignal> output = new ArrayList<MultiplierSignal>();

    public Multiplier(double freq, double amplitude, double phase, List<ChannelSignal> signal)
    {
	this.ethalon_frequency = freq;
	this.ethalon_amplitude = amplitude;
	this.ethalon_phase = phase;
	this.received_signal = signal;
    }

    public void doMultiply()
    {
	for (ChannelSignal crs: this.received_signal)
	{
	    MultiplierSignal mbfcrs = new MultiplierSignal(crs.getFrequency(), crs.getAmplitude(), crs.getPhase(), crs.getNoise(), this.ethalon_frequency, this.ethalon_amplitude, this.ethalon_phase, crs.getStart(), crs.getEnd());
	    this.output.add(mbfcrs);
	}
    }

    public List<MultiplierSignal> getSignals()
    {
	return this.output;
    }
}
