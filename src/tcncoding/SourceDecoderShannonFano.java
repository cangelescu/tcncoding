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

import java.util.HashMap;
import java.util.List;

/**
 * Allows using Shannon-Fano code
 * @author Oleksandr Natalenko aka post-factum
 */
public class SourceDecoderShannonFano extends SourceDecoder
{

    private HashMap<String, String> codeMap = new HashMap<String, String>();

    /**
     * Creates Shannon-Fano source decoder
     * @param _sourceSequence
     * @param _isCyr indicates if to use cyrillic table
     */
    public SourceDecoderShannonFano(List<BinaryNumber> _sourceSequence, boolean _isCyr)
    {
	sourceSequence = _sourceSequence;

	SourceDecoderCodeMapLoader loader;
	loader = new SourceDecoderCodeMapLoader(_isCyr ? "shannon_cyr" : "shannon_lat");
	codeMap = loader.getCodeMap();
    }

    /**
     * Decodes source message with Shannon-Fano code
     * @return decoded sequence
     */
    public String getMessage()
    {
	sourceMessage = "";

        BitsRectifier rectifier = new BitsRectifier(sourceSequence);
        List<Boolean> sequence = rectifier.getBits();

	//goes through sequence bit by bit
	String buffer = "";
	for (Boolean cb: sequence)
	{
	    buffer += cb ? "1" : "0";

	    String currentChar = codeMap.get(buffer);

	    if (currentChar != null)
	    {
		sourceMessage += currentChar;
		buffer = "";
	    } else
		continue;
	}

	//tries to recognize last symbol from buffer
	if (buffer.length() > 0)
	{
	    String currentChar = codeMap.get(buffer);
	    sourceMessage += (currentChar != null) ? currentChar : "*";
	}
        return sourceMessage;
    }
}
