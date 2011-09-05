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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Allows using KOI8-U
 * @author Oleksandr Natalenko aka post-factum
 */
public class SourceCoderKOI8U implements SourceCoder
{
    private String message;
    private List<BinaryNumber> sourceSequence = new ArrayList<BinaryNumber>();
    private HashMap<String, BinaryNumber> codeMapKOI8U = new HashMap<String, BinaryNumber>();

    /**
     * 
     * @return
     */
    public boolean isCyrillic()
    {
        return true;
    }

    /**
     * Creates KOI8-U source coder
     * @param _message
     */
    public SourceCoderKOI8U(String _message)
    {
	message = _message;

	SourceCoderCodeMapLoader loader = new SourceCoderCodeMapLoader("koi8u");
	codeMapKOI8U = loader.getCodeMap();
    }

    /**
     * Encodes source sequence with KOI8-U
     * @return encoded sequence
     */
    public List<BinaryNumber> getSequence()
    {
	sourceSequence.clear();

	for (int i = 0; i < message.length(); i++)
	{
	    char wChar = message.charAt(i);

	    BinaryNumber bKOI8U = codeMapKOI8U.get(String.valueOf(wChar));
	    if (bKOI8U != null)
	    	sourceSequence.add(bKOI8U);
	}
        return sourceSequence;
    }
}
