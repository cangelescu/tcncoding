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

import java.util.List;

/**
 * Common class to control source decoders
 * @author Oleksandr Natalenko aka post-factum
 */
public class SourceDecoderController
{

    private String sourceMessage = "";
    private List<BinaryNumber> sourceSequence;
    private boolean isCyr;
    private SourceCoderController.SourceCoderCode usingCode;

    /**
     * Creates source decoder for input source sequence
     * @param _sourceSequence binary sequence to decode
     * @param _codeType type of using code
     * @param _isCyr indicates if to use cyrillic table
     */
    public SourceDecoderController(List<BinaryNumber> _sourceSequence, SourceCoderController.SourceCoderCode _codeType, boolean _isCyr)
    {
	sourceSequence = _sourceSequence;
	usingCode = _codeType;
	isCyr = _isCyr;
    }

    /**
     * Runs decoding
     * @return decoded sequence
     */
    public String getMessage()
    {
        SourceDecoder sourceDecoder = null;
	switch (usingCode)
	{
	    case MTK2:
		sourceDecoder = new SourceDecoderITC2(sourceSequence);
		break;
	    case MTK5:
		sourceDecoder = new SourceDecoderITC5(sourceSequence);
		break;
	    case KOI8U:
		sourceDecoder = new SourceDecoderKOI8U(sourceSequence);
		break;
	    case MORSE:
		sourceDecoder = new SourceDecoderMorse(sourceSequence, isCyr);
		break;
	    case SHANNON:
		sourceDecoder = new SourceDecoderShannonFano(sourceSequence, isCyr);
		break;
	    default:
		break;
	}
        sourceMessage = sourceDecoder.getMessage();
        return sourceMessage;
    }
}
