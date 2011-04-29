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
 * Allows using ITC-2
 * @author Oleksandr Natalenko aka post-factum
 */
public class SourceDecoderITC5 extends SourceDecoder
{

    private HashMap<String, String> codeMapMTK5 = new HashMap<String, String>();

    /**
     * Creates ITC-5 source decoder
     * @param _sourceSequence
     */
    public SourceDecoderITC5(List<BinaryNumber> _sourceSequence)
    {
	sourceSequence = _sourceSequence;

	SourceDecoderCodeMapLoader loader = new SourceDecoderCodeMapLoader("mtk5");
	codeMapMTK5 = loader.getCodeMap();
    }

    /**
     * Decodes source message with ITC-5
     * @return decoded sequence
     */
    public String getMessage()
    {
	sourceMessage = "";

	for (BinaryNumber cbn: sourceSequence)
	{
	    String currentChar = codeMapMTK5.get(cbn.getStringSequence());
	    sourceMessage += (currentChar != null) ? currentChar : "*";
	}
        return sourceMessage;
    }
}
