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
public class ModulatorRPSK
{

    private List<BinaryNumber> sequence = null;
    private double bearerAmplitude, bearerFrequency, impulseLength;

    List<ModulatorSignal> modulatedSequence = new ArrayList<ModulatorSignal>();

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

	Splitter splitter = new Splitter(sequence);
	splitter.doSplitting();
	boolean[] splittedArray = splitter.getBlocks().get(0).getBinaryArray();

	ModulatorRPSKRecoder recoder = new ModulatorRPSKRecoder(splittedArray);
	recoder.doRecoding();
	boolean[] recodedArray = recoder.getArray();
	for (boolean cb: recodedArray)
	{
	    if (!cb)
		modulatedSequence.add(new ModulatorSignal(bearerFrequency, bearerAmplitude, 0, currentTime, currentTime + impulseLength));
	    else
		modulatedSequence.add(new ModulatorSignal(bearerFrequency, bearerAmplitude, -Math.PI, currentTime, currentTime + impulseLength));
	    currentTime += impulseLength;
	}
    }

    /**
     * Returns modulated signals
     * @return
     */
    public List<ModulatorSignal> getSignals()
    {
	return modulatedSequence;
    }
}
