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
public class ModulatorPSK
{

    private List sequence = null;
    private double bearerAmplitude, bearerFrequency, impulseLength;

    List<ModulatorSignal> modulatedSequence = new ArrayList<ModulatorSignal>();

    /**
     * Creates PSK modulator
     * @param _bearerAmplitude bearer amplitude, V
     * @param _bearerFrequency bearer frequency, Hz
     * @param _symbols input binary sequence
     * @param _impulseLength length of impulse, s
     */
    public ModulatorPSK(double _bearerAmplitude, double _bearerFrequency, List _symbols, double _impulseLength)
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

	Rectifier rectifier = new Rectifier(sequence);
	rectifier.doRectifying();
	boolean[] rectifiedArray = rectifier.getArray();
	for (boolean cb: rectifiedArray)
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
