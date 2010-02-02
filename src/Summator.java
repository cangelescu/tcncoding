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
    private Vector<Vector<FunctionStep>> sequence0;
    private Vector<Vector<FunctionStep>> sequence1;
    private Vector<Vector<FunctionStep>> sum_result = new Vector<Vector<FunctionStep>>();

    public Summator(Vector<Vector<FunctionStep>> new_sequence0, Vector<Vector<FunctionStep>> new_sequence1)
    {
	this.sequence0 = new_sequence0;
	this.sequence1 = new_sequence1;
    }

    public void doSumming()
    {
	this.sum_result.clear();
	for (int i = 0; i < this.sequence0.size(); i++)
	{
	    Vector<FunctionStep> s0 = this.sequence0.elementAt(i);
	    Vector<FunctionStep> s1 = this.sequence1.elementAt(i);
	    Vector<FunctionStep> new_sum = new Vector<FunctionStep>();
	    for (int k = 0; k < s0.size(); k++)
	    {
		FunctionStep ss0 = s0.elementAt(k);
		FunctionStep ss1 = s1.elementAt(k);
		new_sum.add(new FunctionStep(ss1.getX(), ss1.getY() - ss0.getY()));
	    }
	    sum_result.add(new_sum);
	}
    }

    public Vector<Vector<FunctionStep>> getSum()
    {
	return this.sum_result;
    }
}
