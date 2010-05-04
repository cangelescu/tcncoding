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

/**
 *
 * @author post-factum
 */
public class Multiplier
{
    private double ethalonFrequency, ethalonAmplitude, ethalonPhase;
    private List<ChannelSignal> receivedSignal;
    private List<MultiplierSignal> output = new ArrayList<MultiplierSignal>();

    /**
     * Creates multiplier
     * @param freq ethalon frequency, Hz
     * @param amplitude ethalon amplitude, V
     * @param phase ethalon phase, rad
     * @param signal list of input signals
     */
    public Multiplier(double freq, double amplitude, double phase, List<ChannelSignal> signal)
    {
	this.ethalonFrequency = freq;
	this.ethalonAmplitude = amplitude;
	this.ethalonPhase = phase;
	this.receivedSignal = signal;
    }

    /**
     * Runs multiplying
     */
    public void doMultiply()
    {
	for (ChannelSignal crs: this.receivedSignal)
	{
	    MultiplierSignal mbfcrs = new MultiplierSignal(crs.getFrequency(), crs.getAmplitude(), crs.getPhase(), crs.getNoise(), this.ethalonFrequency, this.ethalonAmplitude, this.ethalonPhase, crs.getStart(), crs.getEnd());
	    this.output.add(mbfcrs);
	}
    }

    /**
     * Returns list of multiplied signals
     * @return
     */
    public List<MultiplierSignal> getSignals()
    {
	return this.output;
    }
}
