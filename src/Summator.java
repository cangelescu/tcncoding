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

class Summator {
    private List<List<FunctionStep>> sequence0;
    private List<List<FunctionStep>> sequence1;
    private List<List<FunctionStep>> sum_result = new ArrayList<List<FunctionStep>>();

    public Summator(List<List<FunctionStep>> new_sequence0, List<List<FunctionStep>> new_sequence1)
    {
	this.sequence0 = new_sequence0;
	this.sequence1 = new_sequence1;
    }

    public void doSumming()
    {
	this.sum_result.clear();
	for (int i = 0; i < this.sequence0.size(); i++)
	{
	    List<FunctionStep> s0 = this.sequence0.get(i);
	    List<FunctionStep> s1 = this.sequence1.get(i);
	    List<FunctionStep> new_sum = new ArrayList<FunctionStep>();
	    for (int k = 0; k < s0.size(); k++)
	    {
		FunctionStep ss0 = s0.get(k);
		FunctionStep ss1 = s1.get(k);
		new_sum.add(new FunctionStep(ss1.getX(), ss1.getY() - ss0.getY()));
	    }
	    sum_result.add(new_sum);
	}
    }

    public List<List<FunctionStep>> getSum()
    {
	return this.sum_result;
    }
}
