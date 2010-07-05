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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author post-factum
 */
public class DataVizualizatorProvider
{

    /**
     * List of signal types
     */
    public enum SignalType
    {

	/**
	 * Signal from modulator
	 */
	MODULATOR,
	/**
	 * Signal from channel
	 */
	CHANNEL,
	/**
	 * Signal from multiplier
	 */
	MULTIPLIER,
	/**
	 * Tabulated signal
	 */
	TABULATED;
    };
    private SignalType signalType;

    private List<ModulatorSignal> modulatorSignal = null;
    private List<ChannelSignal> channelSignal = null;
    private List<MultiplierSignal> multiplierSignal = null;
    private List<List<FunctionStep>> integratorSignal = null;

    private double xStart, xEnd, maxValue;
    private String description;
    private Color chartColor;

    public DataVizualizatorProvider(List _data, SignalType _signalType, String _description, Color _chartColor)
    {
	signalType = _signalType;
	switch (signalType)
	{
	    case MODULATOR:
		modulatorSignal = _data;
		xStart = modulatorSignal.get(0).getStart();
		xEnd = modulatorSignal.get(modulatorSignal.size() - 1).getEnd();
		maxValue = modulatorSignal.get(0).getMaxValue();
		for (ModulatorSignal cms: modulatorSignal)
		    if (cms.getMaxValue() > maxValue)
			maxValue = cms.getMaxValue();
		break;
	    case CHANNEL:
		channelSignal = _data;
		xStart = channelSignal.get(0).getStart();
		xEnd = channelSignal.get(channelSignal.size() - 1).getEnd();
		maxValue = channelSignal.get(0).getMaxValue();
		for (ChannelSignal cms: channelSignal)
		    if (cms.getMaxValue() > maxValue)
			maxValue = cms.getMaxValue();
		break;
	    case MULTIPLIER:
		multiplierSignal = _data;
		xStart = multiplierSignal.get(0).getStart();
		xEnd = multiplierSignal.get(multiplierSignal.size() - 1).getEnd();
		maxValue = multiplierSignal.get(0).getMaxValue();
		for (MultiplierSignal cms: multiplierSignal)
		    if (cms.getMaxValue() > maxValue)
			maxValue = cms.getMaxValue();
		break;
	    case TABULATED:
		integratorSignal = _data;
		xStart = integratorSignal.get(0).get(0).getX();
		int lastBlock = integratorSignal.size() - 1;
		int lastSymbol = integratorSignal.get(lastBlock).size() - 1;
		xEnd = integratorSignal.get(lastBlock).get(lastSymbol).getX();
		maxValue = integratorSignal.get(0).get(0).getY();
		for (List<FunctionStep> clfs: integratorSignal)
		    for (FunctionStep cfs: clfs)
			if (cfs.getY() > maxValue)
			    maxValue = cfs.getY();
		break;
	    default:
		break;
	}

	description = _description;
	chartColor = _chartColor;
    }

    /**
     * Returns f(x) of current signal
     * @param x time variable, s
     * @return
     */
    public double getFunction(double x)
    {
	double out = 0;
	switch (signalType)
	{
	    case MODULATOR:
		for (ModulatorSignal cms: modulatorSignal)
		    if (x >= cms.getStart() && x <= cms.getEnd())
			out = cms.function(x);
		break;
	    case CHANNEL:
		for (ChannelSignal cms: channelSignal)
		    if (x >= cms.getStart() && x <= cms.getEnd())
			out = cms.function(x);
		break;
	    case MULTIPLIER:
		for (MultiplierSignal cms: multiplierSignal)
		    if (x >= cms.getStart() && x <= cms.getEnd())
			out = cms.function(x);
		break;
	    case TABULATED:
		for (List<FunctionStep> clfs: integratorSignal)
		{
		    boolean found = false;
		    for (FunctionStep fs: clfs)
			if (fs.getX() >= x)
			{
			    out = fs.getY();
			    found = true;
			    break;
			}
		    if (found)
			break;
		}
		break;
	    default:
		break;
	}
	return out;
    }

    /**
     * Returns signal start
     * @return
     */
    public double getStart()
    {
	return xStart;
    }

    /**
     * Returns signal end
     * @return
     */
    public double getEnd()
    {
	return xEnd;
    }

    /**
     * Returns signal maximum value
     * @return
     */
    public double getMaxValue()
    {
	return maxValue;
    }

    /**
     * Returns signal description
     * @return
     */
    public String getDescription()
    {
	return description;
    }

    /**
     * Returns signal chart color
     * @return
     */
    public Color getChartColor()
    {
	return chartColor;
    }
}
