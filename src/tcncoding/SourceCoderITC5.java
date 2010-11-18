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

import java.util.HashMap;

/**
 * Allows using ITC-5
 * @author post-factum
 */
public class SourceCoderITC5 extends SourceCoder
{

    private HashMap<String, BinaryNumber> codeMapMTK5 = new HashMap<String, BinaryNumber>();

    /**
     * Creates ITC-5 source coder
     * @param _message
     */
    public SourceCoderITC5(String _message)
    {
	message = _message;

	SourceCoderCodeMapLoader loader = new SourceCoderCodeMapLoader("mtk5");
	codeMapMTK5 = loader.getCodeMap();
    }

    /**
     * Encodes source sequence with ITC-5
     */
    public void doEncoding()
    {
	sourceSequence.clear();

	for (int i = 0; i < message.length(); i++)
	{
	    char wChar = message.charAt(i);

	    BinaryNumber bMTK5 = codeMapMTK5.get(String.valueOf(wChar));
	    if (bMTK5 != null)
	    	sourceSequence.add(bMTK5);
	}
    }
}
