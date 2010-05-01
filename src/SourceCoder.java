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

public class SourceCoder {

    public enum SourceCoderCode {MTK2, MTK5, KOI8U, MORSE, SHANNON;};

    private SourceCoderCode usingCode = null;
    private HashMap<String, BinaryNumber> codeMap = new HashMap<String, BinaryNumber>();
    private String sourceMessage = null;
    private List<BinaryNumber> sourceSequence = new ArrayList<BinaryNumber>();

    public SourceCoder(SourceCoderCode codeType, String message)
    {
	this.sourceMessage = message;
	String filename = "";
	switch (codeType)
	{
	    case MTK2:
		this.usingCode = SourceCoderCode.MTK2;
		filename = "mtk2";
		break;
	    case MTK5:
		this.usingCode = SourceCoderCode.MTK5;
		filename = "mtk5";
		break;
	    case KOI8U:
		this.usingCode = SourceCoderCode.KOI8U;
		filename = "koi8u";
		break;
	    case MORSE:
		this.usingCode = SourceCoderCode.MORSE;
		filename = "morse";
		break;
	    case SHANNON:
		this.usingCode = SourceCoderCode.SHANNON;
		filename = "shannon";
		break;
	    default:
		break;
	}
	this.codeMap.clear();
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
		this.codeMap.put(parts[1], bnum);
	    }
	} catch (Exception ex)
	{
	    System.err.println(ex.getMessage());
	}
    }

    public void doEncode()
    {
	this.sourceSequence.clear();
	String workingMessage = "";
	/*
	 * ITC-2, Morse and Shennon-Fano codes do not depend on letters' case
	 */
	switch (this.usingCode)
	{
	    case MTK2:
		workingMessage = this.sourceMessage.toUpperCase();
		break;
	    case MTK5:
		workingMessage = this.sourceMessage;
		break;
	    case KOI8U:
		workingMessage = this.sourceMessage;
		break;
	    case MORSE:
		workingMessage = this.sourceMessage.toUpperCase();
		break;
	    case SHANNON:
		workingMessage = this.sourceMessage.toUpperCase();
		break;
	    default:
		break;
	}
	int len = workingMessage.length();
	for (int i = 0; i < len; i++)
	{
	    char current_char = workingMessage.charAt(i);
	    BinaryNumber num = this.codeMap.get(String.valueOf(current_char));
	    if (num != null)
		this.sourceSequence.add(num);
	}
    }

    public List getSequence()
    {
	return this.sourceSequence;
    }

    public String getStringSequence()
    {
	String out = "<html>";
	boolean trigger = false;
	for (BinaryNumber bn: this.sourceSequence)
	{
	    if (trigger)
		out += "<font color=\"blue\">" + bn.getStringSequence() + "</font>";
	    else
		out += "<font color=\"green\">" + bn.getStringSequence() + "</font>";
	    trigger = !trigger;
	}
	out += "</html>";
	return out;
    }
}