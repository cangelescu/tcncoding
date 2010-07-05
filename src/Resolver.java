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
 *
 * @author post-factum
 */
public class Resolver
{
    private List<List<List<FunctionStep>>> summatorSignal;
    private double threshold;
    private List<BinaryNumber> outputNumbers;
    private Modulator.ModulationType modulationType;

    /**
     * Creates resolver
     * @param _summatorSignal signal from summator to operate on
     * @param _threshold threshold value to decide which symbol should be registered
     * @param _modulationType type of using modulation
     */
    public Resolver(List<List<List<FunctionStep>>> _summatorSignal, double _threshold, Modulator.ModulationType _modulationType)
    {
	summatorSignal = _summatorSignal;
	threshold = _threshold;
	modulationType = _modulationType;
    }

    /**
     * Runs resolving
     */
    public void doResolving()
    {
	outputNumbers = new ArrayList<BinaryNumber>();
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
	
	if (modulationType == Modulator.ModulationType.RPSK)
	{
	    ModulatorRPSKRecoder recoder = new ModulatorRPSKRecoder(outputNumbers);
	    recoder.doDecoding();
	    outputNumbers = recoder.getList();
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
	String out = "<html>";
	boolean trigger = false;
	for (BinaryNumber bn: outputNumbers)
	{
	    if (trigger)
		out += "<font color=\"blue\" size=\"5\">" + bn.getStringSequence() + " </font>";
	    else
		out += "<font color=\"green\" size=\"5\">" + bn.getStringSequence() + " </font>";
	    trigger = !trigger;
	}
	out += "</html>";
	return out;
    }
}
