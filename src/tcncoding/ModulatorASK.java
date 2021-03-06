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

import java.util.List;

/**
 * Model of ASK modulator
 * @author Oleksandr Natalenko aka post-factum
 */
public class ModulatorASK extends Modulator
{

    /**
     * Creates ASK modulator
     * @param _bearerAmplitude bearer amplitude, V
     * @param _bearerFrequency bearer frequency, Hz
     * @param _symbols input binary sequence
     * @param _impulseLength length of impulse, s
     */
    public ModulatorASK(double _bearerAmplitude, double _bearerFrequency, List<BinaryNumber> _symbols, double _impulseLength)
    {
	inputSequence = _symbols;
	bearerAmplitude = _bearerAmplitude;
	bearerFrequency = _bearerFrequency;
	impulseLength = _impulseLength;
    }

    /**
     * Runs modulating
     * @return modulated sequence
     */
    public List<ModulatorSignal> getSignals()
    {
	modulatedSequence.clear();

	double currentTime = 0;

	for (BinaryNumber cbn: inputSequence)
	{
	    boolean[] bits = cbn.getBinaryArray();
	    for (boolean cb: bits)
	    {
		if (!cb)
		    modulatedSequence.add(new ModulatorSignal(0, 0, 0, currentTime, currentTime + impulseLength));
		else
		    modulatedSequence.add(new ModulatorSignal(bearerFrequency, bearerAmplitude, 0, currentTime, currentTime + impulseLength));
		currentTime += impulseLength;
	    }
	}
        return modulatedSequence;
    }
}
