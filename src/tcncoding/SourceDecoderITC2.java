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

import java.util.HashMap;
import java.util.List;

/**
 * Allows using ITC-2
 * @author Oleksandr Natalenko aka post-factum
 */
public class SourceDecoderITC2 extends SourceDecoder
{

    private HashMap<String, String> codeMapLat = new HashMap<String, String>();
    private HashMap<String, String> codeMapInt = new HashMap<String, String>();
    private HashMap<String, String> codeMapNum = new HashMap<String, String>();
    private HashMap<String, String> codeMapCtl = new HashMap<String, String>();

    /**
     * Creates ITC-2 source decoder
     * @param _sourceSequence
     */
    public SourceDecoderITC2(List<BinaryNumber> _sourceSequence)
    {
	sourceSequence = _sourceSequence;

	SourceDecoderCodeMapLoader loaderLat = new SourceDecoderCodeMapLoader("mtk2_lat");
	SourceDecoderCodeMapLoader loaderInt = new SourceDecoderCodeMapLoader("mtk2_int");
	SourceDecoderCodeMapLoader loaderNum = new SourceDecoderCodeMapLoader("mtk2_num");
	SourceDecoderCodeMapLoader loaderCtl = new SourceDecoderCodeMapLoader("mtk2_ctl");
	codeMapLat = loaderLat.getCodeMap();
	codeMapInt = loaderInt.getCodeMap();
	codeMapNum = loaderNum.getCodeMap();
	codeMapCtl = loaderCtl.getCodeMap();
    }

    /**
     * Decodes source message with ITC-2
     */
    public void doDecoding()
    {
	sourceMessage = "";

	SourceCoderITC2.CodeMapPart codeMapPart = SourceCoderITC2.CodeMapPart.INT;

	for (BinaryNumber cbn: sourceSequence)
	{
	    String currentCharCtl = codeMapCtl.get(cbn.getStringSequence());

	    if (currentCharCtl == null)
	    {
		String currentChar = null;
		switch (codeMapPart)
		{
		    case LAT:
			currentChar = codeMapLat.get(cbn.getStringSequence());
			break;
		    case INT:
			currentChar = codeMapInt.get(cbn.getStringSequence());
			break;
		    case NUM:
			currentChar = codeMapNum.get(cbn.getStringSequence());
			break;
		    default:
			break;
		}
		sourceMessage += (currentChar != null) ? currentChar : "*";
	    } else
	    {
		if (currentCharCtl.equals("LAT"))
		    codeMapPart = SourceCoderITC2.CodeMapPart.LAT;
		else
		if (currentCharCtl.equals("INT"))
		    codeMapPart = SourceCoderITC2.CodeMapPart.INT;
		else
		if (currentCharCtl.equals("NUM"))
		    codeMapPart = SourceCoderITC2.CodeMapPart.NUM;
	    }
	}
    }
}
