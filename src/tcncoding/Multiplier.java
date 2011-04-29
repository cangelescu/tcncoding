/*

 Copyright (C) 2009-2011 Oleksandr Natalenko aka post-factum

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

package tcncoding;

import java.util.ArrayList;
import java.util.List;

/**
 * Model of multiplier device
 * @author Oleksandr Natalenko aka post-factum
 */
public class Multiplier
{
    private List<List<ChannelSignal>> channelSignals;
    private List<List<ModulatorSignal>> referenceSignals;
    private List<List<MultiplierSignal>> output = new ArrayList<List<MultiplierSignal>>();

    /**
     * Creates multiplier
     * @param _channelSignals list of channel signals
     * @param _referenceSignals list of reference signals
     */
    public Multiplier(List<List<ChannelSignal>> _channelSignals, List<List<ModulatorSignal>> _referenceSignals)
    {
	channelSignals = _channelSignals;
	referenceSignals = _referenceSignals;
    }

    /**
     * Runs multiplying
     * @return multiplied sequence
     */
    public List<List<MultiplierSignal>> getSignals()
    {
	for (int i = 0; i < channelSignals.size(); i++)
	{
	    List<MultiplierSignal> newMultiplierSignals = new ArrayList<MultiplierSignal>();
	    for (int j = 0; j < channelSignals.get(i).size(); j++)
	    {
		MultiplierSignal newSignal = new MultiplierSignal(channelSignals.get(i).get(j), referenceSignals.get(i).get(j));
		newMultiplierSignals.add(newSignal);
	    }
	    output.add(newMultiplierSignals);
	}
        return output;
    }
}
