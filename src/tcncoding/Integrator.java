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
 * Models integrator device
 * @author Oleksandr Natalenko aka post-factum
 */
public class Integrator
{
    private List<List<MultiplierSignal>> signals;
    private List<List<DigitalSignal>> result = new ArrayList<List<DigitalSignal>>();
    private double step;

    /**
     * Creates integrator for input signals
     * @param _signals list of input signals
     * @param _maxFrequency maximum frequency of a signal
     * @param _maxWidth maximum width of visible area
     */
    public Integrator(List<List<MultiplierSignal>> _signals, double _maxFrequency, double _maxWidth)
    {
	signals = _signals;

	int lastBlock = _signals.size() - 1;
	int lastSignal = _signals.get(lastBlock).size() - 1;
	double end = _signals.get(lastBlock).get(lastSignal).getEnd();

	//uses display width
	double step1 = end / _maxWidth;
	//Kotelnikov's teorem works here not in boundary case
	double step2 = 1 / (3 * _maxFrequency);

	//takes less step for more accuracy
	step = Math.min(step1, step2);
    }

    /**
     * Runs integrating
     * @return samples of integration function
     */
    public List<List<DigitalSignal>> getIntegrals()
    {
	result.clear();

        //creates signals map
        List<Integer> signalsMap = new ArrayList<Integer>();
        for (List<MultiplierSignal> clms: signals)
            signalsMap.add(clms.size());

        //creates flat list of signals
        List<MultiplierSignal> flatList = new ArrayList<MultiplierSignal>();
        for (List<MultiplierSignal> clms: signals)
            for (MultiplierSignal cms: clms)
                flatList.add(cms);

        //creates workers map, each worker gets its amount of pieces to process
        int workers = Runtime.getRuntime().availableProcessors();
        int tickets = flatList.size();
        int basePiece = tickets / workers;
        if (basePiece < 1)
            basePiece = 1;
        List<Integer> workersMap = new ArrayList<Integer>();
        for (int i = 0; i < workers - 1; i++)
        {
            workersMap.add(basePiece);
            tickets -= basePiece;
        }
        //but the last worker may get more
        workersMap.add(tickets);

        //create signals list according to workers map
        List<List<MultiplierSignal>> newSignals = new ArrayList<List<MultiplierSignal>>();
        int index = 0;
        for (Integer ci: workersMap)
        {
            List<MultiplierSignal> newBlock = new ArrayList<MultiplierSignal>();
            for (int i = index; i < index + ci; i++)
                newBlock.add(flatList.get(i));
            newSignals.add(newBlock);
            index += ci;
        }

        //creates workers stack
        List<Future<List<DigitalSignal>>> workersStack = new ArrayList<Future<List<DigitalSignal>>>();
        ExecutorService es = Executors.newFixedThreadPool(workers);

        //starts signals processing
        for (List<MultiplierSignal> clms: newSignals)
            workersStack.add(es.submit(new IntegratorWorker(clms, step)));

        //retrieves results
        List<List<DigitalSignal>> rawResult = new ArrayList<List<DigitalSignal>>();
	for (Future<List<DigitalSignal>> cflds: workersStack)
        {
            try {
                rawResult.add(cflds.get());
            } catch (Exception ex) {
                System.err.println(ex.getLocalizedMessage());
            }
        }
        es.shutdown();

        //creates flat list from raw results
        List<DigitalSignal> flatRawList = new ArrayList<DigitalSignal>();
        for (List<DigitalSignal> clms: rawResult)
            for (DigitalSignal cms: clms)
                flatRawList.add(cms);

        //repacks raw results according to signals map
        index = 0;
        for (Integer ci: signalsMap)
        {
            List<DigitalSignal> newBlock = new ArrayList<DigitalSignal>();
            for (int i = index; i < index + ci; i++)
                newBlock.add(flatRawList.get(i));
            index += ci;
            result.add(newBlock);
        }
        return result;
    }
}
