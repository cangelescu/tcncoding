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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.util.Vector;
import javax.swing.JPanel;

public class modulator extends JPanel {

    public enum ModulationType {AMn, FMn, PMn, RPMn};

    private ModulationType using_method = null;
    private Vector sequence = null;
    private int alignment;

    private Paint paint;

    public modulator(ModulationType mod_type, Vector symbols, int align, int wx, int wy, int lx, int ly)
    {
	this.using_method = mod_type;
	this.sequence = symbols;
	this.setSize(wx, wy);
	this.setLocation(lx, ly);
	this.alignment = align;
    }

    private class BearerFunction implements MathToolsFunction
    {
	private double frequency, amplitude, phase;

	public BearerFunction(double freq, double ampl, double ph)
	{
	    this.frequency = freq;
	    this.amplitude = ampl;
	    this.phase = ph;
	}

	public double function(double x)
	{
	    return amplitude * Math.sin(2 * Math.PI * frequency * x + phase);
	}
    }

    public Vector<Vector<FunctionStep>> doModulation()
    {
	Vector<Vector<FunctionStep>> out = new Vector<Vector<FunctionStep>>();

	binaryNumber working_number = (binaryNumber)this.sequence.get(0);
	long[] seq = working_number.toIntArray(this.alignment);
	int len = this.alignment;

	mathTools mtools = new mathTools();

	double freq1 = 100E3;
	double freq2 = 200E3;
	double ampl = 30;
	int prev_phase = 1;

	BearerFunction amn0 = new BearerFunction(0, 0, 0);
	BearerFunction amn1 = new BearerFunction(freq1, ampl, 0);
	BearerFunction fmn0 = new BearerFunction(freq1, ampl, 0);
	BearerFunction fmn1 = new BearerFunction(freq2, ampl, 0);
	BearerFunction pmn0 = new BearerFunction(freq1, ampl, 0);
	BearerFunction pmn1 = new BearerFunction(freq1, ampl, -Math.PI);

	for(int i = 0; i < len; i++)
	{
	    switch (this.using_method)
	    {
		case AMn:
		    if (seq[i] == 0)
		    {
			out.add(mtools.tabulate(amn0, 0, 1/freq1));
		    } else
		    if (seq[i] == 1)
		    {
			out.add(mtools.tabulate(amn1, 0, 1/freq1));
		    }
		    break;
		case FMn:
		    if (seq[i] == 0)
		    {
			out.add(mtools.tabulate(fmn0, 0, 1/freq1));
		    } else
		    if (seq[i] == 1)
		    {
			out.add(mtools.tabulate(fmn1, 0, 1/freq1));
		    }
		    break;
		case PMn:
		    if (seq[i] == 0)
		    {
			out.add(mtools.tabulate(pmn0, 0, 1/freq1));
		    } else
		    if (seq[i] == 1)
		    {
			out.add(mtools.tabulate(pmn1, 0, 1/freq1));
		    }
		    break;
		case RPMn:
		    if (seq[i] == 0)
		    {
			if (prev_phase == 1)
			{
			    out.add(mtools.tabulate(pmn0, 0, 1/freq1));
			    prev_phase = 1;
			} else
			if (prev_phase == -1)
			{
			    out.add(mtools.tabulate(pmn1, 0, 1/freq1));
			    prev_phase = -1;
			}
		    } else
		    if (seq[i] == 1)
		    {
			if (prev_phase == 1)
			{
			    out.add(mtools.tabulate(pmn1, 0, 1/freq1));
			    prev_phase = -1;
			} else
			if (prev_phase == -1)
			{
			    out.add(mtools.tabulate(pmn0, 0, 1/freq1));
			    prev_phase = 1;
			}
		    }
		    break;
		default:
		    break;
	    }
	}
	return out;
    }

    @Override
    public void paintComponent(Graphics g)
    {
	//initializes graphics context
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;
	g2.setPaint(paint);

	//chart margins
	final int left_margin_x = 10;
	final int right_margin_x = 10;
	final int top_margin_y = 10;
	final int bottom_margin_y = 10;
	final int scaling_factor = 50;
	final int x_border = 5;
	//arrows size
	final int arrow_width = 2;
	final int arrow_height = 7;

	//zero levels
	final int zero_x = left_margin_x;
	final int zero_y = this.getHeight() / 2;

	//current position of pen
	int current_x = zero_x;
	int current_y = zero_y;

	//draw coordinates system
	g2.drawLine(left_margin_x, zero_y, this.getWidth() - right_margin_x + x_border, zero_y);
	g2.drawLine(left_margin_x, top_margin_y, left_margin_x, this.getHeight() - bottom_margin_y);
	//0y arrow
	g2.drawLine(left_margin_x, top_margin_y, left_margin_x - arrow_width, top_margin_y + arrow_height);
	g2.drawLine(left_margin_x, top_margin_y, left_margin_x + arrow_width, top_margin_y + arrow_height);
	//0x arrow
	g2.drawLine(this.getWidth() - right_margin_x + x_border, zero_y, this.getWidth() - right_margin_x - arrow_height + x_border, zero_y - arrow_width);
	g2.drawLine(this.getWidth() - right_margin_x + x_border, zero_y, this.getWidth() - right_margin_x - arrow_height + x_border, zero_y + arrow_width);

	//longtitude of chart
	int distance = this.getWidth() - (left_margin_x + right_margin_x);

	//get first symbol of sequence
	binaryNumber working_number = (binaryNumber)this.sequence.get(0);
	long[] seq = working_number.toIntArray(this.alignment);
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
			g2.drawLine(current_x, zero_y, new_x, new_y);
			current_x = new_x;
			current_y = new_y;
		    } else
		    if (seq[i] == 1)
		    {
			for (int k = 1; k <= step; k++)
			{
			    int new_x = current_x + 1;
			    int new_y = - (int) (Math.round(scaling_factor * Math.sin(2 * Math.PI * ((double)k / (double)step)))) + zero_y;
			    g2.drawLine(current_x, current_y, new_x, new_y);
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
			    g2.drawLine(current_x, current_y, new_x, new_y);
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
			    g2.drawLine(current_x, current_y, new_x, new_y);
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
			    g2.drawLine(current_x, current_y, new_x, new_y);
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
			    g2.drawLine(current_x, current_y, new_x, new_y);
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
				g2.drawLine(current_x, current_y, new_x, new_y);
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
				g2.drawLine(current_x, current_y, new_x, new_y);
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
				g2.drawLine(current_x, current_y, new_x, new_y);
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
				g2.drawLine(current_x, current_y, new_x, new_y);
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
