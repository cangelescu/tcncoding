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
import java.util.Random;

/**
 *
 * @author post-factum
 */
public class ErrorsInjector {

    private List<BinaryNumber> inputSequence;
    private List<BinaryNumber> outputSequence = new ArrayList<BinaryNumber>();
    private int errorsCount;
    private boolean perBlock;

    public ErrorsInjector(List<BinaryNumber> _inputSequence, int _errorsCount, boolean _perBlock)
    {
	inputSequence = _inputSequence;
	errorsCount = _errorsCount;
	perBlock = _perBlock;
    }

    public void injectErrors()
    {
	//acts only if count of errors is greater than zero
	if (errorsCount > 0)
	{
	    Random randomizer = new Random();
	    if (perBlock)
	    {
		for (int i = 0; i < inputSequence.size(); i++)
		{
		    List<Integer> positions = new ArrayList<Integer>();
		    for (int j = 0; j < errorsCount; j++)
		    {
			int injectionPosition;
			boolean ok;
			do
			{
			    injectionPosition = (int)Math.round(randomizer.nextDouble() * (inputSequence.get(i).getLength() - 1));
			    ok = true;
			    for (int ci: positions)
				if (ci == injectionPosition)
				    ok = false;
			}
			while (!ok);
			positions.add(injectionPosition);
		    }

		    boolean[] injectedBlock = inputSequence.get(i).getBinaryArray();
		    for (int ci: positions)
		    {
			boolean bit = injectedBlock[ci];
			injectedBlock[ci] = !bit;
		    }

		    outputSequence.add(new BinaryNumber(injectedBlock));
		}
	    } else
	    {
		List<List<Integer>> positions = new ArrayList<List<Integer>>();
		for (int i = 0; i < errorsCount; i++)
		{
		    int injectionBlock, injectionPosition;
		    boolean ok;
		    do
		    {
			injectionBlock = (int)Math.round(randomizer.nextDouble() * (inputSequence.size() - 1));
			injectionPosition = (int)Math.round(randomizer.nextDouble() * (inputSequence.get(injectionBlock).getLength() - 1));
			ok = true;
			for (List<Integer> cli: positions)
			    if (cli.get(0) == injectionBlock && cli.get(1) == injectionPosition)
				ok = false;
		    }
		    while (!ok);
		    List<Integer> injectionPair = new ArrayList<Integer>();
		    injectionPair.add(injectionBlock);
		    injectionPair.add(injectionPosition);
		    positions.add(injectionPair);
		}
		for (BinaryNumber cbn: inputSequence)
		    outputSequence.add(cbn);
		for (List<Integer> cli: positions)
		{
		    boolean[] injectedBlock = outputSequence.get(cli.get(0)).getBinaryArray();
		    boolean bit = injectedBlock[cli.get(1)];
		    injectedBlock[cli.get(1)] = !bit;
		    outputSequence.set(cli.get(0), new BinaryNumber(injectedBlock));
		}
	    }
	} else
	{
	    for (BinaryNumber cbn: inputSequence)
		outputSequence.add(cbn);
	}
    }

    public List<BinaryNumber> getSequence()
    {
	return outputSequence;
    }
}
