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
 * Allows using Morse code
 * @author Oleksandr Natalenko aka post-factum
 */
public class SourceDecoderMorse extends SourceDecoder
{

    private HashMap<String, String> codeMapMorse = new HashMap<String, String>();
    private HashMap<String, String> codeMapMorseNum = new HashMap<String, String>();

    /**
     * Creates Morse source decoder
     * @param _sourceSequence
     * @param _isCyr indicates whether to use cyrillic table
     */
    public SourceDecoderMorse(List<BinaryNumber> _sourceSequence, boolean _isCyr)
    {
	sourceSequence = _sourceSequence;

	SourceDecoderCodeMapLoader loader = new SourceDecoderCodeMapLoader(_isCyr ? "morse_cyr" : "morse_lat");
	SourceDecoderCodeMapLoader loaderNum = new SourceDecoderCodeMapLoader("morse_num");
	codeMapMorse = loader.getCodeMap();
	codeMapMorseNum = loaderNum.getCodeMap();
    }

    /**
     * Decodes source message with Morse code
     * @return decoded sequence
     */
    public String getMessage()
    {
	sourceMessage = "";

	for (BinaryNumber cbn: sourceSequence)
	{
	    String currentChar = codeMapMorse.get(cbn.getStringSequence());
	    if (currentChar != null)
		sourceMessage += currentChar;
	    else
	    {
		currentChar = codeMapMorseNum.get(cbn.getStringSequence());
		sourceMessage += (currentChar != null) ? currentChar : "*";
	    }
	}
        return sourceMessage;
    }
}
