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
import java.util.concurrent.Callable;

/**
 * Thread worker of summator
 * @author Oleksandr Natalenko aka post-factum
 */
public class SummatorWorker implements Callable<List<DigitalSignal>>{

    private List<DigitalSignal> block0, block1;

    /**
     * Creates summator worker
     * @param _block0 list of first digital signals
     * @param _block1 list of second digital signals
     */
    public SummatorWorker(List<DigitalSignal> _block0, List<DigitalSignal> _block1)
    {
        block0 = _block0;
        block1 = _block1;
    }

    public List<DigitalSignal> call()
    {
        List<DigitalSignal> result = new ArrayList<DigitalSignal>();

        for (int j = 0; j < block0.size(); j++)
        {
            DigitalSignal currentSymbol0 = block0.get(j);
            DigitalSignal currentSymbol1 = block1.get(j);
            List<Sample> newSymbol = new ArrayList<Sample>();
            for (int k = 0; k < currentSymbol0.getSamplesCount(); k++)
            {
                Sample currentSample0 = currentSymbol0.getSample(k);
                Sample currentSample1 = currentSymbol1.getSample(k);
                newSymbol.add(new Sample(currentSample1.getX(), currentSample1.getY() - currentSample0.getY()));
            }
            result.add(new DigitalSignal(newSymbol));
        }

        return result;
    }
}
