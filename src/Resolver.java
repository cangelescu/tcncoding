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
import java.util.Random;

/**
 *
 * @author post-factum
 */
public class Resolver
{
    private List<List<List<FunctionStep>>> summatorSignal;
    private double threshold;
    private List<BinaryNumber> outputNumbers;
    private Modulator.ModulationType modulationType;
    private boolean useNoiseErrors;
    private boolean forceErrors;
    private List<BinaryNumber> ethalonBinarySequence;
    private int errorsCount;

    /**
     * Creates resolver
     * @param _summatorSignal signal from summator to operate on
     * @param _threshold threshold value to decide which symbol should be registered
     * @param _modulationType type of using modulation
     * @param _useNoiseErrors whether to use errors, made by channel noise
     * @param _forceErrors whether to force errors injection
     * @param _errorsCount number of errors to inject
     * @param _ethalonBinarySequence ethalon binary sequence to compare with
     */
    public Resolver(List<List<List<FunctionStep>>> _summatorSignal, double _threshold, Modulator.ModulationType _modulationType, boolean _useNoiseErrors, boolean _forceErrors, int _errorsCount, List<BinaryNumber> _ethalonBinarySequence)
    {
	summatorSignal = _summatorSignal;
	threshold = _threshold;
	modulationType = _modulationType;
	useNoiseErrors = _useNoiseErrors;
	forceErrors = _forceErrors;
	ethalonBinarySequence = _ethalonBinarySequence;
	errorsCount = _errorsCount;
    }

    /**
     * Runs resolving
     */
    public void doResolving()
    {
	outputNumbers = new ArrayList<BinaryNumber>();

	//use classic resolving algorithm if enabled
	if (useNoiseErrors)
	{
	    for (List<List<FunctionStep>> cllfs: summatorSignal)
	    {
		List<Boolean> currentBlock = new ArrayList<Boolean>();
		for (List<FunctionStep> clfs: cllfs)
		{
		    double value = clfs.get(clfs.size() - 1).getY();
		    currentBlock.add(value > threshold);
		}
		outputNumbers.add(new BinaryNumber(currentBlock));
	    }
	} else
	    for (BinaryNumber cbn: ethalonBinarySequence)
		outputNumbers.add(cbn);

	//recode sequence if it's RPSK
	if (modulationType == Modulator.ModulationType.RPSK)
	{
	    ModulatorRPSKRecoder recoder = new ModulatorRPSKRecoder(outputNumbers);
	    recoder.doDecoding();
	    outputNumbers = recoder.getList();
	}

	//inject errors if enabled
	if (forceErrors)
	{
	    Random generator = new Random();

	    //store calculated error vectors not to compensate one error with another
	    List<List<Integer>> positions = new ArrayList<List<Integer>>();
	    for (int i = 0; i < errorsCount; i++)
	    {
		int injectionBlock, injectionBlockLength, injectionSymbol;
		boolean found;
		do
		{
		    injectionBlock = (int) Math.round(generator.nextDouble() * (outputNumbers.size() - 1));
		    injectionBlockLength = outputNumbers.get(injectionBlock).getLength();
		    injectionSymbol = (int) Math.round(generator.nextDouble() * (injectionBlockLength - 1));

		    found = false;
		    for (List<Integer> cti: positions)
			if (cti.get(0) == injectionBlock && cti.get(1) == injectionSymbol)
			    found = true;
		} while (found);
		List<Integer> record = new ArrayList<Integer>();
		record.add(injectionBlock);
		record.add(injectionSymbol);
		positions.add(record);

		//inject errors
		boolean[] errorVector = new boolean[injectionBlockLength];
		errorVector[injectionSymbol] = true;
		BinaryNumber errorVectorNumber = new BinaryNumber(errorVector);
		BinaryNumber injectedNumber = outputNumbers.get(injectionBlock).sum2(errorVectorNumber);
		outputNumbers.set(injectionBlock, injectedNumber);
	    }
	}
    }

    /**
     * Returns binary list
     * @return
     */
    public List<BinaryNumber> getBinaryNumbers()
    {
	return outputNumbers;
    }

    /**
     * Returns HTML-formatted encoded string sequence
     * @return
     */
    public String getStringSequence()
    {
	String out = "<html>", color, prevColor;
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
	out += "</html>";
	return out;
    }
}
