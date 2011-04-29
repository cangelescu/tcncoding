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
 * Allows using Shannon-Fano code
 * @author Oleksandr Natalenko aka post-factum
 */
public class SourceCoderShannonFano extends SourceCoder
{

    private HashMap<String, BinaryNumber> codeMapCyr = new HashMap<String, BinaryNumber>();
    private HashMap<String, BinaryNumber> codeMapLat = new HashMap<String, BinaryNumber>();
    private boolean isCyr = true;

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
     * @return encoded sequence
     */
    public List<BinaryNumber> getSequence()
    {
	sourceSequence.clear();

	String wMessage = message.toUpperCase();

	//determines character set of the message
	for (int i = 0; i < wMessage.length(); i++)
	{
	    char wChar = wMessage.charAt(0);
	    BinaryNumber bShannonCyr = codeMapCyr.get(String.valueOf(wChar));
	    BinaryNumber bShannonLat = codeMapLat.get(String.valueOf(wChar));
	    if (bShannonCyr != null)
	    {
		isCyr = true;
		break;
	    } else
	    if (bShannonLat != null)
	    {
		isCyr = false;
		break;
	    } else
		continue;
	}

	//encodes message according to determined character set
	for (int i = 0; i < wMessage.length(); i++)
	{
	    char wChar = wMessage.charAt(i);

	    if (isCyr)
	    {
		BinaryNumber bShannonCyr = codeMapCyr.get(String.valueOf(wChar));
		if (bShannonCyr != null)
	    	sourceSequence.add(bShannonCyr);
	    } else
	    {
		BinaryNumber bShannonLat = codeMapLat.get(String.valueOf(wChar));
		if (bShannonLat != null)
		    sourceSequence.add(bShannonLat);
	    }
	}
        return sourceSequence;
    }

    /**
     * Returns if character set is cyrillic
     * @return true if source message is formed using cyrillic characters
     */
    public boolean isCyrillic()
    {
	return isCyr;
    }
}
