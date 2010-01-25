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
import java.util.Vector;

public class SourceCoder {

    public enum sourceCoderCode {mtk2, mtk5, koi8u, morse, shannon};

    private sourceCoderCode using_code = null;
    private HashMap<String, BinaryNumber> code_map = new HashMap<String, BinaryNumber>();
    private String source_message = null;
    private Vector<BinaryNumber> source_sequence = new Vector<BinaryNumber>();

    public SourceCoder(sourceCoderCode code_type, String message)
    {
	this.source_message = message;
	FileReader fr = null;
	switch (code_type)
	{
	    case mtk2:
		this.using_code = sourceCoderCode.mtk2;
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
		try
		{
		    fr = new FileReader("mtk5");
		} catch (Exception ex)
		{
		    System.err.println(ex.getMessage());
		}
		break;
	    case koi8u:
		this.using_code = sourceCoderCode.koi8u;
		try
		{
		    fr = new FileReader("koi8u");
		} catch (Exception ex)
		{
		    System.err.println(ex.getMessage());
		}
		break;
	    case morse:
		this.using_code = sourceCoderCode.morse;
		try
		{
		    fr = new FileReader("morse");
		} catch (Exception ex)
		{
		    System.err.println(ex.getMessage());
		}
		break;
	    case shannon:
		this.using_code = sourceCoderCode.shannon;
		try
		{
		    fr = new FileReader("shannon");
		} catch (Exception ex)
		{
		    System.err.println(ex.getMessage());
		}
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
		BinaryNumber bnum = new BinaryNumber(parts[0], parts[0].length());
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
	    case koi8u:
		working_message = this.source_message;
		break;
	    case morse:
		working_message = this.source_message.toUpperCase();
		break;
	    case shannon:
		working_message = this.source_message.toUpperCase();
		break;
	    default:
		break;
	}
	int len = working_message.length();
	for (int i = 0; i < len; i++)
	{
	    char current_char = working_message.charAt(i);
	    BinaryNumber num = this.code_map.get(String.valueOf(current_char));
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
	for (BinaryNumber bn: this.source_sequence)
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
