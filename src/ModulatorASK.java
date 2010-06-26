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
public class ModulatorASK
{

    private List sequence = null;
    private double bearerAmplitude, bearerFrequency, impulseLength;

    List<ModulatorSignal> modulatedSequence = new ArrayList<ModulatorSignal>();

    public ModulatorASK(double bearerAmplitude, double bearerFrequency, List symbols, double _impulseLength)
    {
	this.sequence = symbols;
	this.bearerAmplitude = bearerAmplitude;
	this.bearerFrequency = bearerFrequency;
	this.impulseLength = _impulseLength;
    }

    /**
     * Runs modulating
     */
    public void doModulation()
    {
	this.modulatedSequence.clear();

	double currentTime = 0;

	for (int j = 0; j < this.sequence.size(); j++)
	{
	    BinaryNumber workingNumber = (BinaryNumber)this.sequence.get(j);
	    boolean[] seq = workingNumber.getAlignedBinaryArray();

	    for (int i = 0; i < seq.length; i++)
	    {
		if (!seq[i])
		    this.modulatedSequence.add(new ModulatorSignal(0, 0, 0, currentTime, currentTime + this.impulseLength));
		else
		    this.modulatedSequence.add(new ModulatorSignal(bearerFrequency, bearerAmplitude, 0, currentTime, currentTime + this.impulseLength));
		currentTime += this.impulseLength;
	    }
	}
    }

    /**
     * Returns modulated signals
     * @return
     */
    public List<ModulatorSignal> getSignals()
    {
	return this.modulatedSequence;
    }
}
