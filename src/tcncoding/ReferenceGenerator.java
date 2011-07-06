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
 * Models reference generator of correlative receiver
 * @author Oleksandr Natalenko aka post-factum
 */
public class ReferenceGenerator
{
    private double referenceFrequency, referenceAmplitude, referencePhase;
    private List<ChannelSignal> receivedSignals;

    /**
     * Creates reference generator
     * @param _frequency reference frequency, Hz
     * @param _amplitude reference amplitude, V
     * @param _phase reference phase, rad
     * @param _signals list of input signals
     */
    public ReferenceGenerator(double _frequency, double _amplitude, double _phase, List<ChannelSignal> _signals)
    {
	referenceFrequency = _frequency;
	referenceAmplitude = _amplitude;
	referencePhase = _phase;
	receivedSignals = _signals;
    }

    /**
     * Runs generating
     * @return list of reference signals
     */
    public List<ModulatorSignal> getSignals()
    {
        List<ModulatorSignal> output = new ArrayList<ModulatorSignal>();
        for (ChannelSignal ccs: receivedSignals)
            output.add(new ModulatorSignal(referenceFrequency, referenceAmplitude, referencePhase, ccs.getStart(), ccs.getEnd()));
        return output;
    }
}
