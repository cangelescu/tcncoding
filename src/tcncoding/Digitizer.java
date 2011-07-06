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

    private List<AnalogSignal> inputSignals;
    private double step;
    
    /**
     * Converts analog signal into digital
     * @param _inputSignals list of input signals
     * @param _step discretizing step
     */
    public Digitizer(Object _inputSignals, double _step)
    {
        inputSignals = (List<AnalogSignal>)_inputSignals;
        step = _step;
    }
    
    /**
     * Runs signal processing
     * @return list of digital signals
     */
    public List<DigitalSignal> getDigitalSignal()
    {
        List<DigitalSignal> outputSignals = new ArrayList<DigitalSignal>();
        for (AnalogSignal cas: inputSignals)
        {
            List<Sample> newSample = new ArrayList<Sample>();
            for (double currentPoint = cas.getStart(); currentPoint <= cas.getEnd(); currentPoint += step)
                newSample.add(new Sample(currentPoint, cas.function(currentPoint)));
            outputSignals.add(new DigitalSignal(newSample));
        }
        return outputSignals;
    }
}
