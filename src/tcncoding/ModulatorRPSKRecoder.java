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

/**
 * Model of RPSK recoder
 * @author Oleksandr Natalenko aka post-factum
 */
public class ModulatorRPSKRecoder
{
    private List<Boolean> sequence;

    /**
     * Rectifies binary numbers' list into array
     * @param _sequence source list of binary numbers
     */
    public ModulatorRPSKRecoder(List<Boolean> _sequence)
    {
	sequence = _sequence;
    }

    /**
     * Runs encoding
     * @return encoded sequence
     */
    public List<BinaryNumber> getEncodedList()
    {
        List<Boolean> outputArray = new ArrayList<Boolean>();
        outputArray.add(true);

	boolean previous = true;
	for (int i = 0; i < sequence.size(); i++)
	{
            boolean current = sequence.get(i) ^ previous;
            outputArray.add(current);
            previous = current;
	}

        List<BinaryNumber> outputList = new ArrayList<BinaryNumber>();
        for (Boolean cb: outputArray)
        {
            List<Boolean> clb = new ArrayList<Boolean>();
            clb.add(cb);
            outputList.add(new BinaryNumber(clb));
        }
        return outputList;
    }

    /**
     * Runs decoding
     * @return decoded sequence
     */
    public List<Boolean> getDecodedList()
    {
	List<Boolean> outputArray = new ArrayList<Boolean>();

	boolean previous = true;
	for (int i = 1; i < sequence.size(); i++)
	{
            outputArray.add(sequence.get(i) ^ previous);
            previous = sequence.get(i);
	}
        return outputArray;
    }
}
