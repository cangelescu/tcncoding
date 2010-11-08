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
public class SourceCoderShannonFano
{

    private String message;
    private HashMap<String, BinaryNumber> codeMapCyr = new HashMap<String, BinaryNumber>();
    private HashMap<String, BinaryNumber> codeMapLat = new HashMap<String, BinaryNumber>();
    private List<BinaryNumber> sourceSequence = new ArrayList<BinaryNumber>();

    /**
     * Creates Shannon-Fano source coder
     * @param _message
     */
    public SourceCoderShannonFano(String _message)
    {
	message = _message;

	SourceCoderCodeMapLoader loaderCyr = new SourceCoderCodeMapLoader("shannon_cyr");
	SourceCoderCodeMapLoader loaderLat = new SourceCoderCodeMapLoader("shannon_lat");
	codeMapCyr = loaderCyr.getCodeMap();
	codeMapLat = loaderLat.getCodeMap();
    }

    /**
     * Encodes source sequence with Shannon-Fano code
     */
    public void doEncoding()
    {
	sourceSequence.clear();

	String wMessage = message.toUpperCase();

	for (int i = 0; i < wMessage.length(); i++)
	{
	    char wChar = wMessage.charAt(i);

	    BinaryNumber bShannonCyr = codeMapCyr.get(String.valueOf(wChar));
	    BinaryNumber bShannonLat = codeMapLat.get(String.valueOf(wChar));
	    if (bShannonCyr != null)
	    	sourceSequence.add(bShannonCyr);
	    else
	    if (bShannonLat != null)
		sourceSequence.add(bShannonLat);
	}
    }

    /**
     * Returns encoded sequence
     * @return list of binary numbers that represents source message
     */
    public List getSequence()
    {
	return sourceSequence;
    }
}
