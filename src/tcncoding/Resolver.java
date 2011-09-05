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
 * Model of resolver device
 * @author Oleksandr Natalenko aka post-factum
 */
public class Resolver
{
    private List<DigitalSignal> summatorSignal;
    private List<Integer> lengthMap;
    private double threshold;
    private ModulatorController.ModulationType modulationType;
    private boolean useNoiseErrors;
    private boolean forceErrors;
    private List<BinaryNumber> ethalonBinarySequence;
    private int errorsCount;
    private boolean perBlock;
    private List<BinaryNumber> outputSequence = new ArrayList<BinaryNumber>();

    /**
     * Creates resolver
     * @param _summatorSignal signal from summator to operate on
     * @param _lengthMap 
     * @param _threshold threshold value to decide which symbol should be registered
     * @param _modulationType type of using modulation
     * @param _useNoiseErrors whether to use errors, made by channel noise
     * @param _forceErrors whether to force errors injection
     * @param _errorsCount number of errors to inject
     * @param _perBlock indicates using per-block injection
     * @param _ethalonBinarySequence ethalon binary sequence to compare with
     */
    public Resolver(List<DigitalSignal> _summatorSignal, List<Integer> _lengthMap, double _threshold, ModulatorController.ModulationType _modulationType, boolean _useNoiseErrors, boolean _forceErrors, int _errorsCount, boolean _perBlock, List<BinaryNumber> _ethalonBinarySequence)
    {
	summatorSignal = _summatorSignal;
        lengthMap = _lengthMap;
	threshold = _threshold;
	modulationType = _modulationType;
	useNoiseErrors = _useNoiseErrors;
	forceErrors = _forceErrors;
	ethalonBinarySequence = _ethalonBinarySequence;
	errorsCount = _errorsCount;
	perBlock = _perBlock;
    }

    /**
     * Runs resolving
     * @return received bits
     */
    public List<BinaryNumber> getBinaryNumbers()
    {
	List<BinaryNumber> resolvedSequence = new ArrayList<BinaryNumber>();

	//use classic resolving algorithm if enabled
	if (useNoiseErrors)
	{
            //resolves input signal
            List<Boolean> resolvedLinearSequence = new ArrayList<Boolean>();
            for (DigitalSignal cds: summatorSignal)
            {
                double value = cds.getSample(cds.getSamplesCount() - 1).getY();
                resolvedLinearSequence.add(value > threshold);
            }
            //recombines linear sequence into blocks
            int index = 0;
            for (Integer ci: lengthMap)
            {
                boolean[] newBlock = new boolean[ci];
                for (int i = 0; i < ci; i++)
                    newBlock[i] = resolvedLinearSequence.get(index + i);
                resolvedSequence.add(new BinaryNumber(newBlock));
                index += ci;
            }
	} else
            resolvedSequence = ethalonBinarySequence;

	//recode sequence if it's RPSK
	if (modulationType == ModulatorController.ModulationType.RPSK)
	{
	    ModulatorRPSKRecoder recoder = new ModulatorRPSKRecoder(resolvedSequence);
	    resolvedSequence = recoder.getDecodedList();
	}

	//inject errors if enabled
	if (forceErrors)
	{
	    ErrorsInjector injector = new ErrorsInjector(resolvedSequence, errorsCount, perBlock);
	    outputSequence = injector.getSequence();
	} else
	//otherwise copy unaffected sequence
            outputSequence = resolvedSequence;
        return outputSequence;
    }

    /**
     * Returns HTML-formatted encoded string sequence
     * @return string representation of HTML-formatted report
     */
    public String getStringSequence()
    {
        String out = "", color, prevColor;
	boolean trigger = false;
	int listLength = outputSequence.size();
	for (int i = 0; i < listLength; i++)
	{
	    boolean[] receivedSequence = outputSequence.get(i).getBinaryArray();
	    boolean[] ethalonSequence = ethalonBinarySequence.get(i).getBinaryArray();

	    if (trigger)
		color = "blue";
	    else
		color = "green";

	    int sequenceLength = receivedSequence.length;
	    for (int k = 0; k < sequenceLength; k++)
	    {
		prevColor = color;
		if (receivedSequence[k] != ethalonSequence[k])
		    color = "red";
		out += "<font color=\"" + color + "\" size=\"5\">";
		out += receivedSequence[k] ? "1" : "0";
		out += "</font>";
		color = prevColor;
	    }

	    trigger = !trigger;
	    out += " ";
	}
	return out;
    }
}
