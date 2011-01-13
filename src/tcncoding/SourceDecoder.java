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
 * Base class of source decoders
 * @author Oleksandr Natalenko aka post-factum
 */
public class SourceDecoder {

    /**
     * output decoded sequence
     */
    protected String sourceMessage;
    /**
     * input encoded sequence
     */
    protected List<BinaryNumber> sourceSequence = new ArrayList<BinaryNumber>();

    /**
     * Returns decoded message
     * @return string representation of source message
     */
    public String getMessage()
    {
	return sourceMessage;
    }
}
