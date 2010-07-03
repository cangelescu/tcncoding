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
    private List<List<FunctionStep>> summatorSignal;
    private double threshold;
    private BinaryNumber outputNumber;

    /**
     * Creates resolver
     * @param _summatorSignal signal from summator to operate on
     * @param _modulationType type of using modulation
     */
    public Resolver(List<List<FunctionStep>> _summatorSignal, double _threshold)
    {
	summatorSignal = _summatorSignal;
	threshold = _threshold;
    }

    /**
     * Runs summing
     */
    public void doResolving()
    {
	List<Boolean> out = new ArrayList<Boolean>();
	for (List<FunctionStep> currentSymbol: summatorSignal)
	{
	    double value = currentSymbol.get(currentSymbol.size() - 1).getY();
	    out.add(value > threshold);
	}
	outputNumber = new BinaryNumber(out);
    }

    /**
     * Returns binary list
     * @return
     */
    public BinaryNumber getBinaryNumber()
    {
	return outputNumber;
    }

    /**
     * Returns HTML-formatted encoded string sequence
     * @return
     */
    public String getStringSequence()
    {
	String out = "<html>";
	for (boolean bn: outputNumber.getBinaryArray())
	{
	    out += "<font size=\"5\">";
	    out += bn ? "1" : "0";
	    out += "</font>";
	}
	out += "</html>";
	return out;
    }
}
