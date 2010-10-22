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

    /**
     * Creates errors injector
     * @param _inputSequence list of binary symbols to inject to
     * @param _errorsCount count of errors to inject
     * @param _perBlock indicates whether to inject errors in each block or to the whole sequence
     */
    public ErrorsInjector(List<BinaryNumber> _inputSequence, int _errorsCount, boolean _perBlock)
    {
	inputSequence = _inputSequence;
	errorsCount = _errorsCount;
	perBlock = _perBlock;
    }

    /**
     * Injects errors in the sequence
     */
    public void injectErrors()
    {
	//acts only if count of errors is greater than zero
	if (errorsCount > 0)
	{
	    //using common randomizer for two cases
	    Random randomizer = new Random();
	    //injects errorsCount errors to EACH block
	    if (perBlock)
	    {
		//going through the whole sequence block-by-block
		for (int i = 0; i < inputSequence.size(); i++)
		{
		    //store each new position in order not to repeat them
		    List<Integer> positions = new ArrayList<Integer>();
		    //makes errorsCount injections
		    for (int j = 0; j < errorsCount; j++)
		    {
			int injectionPosition;
			boolean ok;
			//repeat several times in order not to repeat injection positions
			do
			{
			    //calculates new injection position
			    injectionPosition = (int)Math.round(randomizer.nextDouble() * (inputSequence.get(i).getLength() - 1));
			    //looks if this position already exists
			    ok = true;
			    for (int ci: positions)
				if (ci == injectionPosition)
				    ok = false;
			}
			while (!ok);
			//adds new injection position to the list
			positions.add(injectionPosition);
		    }

		    //injects errors to the current block
		    boolean[] injectedBlock = inputSequence.get(i).getBinaryArray();
		    for (int ci: positions)
		    {
			boolean bit = injectedBlock[ci];
			injectedBlock[ci] = !bit;
		    }

		    //adds injected block to output sequence
		    outputSequence.add(new BinaryNumber(injectedBlock));
		}
	    } else
	    //injects errorsCount errors to the whole sequence
	    {
		//store pairs [block, position] in order not to repeat them
		List<List<Integer>> positions = new ArrayList<List<Integer>>();
		//repeat the algorithm errorsCount times
		for (int i = 0; i < errorsCount; i++)
		{
		    int injectionBlock, injectionPosition;
		    boolean ok;
		    do
		    {
			//calculates [block, position] pair
			injectionBlock = (int)Math.round(randomizer.nextDouble() * (inputSequence.size() - 1));
			injectionPosition = (int)Math.round(randomizer.nextDouble() * (inputSequence.get(injectionBlock).getLength() - 1));
			//looks if this pair already exists in the storage
			ok = true;
			for (List<Integer> cli: positions)
			    if (cli.get(0) == injectionBlock && cli.get(1) == injectionPosition)
				ok = false;
		    }
		    while (!ok);
		    //adds [block, position] injection pair to storage
		    List<Integer> injectionPair = new ArrayList<Integer>();
		    injectionPair.add(injectionBlock);
		    injectionPair.add(injectionPosition);
		    positions.add(injectionPair);
		}
		//creates output sequence
		for (BinaryNumber cbn: inputSequence)
		    outputSequence.add(cbn);
		//modifies output sequence according to the list of injections
		for (List<Integer> cli: positions)
		{
		    //gets injection block (boolean array)
		    boolean[] injectedBlock = outputSequence.get(cli.get(0)).getBinaryArray();
		    //injects error
		    boolean bit = injectedBlock[cli.get(1)];
		    injectedBlock[cli.get(1)] = !bit;
		    //replace old block in output sequence with a new one
		    outputSequence.set(cli.get(0), new BinaryNumber(injectedBlock));
		}
	    }
	} else
	{
	    //if errorsCount is zero, copy inputSequence to output
	    for (BinaryNumber cbn: inputSequence)
		outputSequence.add(cbn);
	}
    }

    /**
     * Returns injected sequence
     * @return
     */
    public List<BinaryNumber> getSequence()
    {
	return outputSequence;
    }
}
