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

import java.awt.Canvas;
import java.awt.Graphics;
import java.util.Vector;

public class modulator {

    public enum ModulationType {AMn, FMn, PMn, RPMn};

    private ModulationType using_method = null;
    private Graphics context = null;
    private Vector sequence = null;
    private Canvas field = null;
    private int alignment;

    public modulator(ModulationType mod_type, Canvas where_to_draw, Vector symbols, int align)
    {
	this.using_method = mod_type;
	this.context = where_to_draw.getGraphics();
	this.sequence = symbols;
	this.field = where_to_draw;
	this.alignment = align;
    }

    public void makeImage()
    {
	//chart margins
	final int left_margin_x = 10;
	final int right_margin_x = 10;
	final int top_margin_y = 10;
	final int bottom_margin_y = 10;
	final int scaling_factor = 50;

	//zero levels
	final int zero_x = left_margin_x;
	final int zero_y = this.field.getHeight() / 2;

	//current position of pen
	int current_x = zero_x;
	int current_y = zero_y;

	//draw coordinates system
	this.context.drawLine(left_margin_x, zero_y, this.field.getWidth() - right_margin_x, zero_y);
	this.context.drawLine(left_margin_x, top_margin_y, left_margin_x, this.field.getHeight() - bottom_margin_y);

	//longtitude of chart
	int distance = this.field.getWidth() - (left_margin_x + right_margin_x);

	//get first symbol of sequence
	binaryNumber working_number = (binaryNumber)this.sequence.get(0);
	long[] seq = working_number.toAlignedIntArray(this.alignment);
	int len = this.alignment;

	//longtitude of one element, in pixels
	int step = distance / len;

	//previous phase, for RPMn
	long prev_phase = 1;

	for(int i = 0; i < len; i++)
	{
	    switch (this.using_method)
	    {
		case AMn:
		    if (seq[i] == 0)
		    {
			int new_x = current_x + step;
			int new_y = zero_y;
			this.context.drawLine(current_x, zero_y, new_x, new_y);
			current_x = new_x;
			current_y = new_y;
		    } else
		    if (seq[i] == 1)
		    {
			for (int k = 1; k <= step; k++)
			{
			    int new_x = current_x + 1;
			    int new_y = - (int) (Math.round(scaling_factor * Math.sin(2 * Math.PI * ((double)k / (double)step)))) + zero_y;
			    this.context.drawLine(current_x, current_y, new_x, new_y);
			    current_x = new_x;
			    current_y = new_y;
			}
		    }
		    break;
		case FMn:
		    if (seq[i] == 0)
		    {
			for (int k = 1; k <= step; k++)
			{
			    int new_x = current_x + 1;
			    int new_y = - (int) (Math.round(scaling_factor * Math.sin(2 * Math.PI * ((double)k / (double)step)))) + zero_y;
			    this.context.drawLine(current_x, current_y, new_x, new_y);
			    current_x = new_x;
			    current_y = new_y;
			}
		    } else
		    if (seq[i] == 1)
		    {
			for (int k = 1; k <= step; k++)
			{
			    int new_x = current_x + 1;
			    int new_y = - (int) (Math.round(scaling_factor * Math.sin(2 * 2 * Math.PI * ((double)k / (double)step)))) + zero_y;
			    this.context.drawLine(current_x, current_y, new_x, new_y);
			    current_x = new_x;
			    current_y = new_y;
			}
		    }
		    break;
		case PMn:
		    if (seq[i] == 0)
		    {
			for (int k = 1; k <= step; k++)
			{
			    int new_x = current_x + 1;
			    int new_y = - (int) (Math.round(scaling_factor * Math.sin(2 * Math.PI * ((double)k / (double)step)))) + zero_y;
			    this.context.drawLine(current_x, current_y, new_x, new_y);
			    current_x = new_x;
			    current_y = new_y;
			}
		    } else
		    if (seq[i] == 1)
		    {
			for (int k = 1; k <= step; k++)
			{
			    int new_x = current_x + 1;
			    int new_y = (int) (Math.round(scaling_factor * Math.sin(2 * Math.PI * ((double)k / (double)step)))) + zero_y;
			    this.context.drawLine(current_x, current_y, new_x, new_y);
			    current_x = new_x;
			    current_y = new_y;
			}
		    }
		    break;
		case RPMn:
		    if (seq[i] == 0)
		    {
			if (prev_phase == 1)
			{
			    for (int k = 1; k <= step; k++)
			    {
				int new_x = current_x + 1;
				int new_y = - (int) (Math.round(scaling_factor * Math.sin(2 * Math.PI * ((double)k / (double)step)))) + zero_y;
				this.context.drawLine(current_x, current_y, new_x, new_y);
				current_x = new_x;
				current_y = new_y;
			    }
			    prev_phase = 1;
			} else
			if (prev_phase == -1)
			{
			    for (int k = 1; k <= step; k++)
			    {
				int new_x = current_x + 1;
				int new_y = (int) (Math.round(scaling_factor * Math.sin(2 * Math.PI * ((double)k / (double)step)))) + zero_y;
				this.context.drawLine(current_x, current_y, new_x, new_y);
				current_x = new_x;
				current_y = new_y;
			    }
			    prev_phase = -1;
			}
		    } else
		    if (seq[i] == 1)
		    {
			if (prev_phase == 1)
			{
			    for (int k = 1; k <= step; k++)
			    {
				int new_x = current_x + 1;
				int new_y = (int) (Math.round(scaling_factor * Math.sin(2 * Math.PI * ((double)k / (double)step)))) + zero_y;
				this.context.drawLine(current_x, current_y, new_x, new_y);
				current_x = new_x;
				current_y = new_y;
			    }
			    prev_phase = -1;
			} else
			if (prev_phase == -1)
			{
			    for (int k = 1; k <= step; k++)
			    {
				int new_x = current_x + 1;
				int new_y = - (int) (Math.round(scaling_factor * Math.sin(2 * Math.PI * ((double)k / (double)step)))) + zero_y;
				this.context.drawLine(current_x, current_y, new_x, new_y);
				current_x = new_x;
				current_y = new_y;
			    }
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
