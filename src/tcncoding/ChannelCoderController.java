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
 * Control class of applying channel codes
 * @author Oleksandr Natalenko aka post-factum
 */
public class ChannelCoderController
{

    /**
     * List of possible channel coder codes
     */
    public enum ChannelCoderCode
    {

	/**
	 * Code with parity bit checking
	 */
	PARITY_BIT,
	/**
	 * Inversed code
	 */
	INVERSED,
	/**
	 * Manchester code
	 */
	MANCHESTER,
	/**
	 * Hamming code
	 */
	HAMMING,
        /**
         * Cyclic code
         */
        CYCLIC;
    };

    private List<BinaryNumber> sourceSymbols;
    private ChannelCoderCode usingCode;
    private List<BinaryNumber> channelSequence = new ArrayList<BinaryNumber>();
    private int headLength;
    private boolean enabled;
    private String report;

    /**
     * Creates channel coder with given code and symbols on its input
     * @param _symbols input symbols
     * @param _codeType code to use
     * @param _enabled indicates whether to enable channel coding
     */
    public ChannelCoderController(List<BinaryNumber> _symbols, ChannelCoderCode _codeType, boolean _enabled)
    {
	sourceSymbols = _symbols;
	usingCode = _codeType;
	enabled = _enabled;
    }

    /**
     * Returns encoded sequence
     * @return list of encoded binary numbers
     */
    public List<BinaryNumber> getSequence()
    {
        channelSequence.clear();
	if (enabled)
	{
            ChannelCoder channelCoder = null;
	    switch (usingCode)
	    {
		case PARITY_BIT:
		    channelCoder = new ChannelCoderParityBit(sourceSymbols);
		    break;
		case INVERSED:
		    channelCoder = new ChannelCoderInversed(sourceSymbols);
		    break;
		case MANCHESTER:
		    channelCoder = new ChannelCoderManchester(sourceSymbols);
		    break;
		case HAMMING:
		    channelCoder = new ChannelCoderHamming(sourceSymbols);
		    break;
                case CYCLIC:
		    channelCoder = new ChannelCoderCyclic(sourceSymbols);
		    break;
		default:
		    break;
	    }
            channelSequence = channelCoder.getSequence();
            headLength = channelCoder.getHeadLength();
            report = channelCoder.getReport();
	} else
        {
	    channelSequence = sourceSymbols;
            report = "<html>" + java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("CHANNEL CODING IS NOT USED") + "</html>";
        }
	return channelSequence;
    }

    /**
     * Returns encoded sequence length
     * @return integer value of encoded sequence length
     */
    public int getSequenceLength()
    {
	int out = 0;
	for (BinaryNumber cbn: channelSequence)
	    out += cbn.getLength();
	return out;
    }
    
    public List<Integer> getLengthMap()
    {
        List<Integer> out = new ArrayList<Integer>();
        for (BinaryNumber cbn: channelSequence)
            out.add(cbn.getLength());
        return out;
    }

    /**
     * Returns HTML-formatted encoded string sequence
     * @return string representation of HTML-formatted report
     */
    public String getHTMLReport()
    {
	return report;
    }

    /**
     * Returns length of sequence head that was added by splitter in Hamming coder
     * @return integer value of trailing head length
     */
    public int getHeadLength()
    {
	return headLength;
    }
}
