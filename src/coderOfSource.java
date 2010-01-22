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
import java.util.Map;
import java.util.Vector;

public class coderOfSource {

    public enum sourceCoderCode {mtk2, mtk5, morze};

    private sourceCoderCode using_code = null;
    private Map code_map = new HashMap();
    private String source_message = null;
    public int alignment = 0;
    private Vector source_sequence = new Vector();

    public coderOfSource(sourceCoderCode code_type, String message)
    {
	this.source_message = message;
	FileReader fr = null;
	switch (code_type)
	{
	    case mtk2:
		this.using_code = sourceCoderCode.mtk2;
		this.alignment = 5;
		try
		{
		    fr = new FileReader("mtk2");
		} catch (Exception ex)
		{
		    System.err.println(ex.getMessage());
		}
		break;
	    case mtk5:
		this.using_code = sourceCoderCode.mtk5;
		this.alignment = 7;
		try
		{
		    fr = new FileReader("mtk5");
		} catch (Exception ex)
		{
		    System.err.println(ex.getMessage());
		}
		break;
	    case morze:
		break;
	    default:
		break;
	}
	this.code_map.clear();
	String line = "";
	try
	{
	    BufferedReader bfr = new BufferedReader(fr);
	    while((line = bfr.readLine()) != null)
	    {
		String[] parts = line.split("#");
		binaryNumber bnum = new binaryNumber(parts[0]);
		this.code_map.put(parts[1], bnum);
	    }
	} catch (Exception ex)
	{
	    System.err.println(ex.getMessage());
	}
    }

    public void doEncode()
    {
	this.source_sequence.clear();
	String working_message = "";
	switch (this.using_code)
	{
	    case mtk2:
		working_message = this.source_message.toUpperCase();
		break;
	    case mtk5:
		working_message = this.source_message;
		break;
	    case morze:
		break;
	    default:
		break;
	}
	int len = working_message.length();
	for (int i = 0; i < len; i++)
	{
	    char current_char = working_message.charAt(i);
	    binaryNumber num = (binaryNumber)this.code_map.get(String.valueOf(current_char));
	    if (num != null)
		this.source_sequence.add(num);
	}
    }

    public Vector getSequence()
    {
	return this.source_sequence;
    }

    public String getStringSequence()
    {
	String out = "<html>";
	boolean trigger = false;
	for (Object bn: this.source_sequence)
	{
	    binaryNumber number = (binaryNumber)bn;
	    if (trigger)
		out += "<font color=\"blue\">" + number.getString(this.alignment) + "</font>";
	    else
		out += "<font color=\"green\">" + number.getString(this.alignment) + "</font>";
	    trigger = !trigger;
	}
	out += "</html>";
	return out;
    }
}
