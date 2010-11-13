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
 * Allows using ITC-2
 * @author post-factum
 */
public class SourceCoderITC2
{

    private String message;
    private HashMap<String, BinaryNumber> codeMapLat = new HashMap<String, BinaryNumber>();
    private HashMap<String, BinaryNumber> codeMapInt = new HashMap<String, BinaryNumber>();
    private HashMap<String, BinaryNumber> codeMapNum = new HashMap<String, BinaryNumber>();
    private HashMap<String, BinaryNumber> codeMapCtl = new HashMap<String, BinaryNumber>();
    private List<BinaryNumber> sourceSequence = new ArrayList<BinaryNumber>();
    /**
     * Parts of code map
     */
    public enum CodeMapPart
    {
	/**
	 * Latin symbols
	 */
	LAT,
	/**
	 * International symbols
	 */
	INT,
	/**
	 * Numeric symbols
	 */
	NUM,
	/**
	 * Control symbols
	 */
	CTL;
    };

    /**
     * Creates ITC-2 source coder
     * @param _message
     */
    public SourceCoderITC2(String _message)
    {
	message = _message;

	SourceCoderCodeMapLoader latLoader = new SourceCoderCodeMapLoader("mtk2_lat");
	SourceCoderCodeMapLoader intLoader = new SourceCoderCodeMapLoader("mtk2_int");
	SourceCoderCodeMapLoader numLoader = new SourceCoderCodeMapLoader("mtk2_num");
	SourceCoderCodeMapLoader ctlLoader = new SourceCoderCodeMapLoader("mtk2_ctl");
	codeMapLat = latLoader.getCodeMap();
	codeMapInt = intLoader.getCodeMap();
	codeMapNum = numLoader.getCodeMap();
	codeMapCtl = ctlLoader.getCodeMap();
    }

    /**
     * Encodes source sequence with ITC-2
     */
    public void doEncoding()
    {
	sourceSequence.clear();

	String wMessage = message.toUpperCase();

	CodeMapPart codeMapPart = CodeMapPart.INT;

	for (int i = 0; i < wMessage.length(); i++)
	{
	    char wChar = wMessage.charAt(i);

	    BinaryNumber bLat = codeMapLat.get(String.valueOf(wChar));
	    BinaryNumber bInt = codeMapInt.get(String.valueOf(wChar));
	    BinaryNumber bNum = codeMapNum.get(String.valueOf(wChar));
	    if (bLat != null)
	    {
		if (codeMapPart != CodeMapPart.LAT)
		{
		    sourceSequence.add(codeMapCtl.get("LAT"));
		    codeMapPart = CodeMapPart.LAT;
		}
		sourceSequence.add(bLat);
	    } else
	    if (bInt != null)
	    {
		if (codeMapPart != CodeMapPart.INT)
		{
		    sourceSequence.add(codeMapCtl.get("INT"));
		    codeMapPart = CodeMapPart.INT;
		}
		sourceSequence.add(bInt);
	    } else
	    if (bNum != null)
	    {
		if (codeMapPart != CodeMapPart.NUM)
		{
		    sourceSequence.add(codeMapCtl.get("NUM"));
		    codeMapPart = CodeMapPart.NUM;
		}
		sourceSequence.add(bNum);
	    }
	}
    }

    /**
     * Returns encoded sequence
     * @return list of binary numbers that represents source message
     */
    public List<BinaryNumber> getSequence()
    {
	return sourceSequence;
    }
}
