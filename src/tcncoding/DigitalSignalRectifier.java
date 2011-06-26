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
 * Converts structured digital sequence to flat list of samples
 * @author Oleksandr Natalenko aka post-factum
 */
public class DigitalSignalRectifier {
    private List<List<DigitalSignal>> inputSignal;
    
    /**
     * Creates convertor
     * @param _inputSignal input list of digital signals
     */
    public DigitalSignalRectifier(List<List<DigitalSignal>> _inputSignal)
    {
        inputSignal = _inputSignal;
    }
    
    /**
     * Runs signals list processing
     * @return flat list of samples in one class
     */
    public DigitalSignal getSignal()
    {
        List<Sample> newSignal = new ArrayList<Sample>();
        for (List<DigitalSignal> clds: inputSignal)
            for (DigitalSignal cds: clds)
                for (Sample cs: cds.getSamples())
                    newSignal.add(cs);
        return new DigitalSignal(newSignal);
    }
}
