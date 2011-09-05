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
 * Thread worker of integrator
 * @author Oleksandr Natalenko aka post-factum
 */
public class IntegratorWorker implements Callable<List<DigitalSignal>>{

    private List<DigitalSignal> digitalSignals;

    /**
     * Creates integrator worker
     * @param _digitalSignals list of input digital signals to integrate
     */
    public IntegratorWorker(List<DigitalSignal> _digitalSignals)
    {
        digitalSignals = _digitalSignals;
    }

    /**
     * 
     * @return
     */
    public List<DigitalSignal> call()
    {
        List<DigitalSignal> result = new ArrayList<DigitalSignal>();
       
        for (DigitalSignal cds: digitalSignals)
        {
            List<Sample> newSymbol = new ArrayList<Sample>();

            double sum = 0;
            for (double x = cds.getStart(); x <= cds.getEnd(); x += cds.getDelta())
            {
                sum += cds.getDelta() * cds.function(x);
                newSymbol.add(new Sample(x, sum));
            }

            DigitalSignal newDigitalSignal = new DigitalSignal(newSymbol);
            result.add(newDigitalSignal);
        }

        return result;
    }
}
