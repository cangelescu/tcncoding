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

import java.util.Vector;

public class Modulator {

    public enum ModulationType {AMn, FMn, PMn, RPMn};

    private ModulationType using_method = null;
    private Vector sequence = null;
    private int alignment;
    private double bearer_amplitude;
    private double bearer_frequency_0, bearer_frequency_1;

    Vector<Signal> modulated_sequence = new Vector<Signal>();

    public Modulator(ModulationType mod_type, double bearerAmplitude, double bearerFrequency0, double bearerFrequency1, Vector symbols, int align)
    {
	this.using_method = mod_type;
	this.sequence = symbols;
	this.alignment = align;
	this.bearer_amplitude = bearerAmplitude;
	this.bearer_frequency_0 = bearerFrequency0;
	this.bearer_frequency_1 = bearerFrequency1;
    }

    public void doModulation()
    {
	this.modulated_sequence.clear();

	int len = this.alignment;

	int prev_phase = 1;

	SignalFunction amn0 = new SignalFunction(0, 0, 0, 0);
	SignalFunction amn1 = new SignalFunction(bearer_frequency_1, bearer_amplitude, 0, 0);
	SignalFunction fmn0 = new SignalFunction(bearer_frequency_0, bearer_amplitude, 0, 0);
	SignalFunction fmn1 = new SignalFunction(bearer_frequency_1, bearer_amplitude, 0, 0);
	SignalFunction pmn0 = new SignalFunction(bearer_frequency_1, bearer_amplitude, 0, 0);
	SignalFunction pmn1 = new SignalFunction(bearer_frequency_1, bearer_amplitude, -Math.PI, 0);

	for (int j = 0; j < this.sequence.size(); j++)
	{
	    BinaryNumber working_number = (BinaryNumber)this.sequence.get(j);
	    boolean[] seq = working_number.toBinaryArray(this.alignment);

	    for (int i = 0; i < len; i++)
	    {
		switch (this.using_method)
		{
		    case AMn:
			if (!seq[i])
			    this.modulated_sequence.add(new Signal(amn0, 0, 1/bearer_frequency_1));
			else
			    this.modulated_sequence.add(new Signal(amn1, 0, 1/bearer_frequency_1));
			break;
		    case FMn:
			if (!seq[i])
			    this.modulated_sequence.add(new Signal(fmn0, 0, 1/bearer_frequency_0));
			else
			    this.modulated_sequence.add(new Signal(fmn1, 0, 1/bearer_frequency_0));
			break;
		    case PMn:
			if (!seq[i])
			    this.modulated_sequence.add(new Signal(pmn0, 0, 1/bearer_frequency_1));
			else
			    this.modulated_sequence.add(new Signal(pmn1, 0, 1/bearer_frequency_1));
			break;
		    case RPMn:
			if (!seq[i])
			{
			    if (prev_phase == 1)
			    {
				this.modulated_sequence.add(new Signal(pmn0, 0, 1/bearer_frequency_1));
				prev_phase = 1;
			    } else
			    if (prev_phase == -1)
			    {
				this.modulated_sequence.add(new Signal(pmn1, 0, 1/bearer_frequency_1));
				prev_phase = -1;
			    }
			} else
			{
			    if (prev_phase == 1)
			    {
				this.modulated_sequence.add(new Signal(pmn1, 0, 1/bearer_frequency_1));
				prev_phase = -1;
			    } else
			    if (prev_phase == -1)
			    {
				this.modulated_sequence.add(new Signal(pmn0, 0, 1/bearer_frequency_1));
				prev_phase = 1;
			    }
			}
			break;
		    default:
			break;
		}
	    }
	}
    }

    public Vector<Signal> getSignals()
    {
	return this.modulated_sequence;
    }
}
