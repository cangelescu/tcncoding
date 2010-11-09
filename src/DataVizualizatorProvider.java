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
 * Provides vizualizer with data
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
    private List<List<FunctionStep>> tabulatedSignal = null;

    private double xStart, xEnd, maxValue, minValue, delta = 0;
    private String description;
    private Color chartColor;

    /**
     * Creates data vizualizator provider to unify all types of signals
     * @param _data input list of signals
     * @param _stepSize size of step for tabulated functions
     * @param _signalType type of signals to provide to data vizualizator
     * @param _description description of signal
     * @param _chartColor color of vizualized chart
     */
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
		tabulatedSignal = _data;

		xStart = tabulatedSignal.get(0).get(0).getX();
		int lastBlock = tabulatedSignal.size() - 1;
		int lastSample = tabulatedSignal.get(lastBlock).size() - 1;

		double cur = 0, prev = 0, index = 0;
		boolean found = false;
		for (int i = 0; i < tabulatedSignal.size(); i++)
		{
		    for (int j = 0; j < tabulatedSignal.get(i).size(); j++)
		    {
			cur = tabulatedSignal.get(i).get(j).getX();
			delta = cur - prev;
			prev = cur;
			index++;
			if (index > 1)
			{
			    found = true;
			    break;
			}
		    }
		    if (found)
			break;
		}

		xEnd = tabulatedSignal.get(lastBlock).get(lastSample).getX() + delta;

		maxValue = tabulatedSignal.get(0).get(0).getY();
		minValue = tabulatedSignal.get(0).get(0).getY();
		for (List<FunctionStep> clfs: tabulatedSignal)
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
     * @param _x time variable, s
     * @return real value of signal function in x point
     */
    public double getFunction(double _x)
    {
	double out = 0;
	switch (signalType)
	{
	    case MODULATOR:
		for (ModulatorSignal cms: modulatorSignal)
		    if (_x >= cms.getStart() && _x <= cms.getEnd())
			out = cms.function(_x);
		break;
	    case CHANNEL:
		for (ChannelSignal cms: channelSignal)
		    if (_x >= cms.getStart() && _x <= cms.getEnd())
			out = cms.function(_x);
		break;
	    case MULTIPLIER:
		for (MultiplierSignal cms: multiplierSignal)
		    if (_x >= cms.getStart() && _x <= cms.getEnd())
			out = cms.function(_x);
		break;
	    case TABULATED:
		//finds maximum block size
		int size = 0;
		for (List<FunctionStep> clfs: tabulatedSignal)
		    if (clfs.size() > size)
			size = clfs.size();

		//impulse sequence
		if (size == 1)
		{
		    boolean found = false;
		    for (int i = 0; i < tabulatedSignal.size() - 1; i++)
		    {
			FunctionStep cStep = tabulatedSignal.get(i).get(0);
			FunctionStep nStep = tabulatedSignal.get(i + 1).get(0);
			if (_x >= cStep.getX() && _x < nStep.getX())
			{
			    out = cStep.getY();
			    found = true;
			    break;
			}
		    }
		    //should be the last bit in the sequence
		    if (!found)
		    {
			FunctionStep lastStep = tabulatedSignal.get(tabulatedSignal.size() - 1).get(0);
			if (_x >= lastStep.getX() && _x <= lastStep.getX() + delta)
			    out = lastStep.getY();
		    }
		} else
		//other sequence
		{
		    boolean found = false;
		    for (int i = 0; i < tabulatedSignal.size(); i++)
		    {
			List<FunctionStep> cBlock = tabulatedSignal.get(i);
			for (int j = 0; j < cBlock.size(); j++)
			{
			    FunctionStep cStep = cBlock.get(j);
			    if (cStep.getX() >= _x)
			    {
				out = cStep.getY();
				found = true;
				break;
			    }
			}
			if (found)
			    break;
		    }
		}
		break;
	    default:
		break;
	}
	return out;
    }

    /**
     * Returns signal start
     * @return real value of time, when signal starts
     */
    public double getStart()
    {
	return xStart;
    }

    /**
     * Returns signal end
     * @return real value of time, when signal ends
     */
    public double getEnd()
    {
	return xEnd;
    }

    /**
     * Returns signal maximum value
     * @return real maximum signal value
     */
    public double getMaxValue()
    {
	return maxValue;
    }

    /**
     * Returns signal minimum value
     * @return real minimum signal value
     */
    public double getMinValue()
    {
	return minValue;
    }

    /**
     * Returns signal description
     * @return string representation of signal description
     */
    public String getDescription()
    {
	return description;
    }

    /**
     * Returns signal chart color
     * @return color of visual signal representation
     */
    public Color getChartColor()
    {
	return chartColor;
    }
}
