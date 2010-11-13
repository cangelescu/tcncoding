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
 * Digital signal model
 * @author post-factum
 */
public class DigitalSignal {

    private List<Sample> data;
    private double signalEnd = 0;

    /**
     * Creates digital signal class from the list of samples
     * @param _data
     */
    public DigitalSignal(List<Sample> _data)
    {
	data = _data;
    }

    /**
     * Returns function value in _x point
     * @param _x x variable of digital function
     * @return double representation of function value in _x point
     */
    public double function(double _x)
    {
	double out = 0;
	boolean found = false;
	for (Sample cfs: data)
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

    /**
     * Returns sample with given index
     * @param _index integer index of sample in the sequence
     * @return sample with given index
     */
    public Sample getSample(int _index)
    {
	return data.get(_index);
    }

    /**
     * Returns maximum value of digital signal
     * @return double representation of maximum value of digital signal
     */
    public double getMaxValue()
    {
	double max = data.get(0).getY();
	for (int i = 1; i < data.size() - 1; i++)
	    if (data.get(i).getY() > max)
		max = data.get(i).getY();
	return max;
    }
    
    /**
     * Returns minimum value of digital signal
     * @return double representation of minimum value of digital signal
     */
    public double getMinValue()
    {
	double min = data.get(0).getY();
	for (int i = 1; i < data.size() - 1; i++)
	    if (data.get(i).getY() < min)
		min = data.get(i).getY();
	return min;
    }

    /**
     * Returns start point of the signal
     * @return double representation of signal's start point
     */
    public double getStart()
    {
	return data.get(0).getX();
    }

    /**
     * Returns end point of the signal
     * @return double representation of signal's end point
     */
    public double getEnd()
    {
	if (signalEnd == 0)
	    return data.get(data.size() - 1).getX();
	else
	    return signalEnd;
    }

    /**
     * Force setting of signal's end point. Used for impulse signals
     * @param _end double value of end point
     */
    public void setEnd(double _end)
    {
	signalEnd = _end;
    }

    /**
     * Returns the difference between two neighbour samples
     * @return double representation of delta value
     */
    public double getDelta()
    {
	if (data.size() > 1)
	    return data.get(1).getX() - data.get(0).getX();
	else
	    return 0;
    }

    /**
     * Returns count of samples in digital signals
     * @return double representation of samples' count
     */
    public int getSamplesCount()
    {
	return data.size();
    }
}
