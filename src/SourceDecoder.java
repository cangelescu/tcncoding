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
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author post-factum
 */
public class SourceDecoder
{

    private HashMap<String, String> codeMap = new HashMap<String, String>();
    private String sourceMessage = "";
    private List<BinaryNumber> sourceSequence;

    /**
     * Creates source decoder for input source sequence
     * @param _codeType type of using code
     * @param _message input string message
     */
    public SourceDecoder(List<BinaryNumber> _sourceSequence, SourceCoder.SourceCoderCode _codeType)
    {
	sourceSequence = _sourceSequence;

	String filename = "";
	switch (_codeType)
	{
	    case MTK2:
		filename = "mtk2";
		break;
	    case MTK5:
		filename = "mtk5";
		break;
	    case KOI8U:
		filename = "koi8u";
		break;
	    case MORSE:
		filename = "morse";
		break;
	    case SHANNON:
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
		BinaryNumber bnum = new BinaryNumber(parts[0]);
		codeMap.put(bnum.getStringSequence(), parts[1]);
	    }
	} catch (Exception ex)
	{
	    System.err.println(ex.getMessage());
	}
    }

    /**
     * Runs decoding
     */
    public void doDecode()
    {
	for (BinaryNumber cbn: sourceSequence)
	{
	    String currentChar = codeMap.get(cbn.getStringSequence());
	    if (currentChar != null)
		sourceMessage += currentChar;
	    else
		sourceMessage += "*";
	}
    }

    /**
     * Returns decoded messaage
     * @return
     */
    public String getMessage()
    {
	return sourceMessage;
    }

}
