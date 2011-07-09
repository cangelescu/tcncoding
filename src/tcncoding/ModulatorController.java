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
 * Common class to control modulators
 * @author Oleksandr Natalenko aka post-factum
 */
public class ModulatorController
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

    private ModulationType usingMethod;
    private List<BinaryNumber> inputSequence;
    private double bearerAmplitude, bearerFrequency, bearerFrequencyDeviation, impulseLength;

    private List<ModulatorSignal> modulatedSequence = new ArrayList<ModulatorSignal>();

    /**
     * Creates modulator with given parameters
     * @param _modulationType type of using modulation
     * @param _bearerAmplitude bearer amplitude, V
     * @param _bearerFrequency first bearer frequency, Hz
     * @param _bearerFrequencyDeviation second bearer frequency, Hz
     * @param _inputSequence list of input symbols (input videosequence)
     * @param _impulseLength length of channel impulse
     */
    public ModulatorController(ModulationType _modulationType, double _bearerAmplitude, double _bearerFrequency, double _bearerFrequencyDeviation, List<BinaryNumber> _inputSequence, double _impulseLength)
    {
	usingMethod = _modulationType;
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
    public List<ModulatorSignal> getSignals()
    {
	switch (usingMethod)
	{
	    case ASK:
		ModulatorASK modulatorASK = new ModulatorASK(bearerAmplitude, bearerFrequency, inputSequence, impulseLength);
		modulatedSequence = modulatorASK.getSignals();
		break;
	    case FSK:
		ModulatorFSK modulatorFSK = new ModulatorFSK(bearerAmplitude, bearerFrequency, bearerFrequencyDeviation, inputSequence, impulseLength);
		modulatedSequence = modulatorFSK.getSignals();
		break;
	    case PSK:
		ModulatorPSK modulatorPSK = new ModulatorPSK(bearerAmplitude, bearerFrequency, inputSequence, impulseLength);
		modulatedSequence = modulatorPSK.getSignals();
		break;
	    case RPSK:
		//recode input sequence first
		ModulatorRPSKRecoder recoder = new ModulatorRPSKRecoder(inputSequence);
		List<BinaryNumber> recodedsequence = recoder.getEncodedList();
		//using PSK modulator with recoded sequence as its input
		ModulatorPSK modulatorRPSK = new ModulatorPSK(bearerAmplitude, bearerFrequency, recodedsequence, impulseLength);
		modulatedSequence = modulatorRPSK.getSignals();
		break;
	    default:
		break;
	}
        return modulatedSequence;
    }
}
