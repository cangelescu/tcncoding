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
 * Control class for decoding channel codes
 * @author Oleksandr Natalenko aka post-factum
 */
public class ChannelDecoderController
{

    private List<BinaryNumber> inputSequence;
    private ChannelCoderController.ChannelCoderCode usingCode;
    private List<BinaryNumber> outputSequence = new ArrayList<BinaryNumber>();
    private String report;
    private int headLength;
    private List<Integer> lengthMap;
    private boolean enabled;

    /**
     * Creates channel decoder with given code and symbols on its input
     * @param _symbols input symbols
     * @param _codeType code to use
     * @param _headLength length of trailing head in the sequence
     * @param _lengthMap map of blocks' length
     * @param _enabled indicates if to use channel coding
     */
    public ChannelDecoderController(List<BinaryNumber> _symbols, ChannelCoderController.ChannelCoderCode _codeType, int _headLength, List<Integer> _lengthMap, boolean _enabled)
    {
	inputSequence = _symbols;
	usingCode = _codeType;
	headLength = _headLength;
	lengthMap = _lengthMap;
	enabled = _enabled;
    }

    /**
     * Returns decoded sequence
     * @return list of decoded binary numbers
     */
    public List<BinaryNumber> getSequence()
    {
        outputSequence.clear();
	if (enabled)
	{
	    switch (usingCode)
	    {
		case PARITY_BIT:
		    ChannelDecoderParityBit channelDecoderParityBit = new ChannelDecoderParityBit(inputSequence);
		    outputSequence = channelDecoderParityBit.getSequence();
		    report = channelDecoderParityBit.getReport();
		    break;
		case INVERSED:
		    ChannelDecoderInversed channelDecoderInversed = new ChannelDecoderInversed(inputSequence);
		    outputSequence = channelDecoderInversed.getSequence();
		    report = channelDecoderInversed.getReport();
		    break;
		case MANCHESTER:
		    ChannelDecoderManchester channelDecoderManchester = new ChannelDecoderManchester(inputSequence);
		    outputSequence = channelDecoderManchester.getSequence();
		    report = channelDecoderManchester.getReport();
		    break;
		case HAMMING:
		    ChannelDecoderHamming channelDecoderHamming = new ChannelDecoderHamming(inputSequence, headLength, lengthMap);
		    outputSequence = channelDecoderHamming.getSequence();
		    report = channelDecoderHamming.getReport();
		    break;
                case CYCLIC:
		    ChannelDecoderCyclic channelDecoderCyclic = new ChannelDecoderCyclic(inputSequence, headLength, lengthMap);
		    outputSequence = channelDecoderCyclic.getSequence();
		    report = channelDecoderCyclic.getReport();
		    break;
		default:
		    break;
	    }
	} else
        {
	    outputSequence = inputSequence;
            report = "<html>" + java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("CHANNEL CODING IS NOT USED") + "</html>";
        }
	return outputSequence;
    }

    /**
     * Returns decoded sequence length
     * @return integer value of decoded sequence length
     */
    public int getSequenceLength()
    {
	int out = 0;
	for (BinaryNumber cbn: outputSequence)
	    out += cbn.getLength();
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
}
