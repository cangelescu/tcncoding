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
 *
 * @author post-factum
 */
public class SourceDecoderShannon
{

    private String sourceMessage;
    private HashMap<String, String> codeMapShannon = new HashMap<String, String>();
    private List<BinaryNumber> sourceSequence = new ArrayList<BinaryNumber>();

    /**
     * Creates Shannon source decoder
     * @param _sourceSequence
     */
    public SourceDecoderShannon(List<BinaryNumber> _sourceSequence)
    {
	sourceSequence = _sourceSequence;

	SourceDecoderCodeMapLoader loader = new SourceDecoderCodeMapLoader("shannon");
	codeMapShannon = loader.getCodeMap();
    }

    /**
     * Decodes source message with Shannon code
     */
    public void doDecoding()
    {
	sourceMessage = "";

	for (BinaryNumber cbn: sourceSequence)
	{
	    String currentChar = codeMapShannon.get(cbn.getStringSequence());
	    if (currentChar != null)
		sourceMessage += currentChar;
	    else
		sourceMessage += "*";
	}
    }

    /**
     * Returns decoded message
     * @return
     */
    public String getMessage()
    {
	return sourceMessage;
    }
}
