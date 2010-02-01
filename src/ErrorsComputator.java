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

public class ErrorsComputator {
    private double signal_energy, noise_density;
    private Modulator.ModulationType modulation_type;

    public ErrorsComputator(double _signal_energy, double _noise_density, Modulator.ModulationType _modulation_type)
    {
	this.signal_energy = _signal_energy;
	this.noise_density = _noise_density;
	this.modulation_type = _modulation_type;
    }

    public double getErrorProbability()
    {
	double out = 0, arg = 0, factor = 0;
	switch (this.modulation_type)
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
	out = factor * 0.5 * (1 - Stat.erf(Math.sqrt((this.signal_energy / this.noise_density) * arg / 4)));
	return out;
    }
}
