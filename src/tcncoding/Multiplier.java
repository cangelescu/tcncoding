package tcncoding;

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
 * Model of multiplier device
 * @author post-factum
 */
public class Multiplier
{
    private List<List<ChannelSignal>> channelSignals;
    private List<List<ModulatorSignal>> ethalonSignals;
    private List<List<MultiplierSignal>> output = new ArrayList<List<MultiplierSignal>>();

    /**
     * Creates multiplier
     * @param _channelSignals list of channel signals
     * @param _ethalonSignals list of ethalon signals
     */
    public Multiplier(List<List<ChannelSignal>> _channelSignals, List<List<ModulatorSignal>> _ethalonSignals)
    {
	channelSignals = _channelSignals;
	ethalonSignals = _ethalonSignals;
    }

    /**
     * Runs multiplying
     */
    public void doMultiply()
    {
	for (int i = 0; i < channelSignals.size(); i++)
	{
	    List<MultiplierSignal> newMultiplierSignals = new ArrayList<MultiplierSignal>();
	    for (int j = 0; j < channelSignals.get(i).size(); j++)
	    {
		MultiplierSignal newSignal = new MultiplierSignal(channelSignals.get(i).get(j), ethalonSignals.get(i).get(j));
		newMultiplierSignals.add(newSignal);
	    }
	    output.add(newMultiplierSignals);
	}
    }

    /**
     * Returns list of multiplied signals
     * @return list of multiplied signals
     */
    public List<List<MultiplierSignal>> getSignals()
    {
	return output;
    }
}
