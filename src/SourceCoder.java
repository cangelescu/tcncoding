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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author post-factum
 */
public class SourceCoder
{

    /**
     * List of source coder codes
     */
    public enum SourceCoderCode
    {

	/**
	 * MTK-2 (ITC-2)
	 */
	MTK2,
	/**
	 * MTK-5 (ITC-5)
	 */
	MTK5,
	/**
	 * KOI8-U
	 */
	KOI8U,
	/**
	 * Morse code
	 */
	MORSE,
	/**
	 * Shannon code
	 */
	SHANNON;
    };

    private SourceCoderCode usingCode = null;
    private HashMap<String, BinaryNumber> codeMap = new HashMap<String, BinaryNumber>();
    private String sourceMessage = null;
    private List<BinaryNumber> sourceSequence = new ArrayList<BinaryNumber>();

    /**
     * Creates source coder for input message
     * @param _codeType type of using code
     * @param _message input string message
     */
    public SourceCoder(SourceCoderCode _codeType, String _message)
    {
	sourceMessage = _message;
	String filename = "";
	switch (_codeType)
	{
	    case MTK2:
		usingCode = SourceCoderCode.MTK2;
		filename = "mtk2";
		break;
	    case MTK5:
		usingCode = SourceCoderCode.MTK5;
		filename = "mtk5";
		break;
	    case KOI8U:
		usingCode = SourceCoderCode.KOI8U;
		filename = "koi8u";
		break;
	    case MORSE:
		usingCode = SourceCoderCode.MORSE;
		filename = "morse";
		break;
	    case SHANNON:
		usingCode = SourceCoderCode.SHANNON;
		filename = "shannon";
		break;
	    default:
		break;
	}
	codeMap.clear();
	String line = "";
	try
	{
	    //files of code tables must be present in working directory
	    FileReader fr = new FileReader(filename);
	    BufferedReader bfr = new BufferedReader(fr);
	    while((line = bfr.readLine()) != null)
	    {
		String[] parts = line.split("#");
		BinaryNumber bnum = new BinaryNumber(parts[0], parts[0].length());
		codeMap.put(parts[1], bnum);
	    }
	} catch (Exception ex)
	{
	    System.err.println(ex.getMessage());
	}
    }

    /**
     * Runs encoding
     */
    public void doEncode()
    {
	sourceSequence.clear();
	String workingMessage = "";
	/*
	 * ITC-2, Morse and Shannon-Fano codes do not depend on letters' case
	 */
	switch (usingCode)
	{
	    case MTK2:
		workingMessage = sourceMessage.toUpperCase();
		break;
	    case MTK5:
		workingMessage = sourceMessage;
		break;
	    case KOI8U:
		workingMessage = sourceMessage;
		break;
	    case MORSE:
		workingMessage = sourceMessage.toUpperCase();
		break;
	    case SHANNON:
		workingMessage = sourceMessage.toUpperCase();
		break;
	    default:
		break;
	}
	int len = workingMessage.length();
	for (int i = 0; i < len; i++)
	{
	    char current_char = workingMessage.charAt(i);
	    BinaryNumber num = codeMap.get(String.valueOf(current_char));
	    if (num != null)
		sourceSequence.add(num);
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

    /**
     * Returns encoded sequence length
     * @return
     */
    public int getSequenceLength()
    {
	int out = 0;
	for (BinaryNumber cbn: sourceSequence)
	    out += cbn.getLength();
	return out;
    }

    /**
     * Returns encoded HTML-formatted string sequence
     * @return
     */
    public String getStringSequence()
    {
	String out = "<html>";
	boolean trigger = false;
	for (BinaryNumber bn: sourceSequence)
	{
	    if (trigger)
		out += "<font color=\"blue\" size=\"5\">" + bn.getStringSequence() + " </font>";
	    else
		out += "<font color=\"green\" size=\"5\">" + bn.getStringSequence() + " </font>";
	    trigger = !trigger;
	}
	out += "</html>";
	return out;
    }
}
