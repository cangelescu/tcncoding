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
 * Model of transmission channel with Gaussian noise
 * @author Oleksandr Natalenko aka post-factum
 */
public class Channel
{
    private List<ModulatorSignal> inputModulatorSignals;
    private List<NoiseSignal> inputNoiseSignals;

    /**
     * Creates channel with given signals on its input
     * @param _inputModulatorSignals list of input signals from modulator
     * @param _inputNoiseSignals list of input signals from noise generator
     */
    public Channel(List<ModulatorSignal> _inputModulatorSignals, List<NoiseSignal> _inputNoiseSignals)
    {
	inputModulatorSignals = _inputModulatorSignals;
        inputNoiseSignals = _inputNoiseSignals;
    }

    /**
     * Returns list of noised signals
     * @return list of channel signals
     */
    public List<ChannelSignal> getSignals()
    {
        List<ChannelSignal> outputSignals = new ArrayList<ChannelSignal>();
        for (int k = 0; k < inputModulatorSignals.size(); k++)
            outputSignals.add(new ChannelSignal(inputModulatorSignals.get(k), inputNoiseSignals.get(k)));
	return outputSignals;
    }
}
