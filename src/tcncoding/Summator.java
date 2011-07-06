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
 * @author Oleksandr Natalenko aka post-factum
 */
public class Summator
{
    private List<DigitalSignal> sequence0;
    private List<DigitalSignal> sequence1;

    /**
     * Creates summator of two digital functions
     * @param _sequence0 first digital function
     * @param _sequence1 second digital function
     */
    public Summator(List<DigitalSignal> _sequence0, List<DigitalSignal> _sequence1)
    {
	sequence0 = _sequence0;
	sequence1 = _sequence1;
    }

    /**
     * Runs summing
     * @return summed sequence
     */
    public List<DigitalSignal> getSum()
    {
	List<DigitalSignal> result = new ArrayList<DigitalSignal>();

        //creates workers map, each worker gets its amount of pieces to process
        int workers = Runtime.getRuntime().availableProcessors();
        int tickets = sequence0.size();
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

        //create signals map according to workers map (sequence0)
        List<List<DigitalSignal>> newSignals0 = new ArrayList<List<DigitalSignal>>();
        int index = 0;
        for (Integer ci: workersMap)
        {
            List<DigitalSignal> newBlock = new ArrayList<DigitalSignal>();
            for (int i = index; i < index + ci; i++)
                newBlock.add(sequence0.get(i));
            newSignals0.add(newBlock);
            index += ci;
        }

        //create signals map according to workers map (sequence1)
        List<List<DigitalSignal>> newSignals1 = new ArrayList<List<DigitalSignal>>();
        index = 0;
        for (Integer ci: workersMap)
        {
            List<DigitalSignal> newBlock = new ArrayList<DigitalSignal>();
            for (int i = index; i < index + ci; i++)
                newBlock.add(sequence1.get(i));
            newSignals1.add(newBlock);
            index += ci;
        }

        //creates workers stack
        List<Future<List<DigitalSignal>>> workersStack = new ArrayList<Future<List<DigitalSignal>>>();
        ExecutorService es = Executors.newFixedThreadPool(workers);
        for (int i = 0; i < newSignals0.size(); i++)
            workersStack.add(es.submit(new SummatorWorker(newSignals0.get(i), newSignals1.get(i))));


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
        for (List<DigitalSignal> clms: rawResult)
            for (DigitalSignal cms: clms)
                result.add(cms);
        return result;
    }
}
