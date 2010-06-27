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

import flanagan.integration.*;
import java.util.List;

/**
 *
 * @author post-factum
 */
public class EnergyComputator
{
    private List<ChannelSignalSqr> signals;
    private double energy = 0;

    /**
     * Creates energy computator for input signals
     * @param _signals list of input signals
     */
    public EnergyComputator(List<ChannelSignalSqr> _signals)
    {
	signals = _signals;
    }

    /**
     * Runs energy computing
     */
    public void computeEnergy()
    {
	for (ChannelSignalSqr cs: signals)
	    energy += Integration.gaussQuad(cs, cs.getStart(), cs.getEnd(), 1000);
    }

    /**
     * Returns computed value of energy, Watts
     * @return
     */
    public double getEnergy()
    {
	return energy;
    }
}
