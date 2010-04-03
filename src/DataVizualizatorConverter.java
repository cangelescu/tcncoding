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

import java.util.ArrayList;
import java.util.List;

public class DataVizualizatorConverter {
    private DataVizualizatorProvider.SignalType signalType;
    private List data;

    public DataVizualizatorConverter(List vdata, DataVizualizatorProvider.SignalType vSignalType)
    {
	this.signalType = vSignalType;
	this.data = vdata;
    }

    public List<DataVizualizatorProvider> getProvided()
    {
	List<DataVizualizatorProvider> out = new ArrayList<DataVizualizatorProvider>();
	switch (this.signalType)
	{
	    case MODULATOR:
		for (Object co: this.data)
		{
		    ModulatorSignal ms = (ModulatorSignal)co;
		    out.add(new DataVizualizatorProvider(ms));
		}
		break;
	    case CHANNEL:
		for (Object co: this.data)
		{
		    ChannelSignal cs = (ChannelSignal)co;
		    out.add(new DataVizualizatorProvider(cs));
		}
		break;
	    case MULTIPLIER:
		for (Object co: this.data)
		{
		    MultiplierSignal ms = (MultiplierSignal)co;
		    out.add(new DataVizualizatorProvider(ms));
		}
		break;
	    case TABULATED:
		for (Object co: this.data)
		{
		    List<FunctionStep> is = (List<FunctionStep>)co;
		    out.add(new DataVizualizatorProvider(is));
		}
		break;
	    default:
		break;
	}
	return out;
    }
}
