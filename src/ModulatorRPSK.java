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
 * Model of RPSK modulator
 * @author post-factum
 */
public class ModulatorRPSK
{

    private List<BinaryNumber> sequence = null;
    private double bearerAmplitude, bearerFrequency, impulseLength;

    List<List<ModulatorSignal>> modulatedSequence = new ArrayList<List<ModulatorSignal>>();

    /**
     * Creates RPSK modulator (same as PSK, but recodes sequence first)
     * @param _bearerAmplitude bearer amplitude, V
     * @param _bearerFrequency bearer frequency, Hz
     * @param _symbols input binary sequence
     * @param _impulseLength length of impulse, s
     */
    public ModulatorRPSK(double _bearerAmplitude, double _bearerFrequency, List<BinaryNumber> _symbols, double _impulseLength)
    {
	sequence = _symbols;
	bearerAmplitude = _bearerAmplitude;
	bearerFrequency = _bearerFrequency;
	impulseLength = _impulseLength;
    }

    /**
     * Runs modulating
     */
    public void doModulation()
    {
	modulatedSequence.clear();

	double currentTime = 0;

	ModulatorRPSKRecoder recoder = new ModulatorRPSKRecoder(sequence);
	recoder.doEncoding();
	List<BinaryNumber> recodedList = recoder.getList();

	for (BinaryNumber cbn: recodedList)
	{
	    Splitter splitter = new Splitter(cbn);
	    splitter.doSplitting();
	    boolean[] bits = splitter.getBlocks().get(0).getBinaryArray();
	    List<ModulatorSignal> currentSymbolSignals = new ArrayList<ModulatorSignal>();
	    for (boolean cb: bits)
	    {
		if (!cb)
		    currentSymbolSignals.add(new ModulatorSignal(bearerFrequency, bearerAmplitude, 0, currentTime, currentTime + impulseLength));
		else
		    currentSymbolSignals.add(new ModulatorSignal(bearerFrequency, bearerAmplitude, -Math.PI, currentTime, currentTime + impulseLength));
		currentTime += impulseLength;
	    }
	    modulatedSequence.add(currentSymbolSignals);
	}
    }

    /**
     * Returns modulated signals
     * @return list of modulated signals
     */
    public List<List<ModulatorSignal>> getSignals()
    {
	return modulatedSequence;
    }
}
