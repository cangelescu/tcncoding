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

import java.util.ArrayList;
import java.util.List;

/**
 * Model of resolver device
 * @author post-factum
 */
public class Resolver
{
    private List<List<DigitalSignal>> summatorSignal;
    private double threshold;
    private List<BinaryNumber> outputNumbers = new ArrayList<BinaryNumber>();
    private Modulator.ModulationType modulationType;
    private boolean useNoiseErrors;
    private boolean forceErrors;
    private List<BinaryNumber> ethalonBinarySequence;
    private int errorsCount;
    private boolean perBlock;

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
    public Resolver(List<List<DigitalSignal>> _summatorSignal, double _threshold, Modulator.ModulationType _modulationType, boolean _useNoiseErrors, boolean _forceErrors, int _errorsCount, boolean _perBlock, List<BinaryNumber> _ethalonBinarySequence)
    {
	summatorSignal = _summatorSignal;
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
     */
    public void doResolving()
    {
	List<BinaryNumber> preOutputNumbers = new ArrayList<BinaryNumber>();

	//use classic resolving algorithm if enabled
	if (useNoiseErrors)
	{
	    for (List<DigitalSignal> clds: summatorSignal)
	    {
		List<Boolean> currentBlock = new ArrayList<Boolean>();
		for (DigitalSignal cds: clds)
		{
		    double value = cds.getSample(cds.getSamplesCount() - 1).getY();
		    currentBlock.add(value > threshold);
		}
		preOutputNumbers.add(new BinaryNumber(currentBlock));
	    }
	} else
	    for (BinaryNumber cbn: ethalonBinarySequence)
		preOutputNumbers.add(cbn);

	//recode sequence if it's RPSK
	if (modulationType == Modulator.ModulationType.RPSK)
	{
	    ModulatorRPSKRecoder recoder = new ModulatorRPSKRecoder(preOutputNumbers);
	    recoder.doDecoding();
	    preOutputNumbers = recoder.getList();
	}

	//inject errors if enabled
	if (forceErrors)
	{
	    ErrorsInjector injector = new ErrorsInjector(preOutputNumbers, errorsCount, perBlock);
	    injector.injectErrors();
	    outputNumbers = injector.getSequence();
	} else
	//otherwise copy unaffected sequence
	    for (BinaryNumber cbn: preOutputNumbers)
		outputNumbers.add(cbn);
    }

    /**
     * Returns binary list
     * @return list of binary numbers
     */
    public List<BinaryNumber> getBinaryNumbers()
    {
	return outputNumbers;
    }

    /**
     * Returns HTML-formatted encoded string sequence
     * @return string representation of HTML-formatted report
     */
    public String getStringSequence()
    {
	String out = "", color, prevColor;
	boolean trigger = false;
	int listLength = outputNumbers.size();
	for (int i = 0; i < listLength; i++)
	{
	    boolean[] receivedSequence = outputNumbers.get(i).getBinaryArray();
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
