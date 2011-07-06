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
import java.util.Random;

/**
 * Injects errors into binary sequence
 * @author Oleksandr Natalenko aka post-factum
 */
public class ErrorsInjector {

    private List<Boolean> inputSequence;
    private int errorsCount;
    private boolean perBlock;

    /**
     * Creates errors injector
     * @param _inputSequence list of binary symbols to inject to
     * @param _errorsCount count of errors to inject
     * @param _perBlock indicates whether to inject errors in each block or to the whole sequence
     */
    public ErrorsInjector(List<Boolean> _inputSequence, int _errorsCount, boolean _perBlock)
    {
	inputSequence = _inputSequence;
	errorsCount = _errorsCount;
	perBlock = _perBlock;
    }

    /**
     * Injects errors in the sequence
     * @return sequence with injected errors
     */
    public List<Boolean> getSequence()
    {
        if (errorsCount > 0)
        {
            List<Boolean> outputSequence = new ArrayList<Boolean>();
            Random randomizer = new Random();
            //TODO: implement per-block injection
            //if (!perBlock)
            //{
                //injects errors into WHOLE sequence
                //copies input sequence into output
                for (Boolean cb: inputSequence)
                    outputSequence.add(cb);
                //creates unique error positions list
                List<Integer> positions = new ArrayList<Integer>();
                for (int i = 0; i < errorsCount; i++)
                {
                    int position;
                    boolean found = false;
                    do {
                        position = (int)(Math.round(randomizer.nextDouble() * (inputSequence.size() - 1)));
                        for (Integer cp: positions)
                            if (cp == position)
                                found = true;
                    } while (found);
                    positions.add(position);
                }
                //injects errors
                for (Integer cp: positions)
                {
                    boolean currentValue = outputSequence.get(cp);
                    outputSequence.set(cp, !currentValue);
                }
                return outputSequence;
            //}
        } else
            return inputSequence;
    }
}
