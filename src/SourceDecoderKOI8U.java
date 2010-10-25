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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Allows using KOI8-U
 * @author post-factum
 */
public class SourceDecoderKOI8U
{

    private String sourceMessage;
    private HashMap<String, String> codeMapKOI8U = new HashMap<String, String>();
    private List<BinaryNumber> sourceSequence = new ArrayList<BinaryNumber>();

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
     */
    public void doDecoding()
    {
	sourceMessage = "";

	for (BinaryNumber cbn: sourceSequence)
	{
	    String currentChar = codeMapKOI8U.get(cbn.getStringSequence());
	    if (currentChar != null)
		sourceMessage += currentChar;
	    else
		sourceMessage += "*";
	}
    }

    /**
     * Returns decoded message
     * @return string representation of source message
     */
    public String getMessage()
    {
	return sourceMessage;
    }
}
