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

    private List<AnalogSignal> inputSignal;

    private double xStart, xEnd, maxValue, minValue;
    private String description;
    private Color chartColor;

    /**
     * Creates data vizualizator provider to unify all types of signals
     * @param _data input list of signals
     * @param _signalType type of signals to provide to data vizualizator
     * @param _description description of signal
     * @param _chartColor color of vizualized chart
     */
    public DataVizualizatorProvider(Object _data, String _description, Color _chartColor)
    {
        inputSignal = (List<AnalogSignal>)_data;
        xStart = inputSignal.get(0).getStart();
        xEnd = inputSignal.get(inputSignal.size() - 1).getEnd();
        maxValue = inputSignal.get(0).getMaxValue();
        minValue = inputSignal.get(0).getMinValue();
        for (AnalogSignal cms: inputSignal)
        {
            if (cms.getMaxValue() > maxValue)
                maxValue = cms.getMaxValue();
            if (cms.getMinValue() < minValue)
                minValue = cms.getMinValue();
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
        for (AnalogSignal cms: inputSignal)
            if (_x >= cms.getStart() && _x <= cms.getEnd())
                out = cms.function(_x);
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
