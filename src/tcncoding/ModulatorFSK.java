/*

 Copyright (C) 2009-2011 Oleksandr Natalenko aka post-factum

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

package tcncoding;

import java.util.ArrayList;
import java.util.List;

/**
 * Model of FSK modulator
 * @author Oleksandr Natalenko aka post-factum
 */
public class ModulatorFSK extends Modulator
{

    private double bearerFrequencyDeviation;

    /**
     * Creates FSK modulator with phase breaking
     * @param _bearerAmplitude bearer amplitude, V
     * @param _bearerFrequency bearer frequency, Hz
     * @param _bearerFrequencyDeviation bearer frequency deviation, Hz
     * @param _inputSequence input binary sequence
     * @param _impulseLength length of impulse, s
     */
    public ModulatorFSK(double _bearerAmplitude, double _bearerFrequency, double _bearerFrequencyDeviation, List<BinaryNumber> _inputSequence, double _impulseLength)
    {
	inputSequence = _inputSequence;
	bearerAmplitude = _bearerAmplitude;
	bearerFrequency = _bearerFrequency;
	bearerFrequencyDeviation = _bearerFrequencyDeviation;
	impulseLength = _impulseLength;
    }

    /**
     * Runs modulating
     * @return modulated sequence
     */
    public List<List<ModulatorSignal>> getSignals()
    {
	modulatedSequence.clear();

	double currentTime = 0;

	for (BinaryNumber cbn: inputSequence)
	{
	    boolean[] bits = cbn.getBinaryArray();
	    List<ModulatorSignal> currentSymbolSignals = new ArrayList<ModulatorSignal>();
	    for (boolean cb: bits)
	    {
		if (!cb)
		    currentSymbolSignals.add(new ModulatorSignal(bearerFrequency - bearerFrequencyDeviation, bearerAmplitude, 0, currentTime, currentTime + impulseLength));
		else
		    currentSymbolSignals.add(new ModulatorSignal(bearerFrequency + bearerFrequencyDeviation, bearerAmplitude, 0, currentTime, currentTime + impulseLength));
		currentTime += impulseLength;
	    }
	    modulatedSequence.add(currentSymbolSignals);
	}
        return modulatedSequence;
    }
}
