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
    private List<BinaryNumber> sequence;

    /**
     * Rectifies binary numbers' list into array
     * @param _sequence source list of binary numbers
     */
    public ModulatorRPSKRecoder(List<BinaryNumber> _sequence)
    {
	sequence = _sequence;
    }

    /**
     * Runs encoding
     * @return encoded sequence
     */
    public List<BinaryNumber> getEncodedList()
    {
        List<BinaryNumber> outputArray = new ArrayList<BinaryNumber>();
	outputArray.add(new BinaryNumber(1));

	int sequenceLength = sequence.size();
	boolean prev = true;
	for (int i = 0; i < sequenceLength; i++)
	{
	    int pieceLength = sequence.get(i).getLength();
	    boolean[] currentOldPiece = sequence.get(i).getBinaryArray();
	    boolean[] currentNewPiece = new boolean[pieceLength];
	    for (int j = 0; j < pieceLength; j++)
	    {
		currentNewPiece[j] = currentOldPiece[j] ^ prev;
		prev = currentNewPiece[j];
	    }
	    outputArray.add(new BinaryNumber(currentNewPiece));
	}
        return outputArray;
    }

    /**
     * Runs decoding
     * @return decoded sequence
     */
    public List<BinaryNumber> getDecodedList()
    {
	List<BinaryNumber> outputArray = new ArrayList<BinaryNumber>();

	int sequenceLength = sequence.size();
	boolean prev = true;
	for (int i = 1; i < sequenceLength; i++)
	{
	    int pieceLength = sequence.get(i).getLength();
	    boolean[] currentOldPiece = sequence.get(i).getBinaryArray();
	    boolean[] currentNewPiece = new boolean[pieceLength];
	    for (int j = 0; j < pieceLength; j++)
	    {
		currentNewPiece[j] = currentOldPiece[j] ^ prev;
		prev = currentOldPiece[j];
	    }
	    outputArray.add(new BinaryNumber(currentNewPiece));
	}
        return outputArray;
    }
}
