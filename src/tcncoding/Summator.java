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
    private List<List<DigitalSignal>> sequence0;
    private List<List<DigitalSignal>> sequence1;
    private List<List<DigitalSignal>> result = new ArrayList<List<DigitalSignal>>();

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
     * @return summed sequence
     */
    public List<List<DigitalSignal>> getSum()
    {
	result.clear();

        //creates signals map (sequence0)
        List<Integer> signalsMap0 = new ArrayList<Integer>();
        for (List<DigitalSignal> clms: sequence0)
            signalsMap0.add(clms.size());

        //creates flat list of signals (sequence0)
        List<DigitalSignal> flatList0 = new ArrayList<DigitalSignal>();
        for (List<DigitalSignal> clms: sequence0)
            for (DigitalSignal cms: clms)
                flatList0.add(cms);

        //creates workers map, each worker gets its amount of pieces to process
        int workers = Runtime.getRuntime().availableProcessors();
        int tickets = flatList0.size();
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
                newBlock.add(flatList0.get(i));
            newSignals0.add(newBlock);
            index += ci;
        }

        //creates signals map (sequence1)
        List<Integer> signalsMap1 = new ArrayList<Integer>();
        for (List<DigitalSignal> clms: sequence1)
            signalsMap1.add(clms.size());

        //creates flat list of signals (sequence1)
        List<DigitalSignal> flatList1 = new ArrayList<DigitalSignal>();
        for (List<DigitalSignal> clms: sequence1)
            for (DigitalSignal cms: clms)
                flatList1.add(cms);

        //create signals map according to workers map (sequence1)
        List<List<DigitalSignal>> newSignals1 = new ArrayList<List<DigitalSignal>>();
        index = 0;
        for (Integer ci: workersMap)
        {
            List<DigitalSignal> newBlock = new ArrayList<DigitalSignal>();
            for (int i = index; i < index + ci; i++)
                newBlock.add(flatList1.get(i));
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
        List<DigitalSignal> flatRawList = new ArrayList<DigitalSignal>();
        for (List<DigitalSignal> clms: rawResult)
            for (DigitalSignal cms: clms)
                flatRawList.add(cms);

        //repacks raw results according to signals map
        index = 0;
        for (Integer ci: signalsMap0)
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
