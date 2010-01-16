
import java.util.Random;

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

public class errorsSource {
    private double[] noise;

    public errorsSource(int sequence_size)
    {
	this.noise = new double[sequence_size];
    }

    public void generateNoise()
    {
	Random generator = new Random();
	for (int i = 0; i < this.noise.length; i++)
	    this.noise[i] = 4 * generator.nextGaussian();
    }

    public double[] getNoisedChannel(double[] modulator_data)
    {
	double[] out = new double[modulator_data.length];
	for (int i = 0; i < out.length; i++)
	    out[i] = modulator_data[i] + this.noise[i];
	return out;
    }
}
