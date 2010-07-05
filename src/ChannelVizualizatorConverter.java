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
public class ChannelVizualizatorConverter
{
    private List<List<ChannelSignal>> data;
    private String description;
    private Color chartColor;

    /**
     * Creates converter for data vizualizator
     * @param _data input list of signals
     * @param _description description of signal
     * @param _chartColor color of vizualized chart
     */

    public ChannelVizualizatorConverter(List<List<ChannelSignal>> _data, String _description, Color _chartColor)
    {
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
	for (List<ChannelSignal> clcs: data)
	    out.add(new DataVizualizatorProvider(clcs, DataVizualizatorProvider.SignalType.CHANNEL, description, chartColor));
	return out;
    }
}
