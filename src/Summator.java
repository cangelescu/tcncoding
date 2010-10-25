/*

 Copyright (C) 2009-2010 Oleksandr Natalenko aka post-factum

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

import java.util.ArrayList;
import java.util.List;

/**
 * Model of summing device
 * @author post-factum
 */
public class Summator
{
    private List<List<List<FunctionStep>>> sequence0;
    private List<List<List<FunctionStep>>> sequence1;
    private List<List<List<FunctionStep>>> sumResult = new ArrayList<List<List<FunctionStep>>>();

    /**
     * Creates summator of two tabulated functions
     * @param _sequence0 first tabulated function
     * @param _sequence1 second tabulated function
     */
    public Summator(List<List<List<FunctionStep>>> _sequence0, List<List<List<FunctionStep>>> _sequence1)
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
	for (int i = 0; i < sequence0.size(); i++)
	{
	    List<List<FunctionStep>> currentBlock0 = sequence0.get(i);
	    List<List<FunctionStep>> currentBlock1 = sequence1.get(i);
	    List<List<FunctionStep>> newBlock = new ArrayList<List<FunctionStep>>();
	    for (int j = 0; j < currentBlock0.size(); j++)
	    {
		List<FunctionStep> currentSymbol0 = currentBlock0.get(j);
		List<FunctionStep> currentSymbol1 = currentBlock1.get(j);
		List<FunctionStep> newSymbol = new ArrayList<FunctionStep>();
		for (int k = 0; k < currentSymbol0.size(); k++)
		{
		    FunctionStep currentStep0 = currentSymbol0.get(k);
		    FunctionStep currentStep1 = currentSymbol1.get(k);
		    newSymbol.add(new FunctionStep(currentStep1.getX(), currentStep1.getY() - currentStep0.getY()));
		}
		newBlock.add(newSymbol);
	    }
	    sumResult.add(newBlock);
	}
    }

    /**
     * Returns tabulated function
     * @return tabulated function
     */
    public List<List<List<FunctionStep>>> getSum()
    {
	return sumResult;
    }
}
