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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Model of summing device
 * @author post-factum
 */
public class Summator
{
    private List<List<DigitalSignal>> sequence0;
    private List<List<DigitalSignal>> sequence1;
    private List<List<DigitalSignal>> sumResult = new ArrayList<List<DigitalSignal>>();

    /**
     * Creates summator of two digital functions
     * @param _sequence0 first digital function
     * @param _sequence1 second digital function
     */
    public Summator(List<List<DigitalSignal>> _sequence0, List<List<DigitalSignal>> _sequence1)
    {
	sequence0 = _sequence0;
	sequence1 = _sequence1;
    }

    /**
     * Runs summing
     */
    public void doSumming()
    {
	sumResult.clear();

        //creates workers stack
        List<Future<List<DigitalSignal>>> workersStack = new ArrayList<Future<List<DigitalSignal>>>();
        ExecutorService es = Executors.newFixedThreadPool(sequence0.size());
        for (int i = 0; i < sequence0.size(); i++)
            workersStack.add(es.submit(new SummatorWorker(sequence0.get(i), sequence1.get(i))));


        //retrieves results
	for (Future<List<DigitalSignal>> cflds: workersStack)
        {
            try {
                sumResult.add(cflds.get());
            } catch (Exception ex) {
                System.err.println(ex.getLocalizedMessage());
            }
        }
        es.shutdown();
    }

    /**
     * Returns digital function
     * @return digital function
     */
    public List<List<DigitalSignal>> getSum()
    {
	return sumResult;
    }
}
