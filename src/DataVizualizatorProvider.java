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

public class DataVizualizatorProvider {

    public enum SignalType {MODULATOR, CHANNEL, MULTIPLIER, TABULATED;};
    private SignalType currentSignalType;

    private ModulatorSignal modulatorSignal = null;
    private ChannelSignal channelSignal = null;
    private MultiplierSignal multiplierSignal = null;
    private List<FunctionStep> integratorSignal = null;

    private double xStart, xEnd, maxValue;

    public DataVizualizatorProvider(ModulatorSignal mosData)
    {
	this.currentSignalType = SignalType.MODULATOR;
	this.modulatorSignal = mosData;
	this.xStart = mosData.getStart();
	this.xEnd = mosData.getEnd();
	this.maxValue = mosData.getMaxValue();
    }

    public DataVizualizatorProvider(ChannelSignal casData)
    {
	this.currentSignalType = SignalType.CHANNEL;
	this.channelSignal = casData;
	this.xStart = casData.getStart();
	this.xEnd = casData.getEnd();
	this.maxValue = casData.getMaxValue();
    }

    public DataVizualizatorProvider(MultiplierSignal musData)
    {
	this.currentSignalType = SignalType.MULTIPLIER;
	this.multiplierSignal = musData;
	this.xStart = musData.getStart();
	this.xEnd = musData.getEnd();
	this.maxValue = musData.getMaxValue();
    }

    public DataVizualizatorProvider(List<FunctionStep> iosData)
    {
	this.currentSignalType = SignalType.TABULATED;
	this.integratorSignal = iosData;
	this.xStart = iosData.get(0).getX();
	this.xEnd = iosData.get(iosData.size() - 1).getX();

	this.maxValue = iosData.get(0).getY();
	for (FunctionStep fs: iosData)
	    if (fs.getY() > this.maxValue)
		this.maxValue = fs.getY();
    }

    public double getFunction(double x)
    {
	double out;
	switch (this.currentSignalType)
	{
	    case MODULATOR:
		out = this.modulatorSignal.function(x);
		break;
	    case CHANNEL:
		out = this.channelSignal.function(x);
		break;
	    case MULTIPLIER:
		out = this.multiplierSignal.function(x);
		break;
	    case TABULATED:
		double found = 0;
		for (FunctionStep fs: this.integratorSignal)
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
	return this.xStart;
    }

    public double getEnd()
    {
	return this.xEnd;
    }

    public double getMaxValue()
    {
	return this.maxValue;
    }
}
