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
import java.util.ArrayList;
import java.util.List;

/**
 * Converts digital function values into vizualizator-friendly form
 * @author Oleksandr Natalenko aka post-factum
 */
public class DigitalVizualizatorConverter
{
    private List<List<DigitalSignal>> data;
    private String description;
    private Color chartColor;

    /**
     * Creates converter for data vizualizator
     * @param _data input list of signals
     * @param _description description of signal
     * @param _chartColor color of vizualized chart
     */

    public DigitalVizualizatorConverter(List<List<DigitalSignal>> _data, String _description, Color _chartColor)
    {
	data = _data;
	description = _description;
	chartColor = _chartColor;
    }

    /**
     * Returns list of provided data
     * @return list of vizualizator-friendly data
     */
    public List<DataVizualizatorProvider> getProvided()
    {
	List<DataVizualizatorProvider> out = new ArrayList<DataVizualizatorProvider>();
	for (List<DigitalSignal> clds: data)
	    out.add(new DataVizualizatorProvider(clds, Signal.SignalType.TABULATED, description, chartColor));
	return out;
    }
}
