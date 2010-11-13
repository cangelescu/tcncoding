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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

/**
 * Loads code table and creates its map
 * @author post-factum
 */
public class SourceDecoderCodeMapLoader
{

    private HashMap<String, String> codeMap = new HashMap<String, String>();

    /**
     * Creates code map loader
     * @param _fileName
     */
    public SourceDecoderCodeMapLoader(String _fileName)
    {
	String line = null;
	try
	{
	    //files of code tables must be present in working directory
	    FileReader fr = new FileReader(_fileName);
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
     * Returns loaded code map
     * @return list of string pairs that represents loaded code map
     */
    public HashMap<String, String> getCodeMap()
    {
	return codeMap;
    }
}
