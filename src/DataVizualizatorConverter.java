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

public class DataVizualizatorConverter {
    private DataVizualizatorProvider.SignalType signal_type;
    private Vector data;

    public DataVizualizatorConverter(Vector vdata, DataVizualizatorProvider.SignalType vsignal_type)
    {
	this.signal_type = vsignal_type;
	this.data = vdata;
    }

    public Vector<DataVizualizatorProvider> getProvided()
    {
	Vector<DataVizualizatorProvider> out = new Vector<DataVizualizatorProvider>();
	switch (this.signal_type)
	{
	    case modulator:
		for (Object co: this.data)
		{
		    ModulatorSignal ms = (ModulatorSignal)co;
		    out.add(new DataVizualizatorProvider(ms));
		}
		break;
	    case channel:
		for (Object co: this.data)
		{
		    ChannelSignal cs = (ChannelSignal)co;
		    out.add(new DataVizualizatorProvider(cs));
		}
		break;
	    case multiplier:
		for (Object co: this.data)
		{
		    MultiplierSignal ms = (MultiplierSignal)co;
		    out.add(new DataVizualizatorProvider(ms));
		}
		break;
	    case tabulated:
		for (Object co: this.data)
		{
		    Vector<FunctionStep> is = (Vector<FunctionStep>)co;
		    out.add(new DataVizualizatorProvider(is));
		}
		break;
	    default:
		break;
	}
	return out;
    }
}
