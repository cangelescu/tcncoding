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

import java.awt.Color;
import java.util.List;

/**
 * Provides vizualizer with data
 * @author Oleksandr Natalenko aka post-factum
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
         * Signal from noise generator
         */
        NOISE,
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

    private List<ModulatorSignal> modulatorSignal;
    private List<NoiseSignal> noiseSignal;
    private List<ChannelSignal> channelSignal;
    private List<MultiplierSignal> multiplierSignal;
    private List<DigitalSignal> digitalSignal;

    private double xStart, xEnd, maxValue, minValue, delta = 0;
    private int digitalBlockSize = 0;
    private String description;
    private Color chartColor;

    /**
     * Creates data vizualizator provider to unify all types of signals
     * @param _data input list of signals
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
            case NOISE:
		noiseSignal = _data;
		xStart = noiseSignal.get(0).getStart();
		xEnd = noiseSignal.get(noiseSignal.size() - 1).getEnd();
		maxValue = noiseSignal.get(0).getMaxValue();
		minValue = noiseSignal.get(0).getMinValue();
		for (NoiseSignal cns: noiseSignal)
		{
		    if (cns.getMaxValue() > maxValue)
			maxValue = cns.getMaxValue();
		    if (cns.getMinValue() < minValue)
			minValue = cns.getMinValue();
		}
		break;
	    case CHANNEL:
		channelSignal = _data;
		xStart = channelSignal.get(0).getStart();
		xEnd = channelSignal.get(channelSignal.size() - 1).getEnd();
		maxValue = channelSignal.get(0).getMaxValue();
		minValue = channelSignal.get(0).getMinValue();
		for (ChannelSignal cchs: channelSignal)
		{
		    if (cchs.getMaxValue() > maxValue)
			maxValue = cchs.getMaxValue();
		    if (cchs.getMinValue() < minValue)
			minValue = cchs.getMinValue();
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
		digitalSignal = _data;

		for (DigitalSignal cds: digitalSignal)
		    if (cds.getSamplesCount() > digitalBlockSize)
			digitalBlockSize = cds.getSamplesCount();

		delta = (digitalBlockSize == 1) ? digitalSignal.get(0).getDelta() : (digitalSignal.get(0).getSample(1).getX() - digitalSignal.get(0).getSample(0).getX());
		xEnd = digitalSignal.get(digitalSignal.size() - 1).getEnd() + delta;

		xStart = digitalSignal.get(0).getStart();
		maxValue = digitalSignal.get(0).getMaxValue();
		minValue = digitalSignal.get(0).getMinValue();
		for (DigitalSignal cds: digitalSignal)
		{
		    if (cds.getMaxValue() > maxValue)
			maxValue = cds.getMaxValue();
		    if (cds.getMinValue() > minValue)
			minValue = cds.getMinValue();
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
            case NOISE:
		for (NoiseSignal cns: noiseSignal)
		    if (_x >= cns.getStart() && _x <= cns.getEnd())
			out = cns.function(_x);
		break;
	    case CHANNEL:
		for (ChannelSignal cchs: channelSignal)
		    if (_x >= cchs.getStart() && _x <= cchs.getEnd())
			out = cchs.function(_x);
		break;
	    case MULTIPLIER:
		for (MultiplierSignal cms: multiplierSignal)
		    if (_x >= cms.getStart() && _x <= cms.getEnd())
			out = cms.function(_x);
		break;
	    case TABULATED:
		for (DigitalSignal cds: digitalSignal)
		    if (_x >= cds.getStart() && _x <= cds.getEnd())
			out = cds.function(_x);
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
