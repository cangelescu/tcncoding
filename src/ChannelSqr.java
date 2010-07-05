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
public class ChannelSqr
{
    private List<List<ModulatorSignal>> inputSignals;
    private List<List<ChannelSignalSqr>> outputSignals = new ArrayList<List<ChannelSignalSqr>>();
    private double noisePower;

    /**
     * Creates list of channel signals^2
     * @param _inputSignals list of input signals
     * @param _noisePower power of noise, W
     */
    public ChannelSqr(List<List<ModulatorSignal>> _inputSignals, double _noisePower)
    {
	inputSignals = _inputSignals;
	noisePower = _noisePower;
    }

    /**
     * Runs noising
     */
    public void doNoising()
    {
	for(List<ModulatorSignal> clms: inputSignals)
	{
	    List<ChannelSignalSqr> newChannelSignalsList = new ArrayList<ChannelSignalSqr>();
	    for (ModulatorSignal cms: clms)
	    {
		ChannelSignalSqr ncfs = new ChannelSignalSqr(cms.getFrequency(), cms.getAmplitude(), cms.getPhase(), noisePower, cms.getStart(), cms.getEnd());
		newChannelSignalsList.add(ncfs);
	    }
	    outputSignals.add(newChannelSignalsList);
	}
    }

    /**
     * Returns list of signals
     * @return
     */
    public List<List<ChannelSignalSqr>> getSignals()
    {
	return outputSignals;
    }
}
