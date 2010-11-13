package tcncoding;

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
 * Models ethalon generator of correlative receiver
 * @author post-factum
 */
public class EthalonGenerator
{
    private double ethalonFrequency, ethalonAmplitude, ethalonPhase;
    private List<List<ChannelSignal>> receivedSignals;
    private List<List<ModulatorSignal>> output = new ArrayList<List<ModulatorSignal>>();

    /**
     * Creates ethalon generator
     * @param _frequency ethalon frequency, Hz
     * @param _amplitude ethalon amplitude, V
     * @param _phase ethalon phase, rad
     * @param _signals list of input signals
     */
    public EthalonGenerator(double _frequency, double _amplitude, double _phase, List<List<ChannelSignal>> _signals)
    {
	ethalonFrequency = _frequency;
	ethalonAmplitude = _amplitude;
	ethalonPhase = _phase;
	receivedSignals = _signals;
    }

    /**
     * Runs generating
     */
    public void generate()
    {
	for (List<ChannelSignal> clcs: receivedSignals)
	{
	    List<ModulatorSignal> newModulatorSignalList = new ArrayList<ModulatorSignal>();
	    for (ChannelSignal crs: clcs)
	    {
		ModulatorSignal mbfcrs = new ModulatorSignal(ethalonFrequency, ethalonAmplitude, ethalonPhase, crs.getStart(), crs.getEnd());
		newModulatorSignalList.add(mbfcrs);
	    }
	    output.add(newModulatorSignalList);
	}
    }

    /**
     * Returns list of generated signals
     * @return list of ethalon signals
     */
    public List<List<ModulatorSignal>> getSignals()
    {
	return output;
    }
}
