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
import java.util.List;
import javax.swing.JPanel;

public class DataVizualizator extends JPanel {
    private List<List<DataVizualizatorProvider>> chartData;

    private Paint paint;

    private String lX, lY;

    public DataVizualizator(List<List<DataVizualizatorProvider>> data, int wx, int wy, String legendX, String legendY)
    {
	this.setSize(wx, wy);
	this.chartData = data;
	this.lX = legendX;
	this.lY = legendY;
    }

    @Override
    public void paintComponent(Graphics g)
    {
	//initializes graphics context
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;
	g2.setPaint(paint);

	//chart margins
	final int leftMarginX = 8 * g2.getFontMetrics().charWidth('0');
	final int rightMarginX = 10;
	final int topMarginY = 10;
	final int bottomMarginY = 10;
	final int xBorder = 10;
	final int yBorder = 10;
	//arrows size
	final int arrowWidth = 2;
	final int arrowHeight = 7;

	//zero levels
	final int zeroX = leftMarginX;
	final int zeroY = this.getHeight() / 2;

	//chart index
	int chartIndex = 0;

	//draw coordinates system
	g2.drawLine(leftMarginX, zeroY, this.getWidth() - rightMarginX + xBorder, zeroY);
	g2.drawLine(leftMarginX, topMarginY, leftMarginX, this.getHeight() - bottomMarginY);
	//0y arrow
	g2.drawLine(leftMarginX, topMarginY, leftMarginX - arrowWidth, topMarginY + arrowHeight);
	g2.drawLine(leftMarginX, topMarginY, leftMarginX + arrowWidth, topMarginY + arrowHeight);
	//0x arrow
	g2.drawLine(this.getWidth() - rightMarginX + xBorder, zeroY, this.getWidth() - rightMarginX - arrowHeight + xBorder, zeroY - arrowWidth);
	g2.drawLine(this.getWidth() - rightMarginX + xBorder, zeroY, this.getWidth() - rightMarginX - arrowHeight + xBorder, zeroY + arrowWidth);
	//0
	g2.drawString("0", zeroX - (int)(1.5 * g2.getFontMetrics().charWidth('0')), zeroY + g2.getFontMetrics().getHeight() / 3);
	
	g2.setColor(Color.BLUE);
	//legend x
	g2.drawString(lX, this.getWidth() - rightMarginX, zeroY + g2.getFontMetrics().getHeight());
	//legend y
	g2.drawString(lY, zeroX - g2.getFontMetrics().stringWidth(lY) - g2.getFontMetrics().charWidth('0') / 2, topMarginY);
	//find max value
	double maxY = 0;
	for (List<DataVizualizatorProvider> cldvp: this.chartData)
	{
	    maxY = cldvp.get(0).getMaxValue();
	    for (DataVizualizatorProvider cs: cldvp)
		if (cs.getMaxValue() > maxY)
		    maxY = cs.getMaxValue();
	}
	String maxYString = String.format("%1.2f", maxY);
	//chart scaling factor
	double scalingFactor = (zeroY - topMarginY - yBorder) / maxY;
	//longtitude of chart
	int distance = this.getWidth() - (leftMarginX + rightMarginX + xBorder);
	//draw steps y
	g2.setColor(Color.BLACK);
	g2.drawLine(zeroX - g2.getFontMetrics().charWidth('0') / 2, topMarginY + yBorder, zeroX + g2.getFontMetrics().charWidth('0') / 2, topMarginY + yBorder);
	g2.drawString(maxYString, zeroX - g2.getFontMetrics().stringWidth(maxYString) - g2.getFontMetrics().charWidth('0') / 2, topMarginY + yBorder + g2.getFontMetrics().getHeight() / 3);
	for (List<DataVizualizatorProvider> cldvp: this.chartData)
	{
	    chartIndex++;
	    if (chartIndex == 1)
		g2.setColor(Color.BLACK);
	    else
		g2.setColor(Color.RED);
	    //current position of pen
	    int currentX = zeroX;
	    int currentY = zeroY;
	    //gets number of step records
	    double onePiece = cldvp.get(0).getEnd();
	    double totalTime = cldvp.size() * onePiece;
	    //step of drawing
	    double step = totalTime / (double)distance;
	    //draw chart
	    double currentTime = 0;
	    int index = 0;
	    while (currentTime <= totalTime)
	    {
		if (currentTime > onePiece * (index + 1))
		    index++;
		int newX = currentX + 1;
		double yValue = cldvp.get(index).getFunction(currentTime);
		int newY;
		if (yValue == 0)
		    newY = zeroY;
		else
		    newY = (int) (zeroY - scalingFactor * yValue);
		g2.drawLine(currentX, currentY, newX, newY);
		currentX = newX;
		currentY = newY;
		currentTime += step;
	    }
	    g2.drawLine(currentX, currentY, currentX + 1, zeroY);
	}
    }
}
