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
 *
 * @author Oleksandr Natalenko aka post-factum
 */
public class BitsRectifier {
    private List<BinaryNumber> sequence;
    
    /**
     * Rectifies bits sequence into one boolean array
     * @param _sequence input list of binary numbers
     */
    public BitsRectifier(List<BinaryNumber> _sequence)
    {
        sequence = _sequence;
    }
    
    /**
     * Runs rectifying
     * @return flat boolean array
     */
    public List<Boolean> getBits()
    {
        List<Boolean> linearSequence = new ArrayList<Boolean>();
	//forms linear bit array
	for (BinaryNumber bn: sequence)
            for (int i = 0; i < bn.getLength(); i++)
                linearSequence.add(bn.getDigit(i));
        return linearSequence;
    }
}
