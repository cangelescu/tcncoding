package tcncoding;

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
 * Allows using Shannon-Fano code
 * @author post-factum
 */
public class SourceDecoderShannonFano
{

    private String sourceMessage;
    private HashMap<String, String> codeMap = new HashMap<String, String>();
    private List<BinaryNumber> sourceSequence = new ArrayList<BinaryNumber>();

    /**
     * Creates Shannon-Fano source decoder
     * @param _sourceSequence
     * @param _isCyr indicates if to use cyrillic table
     */
    public SourceDecoderShannonFano(List<BinaryNumber> _sourceSequence, boolean _isCyr)
    {
	sourceSequence = _sourceSequence;

	SourceDecoderCodeMapLoader loader;
	if (_isCyr)
	    loader = new SourceDecoderCodeMapLoader("shannon_cyr");
	else
	    loader = new SourceDecoderCodeMapLoader("shannon_lat");
	codeMap = loader.getCodeMap();
    }

    /**
     * Decodes source message with Shannon-Fano code
     */
    public void doDecoding()
    {
	sourceMessage = "";

	//gets bits count
	int length = 0;
	for (BinaryNumber cbn: sourceSequence)
	    length += cbn.getLength();

	//creates map with 1 block
	List<Integer> unifiedMap = new ArrayList<Integer>();
	unifiedMap.add(length);

	//forms 1 binary number from source sequence
	Splitter splitter = new Splitter(sourceSequence, unifiedMap);
	splitter.doRecovering();
	BinaryNumber sequence = splitter.getBlocks().get(0);

	//goes through sequence bit by bit
	String buffer = "";
	for (int i = 0; i < sequence.getLength(); i++)
	{
	    buffer += sequence.getDigit(i) ? "1" : "0";

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