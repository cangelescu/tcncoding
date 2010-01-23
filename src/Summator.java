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

class Summator {
    private Vector<Double> sequence0;
    private Vector<Double> sequence1;
    private Vector<Double> sum_result = new Vector<Double>();

    public Summator(Vector<Double> new_sequence0, Vector<Double> new_sequence1)
    {
	this.sequence0 = new_sequence0;
	this.sequence1 = new_sequence1;
    }

    public void doSumming()
    {
	this.sum_result.clear();
	for (int i = 0; i < this.sequence1.size(); i++)
	    this.sum_result.add(this.sequence1.elementAt(i) - this.sequence0.elementAt(i));
    }

    public Vector<Double> getSum()
    {
	return this.sum_result;
    }
}
