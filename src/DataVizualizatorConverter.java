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
public class DataVizualizatorConverter
{
    private DataVizualizatorProvider.SignalType signalType;
    private List data;
    private String description;
    private Color chartColor;

    /**
     * Creates converter for data vizualizator
     * @param _data input list of signals
     * @param _signalType type of input signals
     */
    public DataVizualizatorConverter(List _data, DataVizualizatorProvider.SignalType _signalType, String _description, Color _chartColor)
    {
	signalType = _signalType;
	data = _data;
	description = _description;
	chartColor = _chartColor;
    }

    /**
     * Returns list of provided data
     * @return
     */
    public List<DataVizualizatorProvider> getProvided()
    {
	List<DataVizualizatorProvider> out = new ArrayList<DataVizualizatorProvider>();
	switch (signalType)
	{
	    case MODULATOR:
		for (Object co: data)
		{
		    ModulatorSignal ms = (ModulatorSignal)co;
		    out.add(new DataVizualizatorProvider(ms, description, chartColor));
		}
		break;
	    case CHANNEL:
		for (Object co: data)
		{
		    ChannelSignal cs = (ChannelSignal)co;
		    out.add(new DataVizualizatorProvider(cs, description, chartColor));
		}
		break;
	    case MULTIPLIER:
		for (Object co: data)
		{
		    MultiplierSignal ms = (MultiplierSignal)co;
		    out.add(new DataVizualizatorProvider(ms, description, chartColor));
		}
		break;
	    case TABULATED:
		for (Object co: data)
		{
		    List<FunctionStep> is = (List<FunctionStep>)co;
		    out.add(new DataVizualizatorProvider(is, description, chartColor));
		}
		break;
	    default:
		break;
	}
	return out;
    }
}
