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
    private double bearerAmplitude, bearerFrequency, bearerFrequencyDeviation, impulseLength;

    List<ModulatorSignal> modulatedSequence = new ArrayList<ModulatorSignal>();

    /**
     * Creates modulator with given parameters
     * @param mod_type type of using modulation
     * @param bearerAmplitude bearer amplitude, V
     * @param bearerFrequency first bearer frequency, Hz
     * @param bearerFrequencyDeviation second bearer frequency, Hz
     * @param symbols list of input symbols (input videosequence)
     */
    public Modulator(ModulationType mod_type, double bearerAmplitude, double bearerFrequency, double bearerFrequencyDeviation, List symbols, double _impulseLength)
    {
	this.usingMethod = mod_type;
	this.sequence = symbols;
	this.bearerAmplitude = bearerAmplitude;
	this.bearerFrequency = bearerFrequency;
	this.bearerFrequencyDeviation = bearerFrequencyDeviation;
	this.impulseLength = _impulseLength;
    }

    /**
     * Runs modulating
     */
    public void doModulation()
    {
	switch (this.usingMethod)
	{
	    case ASK:
		ModulatorASK modulatorASK = new ModulatorASK(bearerAmplitude, bearerFrequency, sequence, impulseLength);
		modulatorASK.doModulation();
		this.modulatedSequence = modulatorASK.getSignals();
		break;
	    case FSK:
		ModulatorFSK modulatorFSK = new ModulatorFSK(bearerAmplitude, bearerFrequency, bearerFrequencyDeviation, sequence, impulseLength);
		modulatorFSK.doModulation();
		this.modulatedSequence = modulatorFSK.getSignals();
		break;
	    case PSK:
		ModulatorPSK modulatorPSK = new ModulatorPSK(bearerAmplitude, bearerFrequency, sequence, impulseLength);
		modulatorPSK.doModulation();
		this.modulatedSequence = modulatorPSK.getSignals();
		break;
	    case RPSK:
		ModulatorRPSK modulatorRPSK = new ModulatorRPSK(bearerAmplitude, bearerFrequency, sequence, impulseLength);
		modulatorRPSK.doModulation();
		this.modulatedSequence = modulatorRPSK.getSignals();
		break;
	    default:
		break;
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
