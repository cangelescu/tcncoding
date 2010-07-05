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
    private List<List<ChannelSignal>> receivedSignals;
    private List<List<MultiplierSignal>> output = new ArrayList<List<MultiplierSignal>>();

    /**
     * Creates multiplier
     * @param _frequency ethalon frequency, Hz
     * @param _amplitude ethalon amplitude, V
     * @param _phase ethalon phase, rad
     * @param _signals list of input signals
     */
    public Multiplier(double _frequency, double _amplitude, double _phase, List<List<ChannelSignal>> _signals)
    {
	ethalonFrequency = _frequency;
	ethalonAmplitude = _amplitude;
	ethalonPhase = _phase;
	receivedSignals = _signals;
    }

    /**
     * Runs multiplying
     */
    public void doMultiply()
    {
	for (List<ChannelSignal> clcs: receivedSignals)
	{
	    List<MultiplierSignal> newMultiplierSignalList = new ArrayList<MultiplierSignal>();
	    for (ChannelSignal crs: clcs)
	    {
		MultiplierSignal mbfcrs = new MultiplierSignal(crs.getFrequency(), crs.getAmplitude(), crs.getPhase(), crs.getNoise(), ethalonFrequency, ethalonAmplitude, ethalonPhase, crs.getStart(), crs.getEnd());
		newMultiplierSignalList.add(mbfcrs);
	    }
	    output.add(newMultiplierSignalList);
	}
    }

    /**
     * Returns list of multiplied signals
     * @return
     */
    public List<List<MultiplierSignal>> getSignals()
    {
	return output;
    }
}
