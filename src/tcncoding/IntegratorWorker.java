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

    private List<MultiplierSignal> multiplierSignals;
    private double step;

    /**
     * Creates integrator worker
     * @param _multiplierSignals list of input signals
     * @param _step integration step
     */
    public IntegratorWorker(List<MultiplierSignal> _multiplierSignals, double _step)
    {
        multiplierSignals = _multiplierSignals;
        step = _step;
    }

    public List<DigitalSignal> call()
    {
        List<DigitalSignal> result = new ArrayList<DigitalSignal>();

        for (MultiplierSignal cms: multiplierSignals)
        {
            List<Sample> newSymbol = new ArrayList<Sample>();

            //integrating using Simpson's rule
            double currentPoint = cms.getStart();
            double endPoint = cms.getEnd();
            double sum = 0;
            while (currentPoint <= endPoint)
            {
                double leftBorder = currentPoint;
                double rightBorder = leftBorder + step;
                double area = ((rightBorder - leftBorder) / 6) * (cms.function(leftBorder) + 4 * cms.function((leftBorder + rightBorder) / 2) + cms.function(rightBorder));
                newSymbol.add(new Sample(currentPoint, sum));
                sum += area;
                currentPoint += step;
            }

            DigitalSignal newDigitalSignal = new DigitalSignal(newSymbol);
            result.add(newDigitalSignal);
        }

        return result;
    }
}
