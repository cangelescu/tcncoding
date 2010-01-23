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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.util.Vector;
import javax.swing.JPanel;

public class DataVizualizator extends JPanel {
    private Vector<DataVizualizatorProvider> chart_data;

    private Paint paint;

    private String l_x, l_y;

    public DataVizualizator(Vector<DataVizualizatorProvider> data, int wx, int wy, String legend_x, String legend_y)
    {
	this.setSize(wx, wy);
	this.chart_data = data;
	this.l_x = legend_x;
	this.l_y = legend_y;
    }

    @Override
    public void paintComponent(Graphics g)
    {
	//initializes graphics context
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;
	g2.setPaint(paint);

	//chart margins
	final int left_margin_x = 7 * g2.getFontMetrics().charWidth('0');
	final int right_margin_x = 10;
	final int top_margin_y = 10;
	final int bottom_margin_y = 10;
	final int x_border = 10;
	final int y_border = 10;
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
	//0
	g2.drawString("0", zero_x - (int)(1.5 * g2.getFontMetrics().charWidth('0')), zero_y + g2.getFontMetrics().getHeight() / 3);
	
	g2.setColor(Color.BLUE);
	//legend x
	g2.drawString(l_x, this.getWidth() - right_margin_x, zero_y + g2.getFontMetrics().getHeight());
	//legend y
	g2.drawString(l_y, zero_x - g2.getFontMetrics().stringWidth(l_y) - g2.getFontMetrics().charWidth('0') / 2, top_margin_y);
	g2.setColor(Color.BLACK);

	//gets number of step records
	double one_piece = this.chart_data.elementAt(0).getEnd();
	double total_time = this.chart_data.size() * one_piece;
	//finds base function values
	double max_y = this.chart_data.elementAt(0).getMaxValue();
	for (DataVizualizatorProvider cs: this.chart_data)
	    if (cs.getMaxValue() > max_y)
		max_y = cs.getMaxValue();
	String max_y_string = String.format("%1.2f", max_y);

	//draw steps y
	g2.drawLine(zero_x - g2.getFontMetrics().charWidth('0') / 2, top_margin_y + y_border, zero_x + g2.getFontMetrics().charWidth('0') / 2, top_margin_y + y_border);
	g2.drawString(max_y_string, zero_x - g2.getFontMetrics().stringWidth(max_y_string) - g2.getFontMetrics().charWidth('0') / 2, top_margin_y + y_border + g2.getFontMetrics().getHeight() / 3);

	//chart scaling factor
	double scaling_factor = (zero_y - top_margin_y - y_border) / max_y;

	//longtitude of chart
	int distance = this.getWidth() - (left_margin_x + right_margin_x + x_border);

	//step of drawing
	double step = total_time / (double)distance;

	//draw chart
	double current_time = 0;
	int index = 0;
	while (current_time <= total_time)
	{
	    if (current_time > one_piece * (index + 1))
		index++;
	    int new_x = current_x + 1;
	    double y_value = this.chart_data.elementAt(index).getFunction(current_time);
	    int new_y;
	    if (y_value == 0)
		new_y = zero_y;
	    else
		new_y = (int) (zero_y - scaling_factor * y_value);
	    g2.drawLine(current_x, current_y, new_x, new_y);
	    current_x = new_x;
	    current_y = new_y;
	    current_time += step;
	}
	g2.drawLine(current_x, current_y, current_x + 1, zero_y);
    }
}
