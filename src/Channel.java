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

/**
 * Model of transmission channel with Gaussian noise
 * @author post-factum
 */
public class Channel
{
    private List<List<ModulatorSignal>> inputSignals;
    private List<List<ChannelSignal>> outputSignals = new ArrayList<List<ChannelSignal>>();
    private double noisePower;

    /**
     * Creates channel with given signals on its input
     * @param _inputSignals list of input signals
     * @param _noisePower power of noise, W
     */
    public Channel(List<List<ModulatorSignal>> _inputSignals, double _noisePower)
    {
	inputSignals = _inputSignals;
	noisePower = _noisePower;
    }

    /**
     * Runs noising of channel
     */
    public void doNoising()
    {
	for(List<ModulatorSignal> clms: inputSignals)
	{
	    List<ChannelSignal> newChannelSignalsList = new ArrayList<ChannelSignal>();
	    for (ModulatorSignal cms: clms)
	    {
		ChannelSignal ncfs = new ChannelSignal(cms, noisePower);
		newChannelSignalsList.add(ncfs);
	    }
	    outputSignals.add(newChannelSignalsList);
	}
    }

    /**
     * Returns list of noised signals
     * @return list of channel signals
     */
    public List<List<ChannelSignal>> getSignals()
    {
	return outputSignals;
    }
}
