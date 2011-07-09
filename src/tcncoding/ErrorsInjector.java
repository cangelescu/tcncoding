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

    private List<BinaryNumber> inputSequence;
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
     * @return sequence with injected errors
     */
    public List<BinaryNumber> getSequence()
    {
        List<BinaryNumber> outputSequence = new ArrayList<BinaryNumber>();
        //copies input sequence into output
        for (BinaryNumber cb: inputSequence)
            outputSequence.add(cb);
        if (errorsCount > 0)
        {
            Random randomizer = new Random();
            List<ErrorDescriptor> positions = new ArrayList<ErrorDescriptor>();
            //injects errors into WHOLE sequence
            if (!perBlock)
            {   
                //creates unique error positions list
                for (int i = 0; i < errorsCount; i++)
                {
                    int blockPosition, bitPosition;
                    boolean found = false;
                    do {
                        blockPosition = (int)(Math.round(randomizer.nextDouble() * (inputSequence.size() - 1)));
                        bitPosition = (int)(Math.round(randomizer.nextDouble() * (inputSequence.get(blockPosition).getBinaryArray().length - 1)));
                        for (ErrorDescriptor cp: positions)
                            if (cp.getBlock() == blockPosition && cp.getBit() == bitPosition)
                                found = true;
                    } while (found);
                    positions.add(new ErrorDescriptor(blockPosition, bitPosition));
                }
            } else
            //inject errors into each block
            {
                //creates unique error positions list
                for (int k = 0; k < outputSequence.size(); k++)
                {
                    for (int i = 0; i < errorsCount; i++)
                    {
                        int bitPosition;
                        boolean found = false;
                        do {
                            bitPosition = (int)(Math.round(randomizer.nextDouble() * (inputSequence.get(k).getBinaryArray().length - 1)));
                            for (ErrorDescriptor cp: positions)
                                if (cp.getBlock() == k && cp.getBit() == bitPosition)
                                    found = true;
                        } while (found);
                        positions.add(new ErrorDescriptor(k, bitPosition));
                    }
                }
            }

            //injects errors
            for (ErrorDescriptor cbd: positions)
            {
                int block = cbd.getBlock();
                int bit = cbd.getBit();
                boolean[] bits = outputSequence.get(block).getBinaryArray();
                bits[bit] = !bits[bit];
                outputSequence.set(block, new BinaryNumber(bits));
            }
        }
        return outputSequence;
    }
}
