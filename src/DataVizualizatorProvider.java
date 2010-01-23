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

public class DataVizualizatorProvider {

    enum SignalType {modulator, channel, multiplier};
    SignalType current_signal_type;

    ModulatorSignal modulator_signal = null;
    ChannelSignal channel_signal = null;
    MultiplierSignal multiplier_signal = null;

    double x_start, x_end, frequency, amplitude, phase, max_value;

    public DataVizualizatorProvider(ModulatorSignal mos_data)
    {
	this.current_signal_type = SignalType.modulator;
	this.modulator_signal = mos_data;
	this.x_start = mos_data.getStart();
	this.x_end = mos_data.getEnd();
	this.frequency = mos_data.getFunction().getFrequency();
	this.amplitude = mos_data.getFunction().getAmplitude();
	this.phase = mos_data.getFunction().getPhase();
	this.max_value = mos_data.getFunction().getMaxValue();
    }

    public DataVizualizatorProvider(ChannelSignal cas_data)
    {
	this.current_signal_type = SignalType.channel;
	this.channel_signal = cas_data;
	this.x_start = cas_data.getStart();
	this.x_end = cas_data.getEnd();
	this.frequency = cas_data.getFunction().getFrequency();
	this.amplitude = cas_data.getFunction().getAmplitude();
	this.phase = cas_data.getFunction().getPhase();
	this.max_value = cas_data.getFunction().getMaxValue();
    }

    public DataVizualizatorProvider(MultiplierSignal mus_data)
    {
	this.current_signal_type = SignalType.multiplier;
	this.multiplier_signal = mus_data;
	this.x_start = mus_data.getStart();
	this.x_end = mus_data.getEnd();
	this.frequency = mus_data.getFunction().getFrequency();
	this.amplitude = mus_data.getFunction().getAmplitude();
	this.phase = mus_data.getFunction().getPhase();
	this.max_value = mus_data.getFunction().getMaxValue();
    }

    public double getFunction(double x)
    {
	double out;
	switch (this.current_signal_type)
	{
	    case modulator:
		out = this.modulator_signal.getFunction().function(x);
		break;
	    case channel:
		out = this.channel_signal.getFunction().function(x);
		break;
	    case multiplier:
		out = this.multiplier_signal.getFunction().function(x);
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
