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
    private HashMap<String, String> codeMapCyr = new HashMap<String, String>();
    private HashMap<String, String> codeMapLat = new HashMap<String, String>();
    private List<BinaryNumber> sourceSequence = new ArrayList<BinaryNumber>();

    /**
     * Creates Shannon-Fano source decoder
     * @param _sourceSequence
     */
    public SourceDecoderShannonFano(List<BinaryNumber> _sourceSequence)
    {
	sourceSequence = _sourceSequence;

	SourceDecoderCodeMapLoader loaderCyr = new SourceDecoderCodeMapLoader("shannon_cyr");
	SourceDecoderCodeMapLoader loaderLat = new SourceDecoderCodeMapLoader("shannon_lat");
	codeMapCyr = loaderCyr.getCodeMap();
	codeMapLat = loaderLat.getCodeMap();
    }

    /**
     * Decodes source message with Shannon-Fano code
     */
    public void doDecoding()
    {
	sourceMessage = "";

	for (BinaryNumber cbn: sourceSequence)
	{
	    String currentCharCyr = codeMapCyr.get(cbn.getStringSequence());
	    String currentCharLat = codeMapLat.get(cbn.getStringSequence());
	    if (currentCharCyr != null)
		sourceMessage += currentCharCyr;
	    else
	    if (currentCharLat != null)
		sourceMessage += currentCharLat;
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
