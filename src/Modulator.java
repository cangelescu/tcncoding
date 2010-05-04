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
public class Modulator
{

    /**
     * List of modulation type
     */
    public enum ModulationType
    {

	/**
	 * Amplitude shift keying
	 */
	ASK,
	/**
	 * Frequency shift keying
	 */
	FSK,
	/**
	 * Phase shift keying
	 */
	PSK,
	/**
	 * Relative phase shift keying
	 */
	RPSK;
    };

    private ModulationType usingMethod = null;
    private List sequence = null;
    private double bearerAmplitude;
    private double bearerFrequency0, bearerFrequency1;

    List<ModulatorSignal> modulatedSequence = new ArrayList<ModulatorSignal>();

    /**
     * Creates modulator with given parameters
     * @param mod_type type of using modulation
     * @param bearerAmplitude bearer amplitude, V
     * @param bearerFrequency0 first bearer frequency, Hz
     * @param bearerFrequency1 second bearer frequency, Hz
     * @param symbols list of input symbols (input videosequence)
     */
    public Modulator(ModulationType mod_type, double bearerAmplitude, double bearerFrequency0, double bearerFrequency1, List symbols)
    {
	this.usingMethod = mod_type;
	this.sequence = symbols;
	this.bearerAmplitude = bearerAmplitude;
	this.bearerFrequency0 = bearerFrequency0;
	this.bearerFrequency1 = bearerFrequency1;
    }

    /**
     * Runs modulating
     */
    public void doModulation()
    {
	this.modulatedSequence.clear();

	int previousPhase = 1;

	ModulatorSignal amn0 = new ModulatorSignal(0, 0, 0, 0, 1 / bearerFrequency1);
	ModulatorSignal amn1 = new ModulatorSignal(bearerFrequency1, bearerAmplitude, 0, 0, 1 / bearerFrequency1);
	ModulatorSignal fmn0 = new ModulatorSignal(bearerFrequency0, bearerAmplitude, 0, 0, 1 / bearerFrequency0);
	ModulatorSignal fmn1 = new ModulatorSignal(bearerFrequency1, bearerAmplitude, 0, 0, 1 / bearerFrequency0);
	ModulatorSignal pmn0 = new ModulatorSignal(bearerFrequency1, bearerAmplitude, 0, 0, 1 / bearerFrequency1);
	ModulatorSignal pmn1 = new ModulatorSignal(bearerFrequency1, bearerAmplitude, -Math.PI, 0, 1 / bearerFrequency1);

	for (int j = 0; j < this.sequence.size(); j++)
	{
	    BinaryNumber workingNumber = (BinaryNumber)this.sequence.get(j);
	    boolean[] seq = workingNumber.getAlignedBinaryArray();

	    for (int i = 0; i < seq.length; i++)
	    {
		switch (this.usingMethod)
		{
		    case ASK:
			if (!seq[i])
			    this.modulatedSequence.add(amn0);
			else
			    this.modulatedSequence.add(amn1);
			break;
		    case FSK:
			if (!seq[i])
			    this.modulatedSequence.add(fmn0);
			else
			    this.modulatedSequence.add(fmn1);
			break;
		    case PSK:
			if (!seq[i])
			    this.modulatedSequence.add(pmn0);
			else
			    this.modulatedSequence.add(pmn1);
			break;
		    case RPSK:
			if (!seq[i])
			{
			    if (previousPhase == 1)
			    {
				this.modulatedSequence.add(pmn0);
				previousPhase = 1;
			    } else
			    if (previousPhase == -1)
			    {
				this.modulatedSequence.add(pmn1);
				previousPhase = -1;
			    }
			} else
			{
			    if (previousPhase == 1)
			    {
				this.modulatedSequence.add(pmn1);
				previousPhase = -1;
			    } else
			    if (previousPhase == -1)
			    {
				this.modulatedSequence.add(pmn0);
				previousPhase = 1;
			    }
			}
			break;
		    default:
			break;
		}
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
