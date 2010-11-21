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

import java.util.List;

/**
 * Common class to control source decoders
 * @author post-factum
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
     */
    public void doDecode()
    {
	switch (usingCode)
	{
	    case MTK2:
		SourceDecoderITC2 decoderMTK2 = new SourceDecoderITC2(sourceSequence);
		decoderMTK2.doDecoding();
		sourceMessage = decoderMTK2.getMessage();
		break;
	    case MTK5:
		SourceDecoderITC5 decoderMTK5 = new SourceDecoderITC5(sourceSequence);
		decoderMTK5.doDecoding();
		sourceMessage = decoderMTK5.getMessage();
		break;
	    case KOI8U:
		SourceDecoderKOI8U decoderKOI8U = new SourceDecoderKOI8U(sourceSequence);
		decoderKOI8U.doDecoding();
		sourceMessage = decoderKOI8U.getMessage();
		break;
	    case MORSE:
		SourceDecoderMorse decoderMorse = new SourceDecoderMorse(sourceSequence, isCyr);
		decoderMorse.doDecoding();
		sourceMessage = decoderMorse.getMessage();
		break;
	    case SHANNON:
		SourceDecoderShannonFano decoderShannon = new SourceDecoderShannonFano(sourceSequence, isCyr);
		decoderShannon.doDecoding();
		sourceMessage = decoderShannon.getMessage();
		break;
	    default:
		break;
	}
    }

    /**
     * Returns decoded messaage
     * @return string representation of source message
     */
    public String getMessage()
    {
	return sourceMessage;
    }

}
