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
    private SignalType currentSignalType;

    private ModulatorSignal modulatorSignal = null;
    private ChannelSignal channelSignal = null;
    private MultiplierSignal multiplierSignal = null;
    private List<FunctionStep> integratorSignal = null;

    private double xStart, xEnd, maxValue;
    private String description;
    private Color chartColor;

    /**
     * Creates data provider for vizualizator
     * @param _data input modulator signal
     */
    public DataVizualizatorProvider(ModulatorSignal _data, String _description, Color _chartColor)
    {
	currentSignalType = SignalType.MODULATOR;
	modulatorSignal = _data;
	xStart = _data.getStart();
	xEnd = _data.getEnd();
	maxValue = _data.getMaxValue();
	description = _description;
	chartColor = _chartColor;
    }

    /**
     * Creates data provider for vizualizator
     * @param _data input channel signal
     */
    public DataVizualizatorProvider(ChannelSignal _data, String _description, Color _chartColor)
    {
	currentSignalType = SignalType.CHANNEL;
	channelSignal = _data;
	xStart = _data.getStart();
	xEnd = _data.getEnd();
	maxValue = _data.getMaxValue();
	description = _description;
	chartColor = _chartColor;
    }

    /**
     * Creates data provider for vizualizator
     * @param _data input multiplier signal
     */
    public DataVizualizatorProvider(MultiplierSignal _data, String _description, Color _chartColor)
    {
	currentSignalType = SignalType.MULTIPLIER;
	multiplierSignal = _data;
	xStart = _data.getStart();
	xEnd = _data.getEnd();
	maxValue = _data.getMaxValue();
	description = _description;
	chartColor = _chartColor;
    }

    /**
     * Creates data provider for vizualizator
     * @param _data input tabulated signal
     */
    public DataVizualizatorProvider(List<FunctionStep> _data, String _description, Color _chartColor)
    {
	currentSignalType = SignalType.TABULATED;
	integratorSignal = _data;
	xStart = _data.get(0).getX();
	xEnd = _data.get(_data.size() - 1).getX();

	maxValue = _data.get(0).getY();
	for (FunctionStep fs: _data)
	    if (fs.getY() > maxValue)
		maxValue = fs.getY();

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
	double out;
	switch (currentSignalType)
	{
	    case MODULATOR:
		out = modulatorSignal.function(x);
		break;
	    case CHANNEL:
		out = channelSignal.function(x);
		break;
	    case MULTIPLIER:
		out = multiplierSignal.function(x);
		break;
	    case TABULATED:
		double found = 0;
		for (FunctionStep fs: integratorSignal)
		    if (fs.getX() >= x)
		    {
			found = fs.getY();
			break;
		    }
		out = found;
		break;
	    default:
		out = 0;
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
