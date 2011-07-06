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
 * Noise generator routine
 * @author Oleksandr Natalenko aka post-factum
 */
public class NoiseGenerator
{

    private double noisePower;
    private List<ModulatorSignal> modulatedSequence;

    /**
     * Creates noise generator
     * @param _modulatedSequence reference modulator signals to use for timing
     * @param _noisePower power of noise, W
     */
    public NoiseGenerator(List<ModulatorSignal> _modulatedSequence, double _noisePower)
    {
	modulatedSequence = _modulatedSequence;
        noisePower = _noisePower;
    }

    /**
     * Generates noise
     * @return list of noise signals
     */
    public List<NoiseSignal> getSignals()
    {
        List<NoiseSignal> noiseSignal = new ArrayList<NoiseSignal>();
	for (ModulatorSignal cms: modulatedSequence)
            noiseSignal.add(new NoiseSignal(noisePower, cms.getStart(), cms.getEnd()));
        return noiseSignal;
    }
}
