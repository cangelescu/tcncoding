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
import java.util.HashMap;
import java.util.List;

/**
 * Allows using KOI8-U
 * @author Oleksandr Natalenko aka post-factum
 */
public class SourceDecoderKOI8U implements SourceDecoder
{
    private String sourceMessage;
    private List<BinaryNumber> sourceSequence = new ArrayList<BinaryNumber>();
    private HashMap<String, String> codeMapKOI8U = new HashMap<String, String>();

    /**
     * Creates KOI8-U source decoder
     * @param _sourceSequence
     */
    public SourceDecoderKOI8U(List<BinaryNumber> _sourceSequence)
    {
	sourceSequence = _sourceSequence;

	SourceDecoderCodeMapLoader loader = new SourceDecoderCodeMapLoader("koi8u");
	codeMapKOI8U = loader.getCodeMap();
    }

    /**
     * Decodes source message with KOI8-U
     * @return decoded sequence
     */
    public String getMessage()
    {
	sourceMessage = "";

	for (BinaryNumber cbn: sourceSequence)
	{
	    String currentChar = codeMapKOI8U.get(cbn.getStringSequence());
	    sourceMessage += (currentChar != null) ? currentChar : "*";
	}
        return sourceMessage;
    }
}
