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

package tcncoding;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class of source coders
 * @author post-factum
 */
public class SourceCoder {

    protected String message;
    protected List<BinaryNumber> sourceSequence = new ArrayList<BinaryNumber>();

    /**
     * Returns encoded sequence
     * @return list of binary numbers that represents source message
     */
    public List<BinaryNumber> getSequence()
    {
	return sourceSequence;
    }
}
