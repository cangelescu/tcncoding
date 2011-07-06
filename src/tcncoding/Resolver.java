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
    private double threshold;
    private ModulatorController.ModulationType modulationType;
    private boolean useNoiseErrors;
    private boolean forceErrors;
    private List<BinaryNumber> ethalonBinarySequence;
    private int errorsCount;
    private boolean perBlock;
    private List<Boolean> outputSequence = new ArrayList<Boolean>();
    private List<Boolean> ethalonLinearSequence = new ArrayList<Boolean>();

    /**
     * Creates resolver
     * @param _summatorSignal signal from summator to operate on
     * @param _threshold threshold value to decide which symbol should be registered
     * @param _modulationType type of using modulation
     * @param _useNoiseErrors whether to use errors, made by channel noise
     * @param _forceErrors whether to force errors injection
     * @param _errorsCount number of errors to inject
     * @param _perBlock indicates using per-block injection
     * @param _ethalonBinarySequence ethalon binary sequence to compare with
     */
    public Resolver(List<DigitalSignal> _summatorSignal, double _threshold, ModulatorController.ModulationType _modulationType, boolean _useNoiseErrors, boolean _forceErrors, int _errorsCount, boolean _perBlock, List<BinaryNumber> _ethalonBinarySequence)
    {
	summatorSignal = _summatorSignal;
	threshold = _threshold;
	modulationType = _modulationType;
	useNoiseErrors = _useNoiseErrors;
	forceErrors = _forceErrors;
	ethalonBinarySequence = _ethalonBinarySequence;
        BitsRectifier bitsRectifier = new BitsRectifier(ethalonBinarySequence);
        ethalonLinearSequence = bitsRectifier.getBits();
	errorsCount = _errorsCount;
	perBlock = _perBlock;
    }

    /**
     * Runs resolving
     * @return received bits
     */
    public List<Boolean> getBinaryNumbers()
    {
	List<Boolean> resolvedBits = new ArrayList<Boolean>();

	//use classic resolving algorithm if enabled
	if (useNoiseErrors)
	{
            for (DigitalSignal cds: summatorSignal)
            {
                double value = cds.getSample(cds.getSamplesCount() - 1).getY();
                resolvedBits.add(value > threshold);
            }
	} else
            resolvedBits = ethalonLinearSequence;

	//recode sequence if it's RPSK
	if (modulationType == ModulatorController.ModulationType.RPSK)
	{
	    ModulatorRPSKRecoder recoder = new ModulatorRPSKRecoder(resolvedBits);
	    resolvedBits = recoder.getDecodedList();
	}

	//inject errors if enabled
	if (forceErrors)
	{
	    ErrorsInjector injector = new ErrorsInjector(resolvedBits, errorsCount, perBlock);
	    outputSequence = injector.getSequence();
	} else
	//otherwise copy unaffected sequence
            outputSequence = resolvedBits;
        return outputSequence;
    }

    /**
     * Returns HTML-formatted encoded string sequence
     * @return string representation of HTML-formatted report
     */
    public String getStringSequence()
    {
        String out = "", color;
        for (int i = 0; i < outputSequence.size(); i++)
        {
            if (outputSequence.get(i) == ethalonLinearSequence.get(i))
                color = "black";
            else
                color = "red";
            out += "<font color=\"" + color + "\" size=\"5\">";
            out += outputSequence.get(i) ? "1" : "0";
            out += "</font>";
        }
	return out;
    }
}
