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

import java.util.List;

/**
 *
 * @author post-factum
 */
public class DigitalSignal {

    private List<FunctionStep> data;
    private double signalEnd = 0;

    public DigitalSignal(List<FunctionStep> _data)
    {
	data = _data;
    }

    public double function(double _x)
    {
	double out = 0;
	boolean found = false;
	for (FunctionStep cfs: data)
	    if (cfs.getX() >= _x)
	    {
		out = cfs.getY();
		found = true;
		break;
	    }
	if (!found)
	    out = data.get(data.size() - 1).getY();
	return out;
    }

    public FunctionStep getSample(int _index)
    {
	return data.get(_index);
    }

    public double getMaxValue()
    {
	double max = data.get(0).getY();
	for (int i = 1; i < data.size() - 1; i++)
	    if (data.get(i).getY() > max)
		max = data.get(i).getY();
	return max;
    }
    
    public double getMinValue()
    {
	double min = data.get(0).getY();
	for (int i = 1; i < data.size() - 1; i++)
	    if (data.get(i).getY() < min)
		min = data.get(i).getY();
	return min;
    }

    public double getStart()
    {
	return data.get(0).getX();
    }

    public double getEnd()
    {
	if (signalEnd == 0)
	    return data.get(data.size() - 1).getX();
	else
	    return signalEnd;
    }

    public void setEnd(double _end)
    {
	signalEnd = _end;
    }

    public double getDelta()
    {
	if (data.size() > 1)
	    return data.get(1).getX() - data.get(0).getX();
	else
	    return 0;
    }

    public int getSamplesCount()
    {
	return data.size();
    }
}
