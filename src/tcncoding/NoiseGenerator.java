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
 * @author post-factum
 */
public class NoiseGenerator
{

    private double noisePower;
    private List<List<ModulatorSignal>> modulatedSequence;

    private List<List<NoiseSignal>> noiseSignal = new ArrayList<List<NoiseSignal>>();

    /**
     * Creates noise generator
     * @param _modulatedSequence reference modulator signals to use for timing
     * @param _noisePower power of noise, W
     */
    public NoiseGenerator(List<List<ModulatorSignal>> _modulatedSequence, double _noisePower)
    {
	modulatedSequence = _modulatedSequence;
        noisePower = _noisePower;
    }

    /**
     * Generates noise
     */
    public void doGenerating()
    {
	for (List<ModulatorSignal> clms: modulatedSequence)
        {
            List<NoiseSignal> clns = new ArrayList<NoiseSignal>();
            for (ModulatorSignal cms: clms)
                clns.add(new NoiseSignal(noisePower, cms.getStart(), cms.getEnd()));
            noiseSignal.add(clns);
        }
    }

    /**
     * Returns noise signals
     * @return list of noise signals
     */
    public List<List<NoiseSignal>> getSignals()
    {
	return noiseSignal;
    }
}
