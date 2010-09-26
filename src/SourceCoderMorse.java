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
public class SourceCoderMorse
{

    private String message;
    private HashMap<String, BinaryNumber> codeMapMorse = new HashMap<String, BinaryNumber>();
    private List<BinaryNumber> sourceSequence = new ArrayList<BinaryNumber>();

    /**
     * Creates Morse source coder
     * @param _message
     */
    public SourceCoderMorse(String _message)
    {
	message = _message;

	SourceCoderCodeMapLoader loader = new SourceCoderCodeMapLoader("morse");
	codeMapMorse = loader.getCodeMap();
    }

    /**
     * Encodes source sequence with Morse code)
     */
    public void doEncoding()
    {
	sourceSequence.clear();

	String wMessage = message.toUpperCase();

	for (int i = 0; i < wMessage.length(); i++)
	{
	    char wChar = wMessage.charAt(i);

	    BinaryNumber bMorse = codeMapMorse.get(String.valueOf(wChar));
	    if (bMorse != null)
	    	sourceSequence.add(bMorse);
	}
    }

    /**
     * Returns encoded sequence
     * @return
     */
    public List getSequence()
    {
	return sourceSequence;
    }
}
