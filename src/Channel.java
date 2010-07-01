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
 *
 * @author post-factum
 */
public class Channel
{
    private List<ModulatorSignal> inputSignals;
    private List<ChannelSignal> outputSignals = new ArrayList<ChannelSignal>();
    private double noisePower;

    /**
     * Creates channel with given signals on its input
     * @param _inputSignals list of input signals
     */
    public Channel(List<ModulatorSignal> _inputSignals, double _noisePower)
    {
	inputSignals = _inputSignals;
	noisePower = _noisePower;
    }

    /**
     * Runs noising of channel
     */
    public void doNoising()
    {
	for(ModulatorSignal cs: inputSignals)
	{

	    ChannelSignal ncfs = new ChannelSignal(cs.getFrequency(), cs.getAmplitude(), cs.getPhase(), noisePower, cs.getStart(), cs.getEnd());
	    outputSignals.add(ncfs);
	}
    }

    /**
     * Returns list of noised signals
     * @return
     */
    public List<ChannelSignal> getSignals()
    {
	return outputSignals;
    }
}
