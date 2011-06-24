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
 * Converts analog signal to digital
 * @author Oleksandr Natalenko aka post-factum
 */
public class Digitizer {

    private List<List<MultiplierSignal>> inputMultiplierSignals;
    private DataVizualizatorProvider.SignalType signalType;
    private double step;
    
    public Digitizer(List<List<MultiplierSignal>> _inputMultiplierSignals, DataVizualizatorProvider.SignalType _signalType, double _step)
    {
        inputMultiplierSignals = _inputMultiplierSignals;
        signalType = _signalType;
        step = _step;
    }
    
    public List<List<DigitalSignal>> getDigitalSignal()
    {
        List<List<DigitalSignal>> outputSignals = new ArrayList<List<DigitalSignal>>();
        switch (signalType)
        {
            case MULTIPLIER:
                for (List<MultiplierSignal> clms: inputMultiplierSignals)
                {
                    List<DigitalSignal> clds = new ArrayList<DigitalSignal>();
                    for (MultiplierSignal cms: clms)
                    {
                        List<Sample> newSample = new ArrayList<Sample>();
                        for (double currentPoint = cms.getStart(); currentPoint <= cms.getEnd(); currentPoint += step)
                            newSample.add(new Sample(currentPoint, cms.function(currentPoint)));
                        clds.add(new DigitalSignal(newSample));
                    }
                    outputSignals.add(clds);
                }
                break;
            default:
                break;
        }
        return outputSignals;
    }
}
