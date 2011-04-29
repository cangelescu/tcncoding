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
import java.util.List;

/**
 * Common class to control source coders
 * @author Oleksandr Natalenko aka post-factum
 */
public class SourceCoderController
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

    private SourceCoderCode usingCode;
    private String sourceMessage;
    private List<BinaryNumber> sourceSequence = new ArrayList<BinaryNumber>();
    private List<Integer> lengthMap = new ArrayList<Integer>();
    private boolean isCyr = true;

    /**
     * Creates source coder for input message
     * @param _codeType type of using code
     * @param _message input string message
     */
    public SourceCoderController(SourceCoderCode _codeType, String _message)
    {
	sourceMessage = _message;
	usingCode = _codeType;
    }

    /**
     * Runs encoding
     * @return encoded sequence
     */
    public List<BinaryNumber> getSequence()
    {
	switch (usingCode)
	{
	    case MTK2:
		SourceCoderITC2 coderMTK2 = new SourceCoderITC2(sourceMessage);
		sourceSequence = coderMTK2.getSequence();
		break;
	    case MTK5:
		SourceCoderITC5 coderMTK5 = new SourceCoderITC5(sourceMessage);
		sourceSequence = coderMTK5.getSequence();
		break;
	    case KOI8U:
		SourceCoderKOI8U coderKOI8U = new SourceCoderKOI8U(sourceMessage);
		sourceSequence = coderKOI8U.getSequence();
		break;
	    case MORSE:
		SourceCoderMorse coderMorse = new SourceCoderMorse(sourceMessage);
		sourceSequence = coderMorse.getSequence();
		isCyr = coderMorse.isCyrillic();
		break;
	    case SHANNON:
		SourceCoderShannonFano coderShannon = new SourceCoderShannonFano(sourceMessage);
		sourceSequence = coderShannon.getSequence();
		isCyr = coderShannon.isCyrillic();
		break;
	    default:
		break;
	}

	lengthMap.clear();
	for (BinaryNumber cbn: sourceSequence)
	    lengthMap.add(cbn.getLength());

        return sourceSequence;
    }

    /**
     * Returns encoded sequence length
     * @return integer value of encoded sequence length
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
     * @return string representation of HTML-formatted report
     */
    public String getHTMLStringSequence()
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

    /**
     * Returns encoded string sequence
     * @return string representation encoded sequence
     */
    public String getStringSequence()
    {
	String out = "";
	for (BinaryNumber bn: sourceSequence)
	out += bn.getStringSequence();
	return out;
    }

    /**
     * Returns map of blocks length (used for recovering blocks after e.g. Hamming decoder)
     * @return list of blocks length
     */
    public List<Integer> getLengthMap()
    {
	return lengthMap;
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
