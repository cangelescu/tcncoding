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

import java.util.Vector;

public class DataVizualizatorProvider {

    public enum SignalType {modulator, channel, multiplier, tabulated};
    private SignalType current_signal_type;

    private ModulatorSignal modulator_signal = null;
    private ChannelSignal channel_signal = null;
    private MultiplierSignal multiplier_signal = null;
    private Vector<FunctionStep> integrator_signal = null;

    private double x_start, x_end, max_value;

    public DataVizualizatorProvider(ModulatorSignal mos_data)
    {
	this.current_signal_type = SignalType.modulator;
	this.modulator_signal = mos_data;
	this.x_start = mos_data.getStart();
	this.x_end = mos_data.getEnd();
	this.max_value = mos_data.getMaxValue();
    }

    public DataVizualizatorProvider(ChannelSignal cas_data)
    {
	this.current_signal_type = SignalType.channel;
	this.channel_signal = cas_data;
	this.x_start = cas_data.getStart();
	this.x_end = cas_data.getEnd();
	this.max_value = cas_data.getMaxValue();
    }

    public DataVizualizatorProvider(MultiplierSignal mus_data)
    {
	this.current_signal_type = SignalType.multiplier;
	this.multiplier_signal = mus_data;
	this.x_start = mus_data.getStart();
	this.x_end = mus_data.getEnd();
	this.max_value = mus_data.getMaxValue();
    }

    public DataVizualizatorProvider(Vector<FunctionStep> ios_data)
    {
	this.current_signal_type = SignalType.tabulated;
	this.integrator_signal = ios_data;
	this.x_start = ios_data.firstElement().getX();
	this.x_end = ios_data.lastElement().getX();

	this.max_value = ios_data.firstElement().getY();
	for (FunctionStep fs: ios_data)
	    if (fs.getY() > this.max_value)
		this.max_value = fs.getY();
    }

    public double getFunction(double x)
    {
	double out;
	switch (this.current_signal_type)
	{
	    case modulator:
		out = this.modulator_signal.function(x);
		break;
	    case channel:
		out = this.channel_signal.function(x);
		break;
	    case multiplier:
		out = this.multiplier_signal.function(x);
		break;
	    case tabulated:
		double found = 0;
		for (FunctionStep fs: this.integrator_signal)
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

    public double getStart()
    {
	return this.x_start;
    }

    public double getEnd()
    {
	return this.x_end;
    }

    public double getMaxValue()
    {
	return this.max_value;
    }
}
