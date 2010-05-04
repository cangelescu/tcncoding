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

import flanagan.analysis.*;

/**
 *
 * @author post-factum
 */
public class ErrorsComputator
{
    private double signalEnergy, noiseDensity;
    private Modulator.ModulationType modulationType;

    /**
     * Creates error computator for channel
     * @param _signalEnergy input signal energy
     * @param _noiseDensity input noise density
     * @param _modulationType type of using modulation
     */
    public ErrorsComputator(double _signalEnergy, double _noiseDensity, Modulator.ModulationType _modulationType)
    {
	this.signalEnergy = _signalEnergy;
	this.noiseDensity = _noiseDensity;
	this.modulationType = _modulationType;
    }

    /**
     * Returns single error probability
     * @return
     */
    public double getErrorProbability()
    {
	double out = 0, arg = 0, factor = 0;
	switch (this.modulationType)
	{
	    case AMn:
		arg = 0.5;
		factor = 1;
		break;
	    case FMn:
		arg = 1;
		factor = 1;
		break;
	    case PMn:
		arg = 2;
		factor = 1;
		break;
	    case RPMn:
		arg = 2;
		factor = 2;
		break;
	    default:
		break;
	}
	out = factor * 0.5 * (1 - Stat.erf(Math.sqrt((this.signalEnergy / this.noiseDensity) * arg / 4)));
	return out;
    }
}
