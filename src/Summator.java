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
 *
 * @author post-factum
 */
public class Summator
{
    private List<List<FunctionStep>> sequence0;
    private List<List<FunctionStep>> sequence1;
    private List<List<FunctionStep>> sumResult = new ArrayList<List<FunctionStep>>();

    /**
     * Creates summator of two tabulated functions
     * @param _sequence0 first tabulated function
     * @param _sequence1 second tabulated function
     */
    public Summator(List<List<FunctionStep>> _sequence0, List<List<FunctionStep>> _sequence1)
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
	    List<FunctionStep> s0 = sequence0.get(i);
	    List<FunctionStep> s1 = sequence1.get(i);
	    List<FunctionStep> newSum = new ArrayList<FunctionStep>();
	    for (int k = 0; k < s0.size(); k++)
	    {
		FunctionStep ss0 = s0.get(k);
		FunctionStep ss1 = s1.get(k);
		newSum.add(new FunctionStep(ss1.getX(), ss1.getY() - ss0.getY()));
	    }
	    sumResult.add(newSum);
	}
    }

    /**
     * Returns tabulated function
     * @return
     */
    public List<List<FunctionStep>> getSum()
    {
	return sumResult;
    }
}
