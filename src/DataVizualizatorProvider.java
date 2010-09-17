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

    private double xStart, xEnd, maxValue, minValue, stepSize;
    private String description;
    private Color chartColor;

    /**
     * Creates data vizualizator provider to unify all types of signals
     * @param _data input list of signals
     * @param _signalType type of signals to provide to data vizualizator
     * @param _description description of signal
     * @param _chartColor color of vizualized chart
     */
    public DataVizualizatorProvider(List _data, double _stepSize, SignalType _signalType, String _description, Color _chartColor)
    {
	signalType = _signalType;
	stepSize = _stepSize;
	switch (signalType)
	{
	    case MODULATOR:
		modulatorSignal = _data;
		xStart = modulatorSignal.get(0).getStart();
		xEnd = modulatorSignal.get(modulatorSignal.size() - 1).getEnd();
		maxValue = modulatorSignal.get(0).getMaxValue();
		minValue = modulatorSignal.get(0).getMinValue();
		for (ModulatorSignal cms: modulatorSignal)
		{
		    if (cms.getMaxValue() > maxValue)
			maxValue = cms.getMaxValue();
		    if (cms.getMinValue() < minValue)
			minValue = cms.getMinValue();
		}
		break;
	    case CHANNEL:
		channelSignal = _data;
		xStart = channelSignal.get(0).getStart();
		xEnd = channelSignal.get(channelSignal.size() - 1).getEnd();
		maxValue = channelSignal.get(0).getMaxValue();
		minValue = channelSignal.get(0).getMinValue();
		for (ChannelSignal cms: channelSignal)
		{
		    if (cms.getMaxValue() > maxValue)
			maxValue = cms.getMaxValue();
		    if (cms.getMinValue() < minValue)
			minValue = cms.getMinValue();
		}
		break;
	    case MULTIPLIER:
		multiplierSignal = _data;
		xStart = multiplierSignal.get(0).getStart();
		xEnd = multiplierSignal.get(multiplierSignal.size() - 1).getEnd();
		maxValue = multiplierSignal.get(0).getMaxValue();
		minValue = multiplierSignal.get(0).getMinValue();
		for (MultiplierSignal cms: multiplierSignal)
		{
		    if (cms.getMaxValue() > maxValue)
			maxValue = cms.getMaxValue();
		    if (cms.getMinValue() > minValue)
			minValue = cms.getMinValue();
		}
		break;
	    case TABULATED:
		integratorSignal = _data;
		int count = 0;
		for (List<FunctionStep> clfs: integratorSignal)
		    count += clfs.size();
		xStart = integratorSignal.get(0).get(0).getX();
		xEnd = xStart + stepSize * count;
		maxValue = integratorSignal.get(0).get(0).getY();
		minValue = integratorSignal.get(0).get(0).getY();
		for (List<FunctionStep> clfs: integratorSignal)
		    for (FunctionStep cfs: clfs)
		    {
			if (cfs.getY() > maxValue)
			    maxValue = cfs.getY();
			if (cfs.getY() < minValue)
			    minValue = cfs.getY();
		    }
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
			if (x >= fs.getX() && x <= fs.getX() + stepSize)
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
     * Returns signal minimum value
     * @return
     */
    public double getMinValue()
    {
	return minValue;
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
