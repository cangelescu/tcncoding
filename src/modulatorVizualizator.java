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
import javax.swing.JPanel;

public class modulatorVizualizator extends JPanel {
    private double[] modulator_data;

    private Paint paint;

    public modulatorVizualizator(double[] data, int wx, int wy, int lx, int ly)
    {
	this.setSize(wx, wy);
	this.setLocation(lx, ly);
	this.modulator_data = data;
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
	final int scaling_factor = 1;
	final int x_border = 10;
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
	int distance = this.getWidth() - (left_margin_x + right_margin_x + x_border);

	//gets number of step records
	int ticks_count = this.modulator_data.length;

	//step of drawing
	double step = (double)ticks_count / (double)distance;

	double index = 0;
	while (index < ticks_count)
	{
	    int new_x = current_x + 1;
	    int new_y = scaling_factor * (int) (zero_y - this.modulator_data[(int)index]);
	    g2.drawLine(current_x, current_y, new_x, new_y);
	    current_x = new_x;
	    current_y = new_y;
	    index += step;
	}
	g2.drawLine(current_x, current_y, current_x + 1, zero_y);
    }
}
