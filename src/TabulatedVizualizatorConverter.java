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
 * Converts tabulated function values into vizualizator-friendly form
 * @author post-factum
 */
public class TabulatedVizualizatorConverter
{
    private List<List<List<FunctionStep>>> data;
    private String description;
    private Color chartColor;
    private double stepSize;

    /**
     * Creates converter for data vizualizator
     * @param _data input list of signals
     * @param _stepSize size of tabulating step
     * @param _description description of signal
     * @param _chartColor color of vizualized chart
     */

    public TabulatedVizualizatorConverter(List<List<List<FunctionStep>>> _data, double _stepSize, String _description, Color _chartColor)
    {
	data = _data;
	description = _description;
	chartColor = _chartColor;
	stepSize = _stepSize;
    }

    /**
     * Returns list of provided data
     * @return list of vizualizator-friendly data
     */
    public List<DataVizualizatorProvider> getProvided()
    {
	List<DataVizualizatorProvider> out = new ArrayList<DataVizualizatorProvider>();
	for (List<List<FunctionStep>> cllfs: data)
	    out.add(new DataVizualizatorProvider(cllfs, stepSize, DataVizualizatorProvider.SignalType.TABULATED, description, chartColor));
	return out;
    }
}
