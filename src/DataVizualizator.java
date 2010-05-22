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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author post-factum
 */
public class DataVizualizator extends JPanel
{
    private List<List<DataVizualizatorProvider>> chartData;

    private Paint paint;

    private String lX, lY;

    /**
     * Creates vizualizator for provided data
     * @param data list of lists of provided data
     * @param wx width of output area
     * @param wy height of output area
     * @param legendX X legend
     * @param legendY Y legend
     */
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
	final int yStepsMargin = g2.getFontMetrics().charWidth('0') / 2;
	final int leftMarginX = 16 * yStepsMargin;
	final int rightMarginX = 10;
	final int topMarginY = 10;
	final int bottomMarginY = 10;
	final int xBorder = 10;
	final int yBorder = 10;
	final int topYBorder = topMarginY + yBorder;
	final int bottomYBorder = this.getHeight() - bottomMarginY;

	//longtitude of chart
	int distance = this.getWidth() - (leftMarginX + rightMarginX + xBorder);

	//arrows size
	final int arrowWidth = 2;
	final int arrowHeight = 7;

	//zero levels
	final int zeroX = leftMarginX;
	final int zeroY = this.getHeight() / 2;

	//chart index
	int chartIndex = 0;

	//find max values
	double maxY = 0;
	int maxCount = 0;
	for (List<DataVizualizatorProvider> cldvp: this.chartData)
	{
	    if (cldvp.size() > maxCount)
		maxCount = cldvp.size();
	    for (DataVizualizatorProvider cdvp: cldvp)
		if (cdvp.getMaxValue() > maxY)
		    maxY = cdvp.getMaxValue();
	}
	//chart scaling factor
	double scalingFactor = (zeroY - topMarginY - yBorder) / maxY;

	//draw grid
	g2.setColor(new Color(200, 200, 200));
	//use double type due to required accuracy
	double gridXStepSize = distance / 10;
	double gridYStepSize;
	if (maxY != 0)
	    gridYStepSize = 2 * maxY * scalingFactor / 10;
	else
	    gridYStepSize = (bottomYBorder - topYBorder) / 10;

	//X axis steps
	for (double i = zeroX + gridXStepSize; i <= zeroX + distance; i += gridXStepSize)
	    g2.drawLine((int)i, bottomYBorder, (int)i, topYBorder);

	//0
	g2.setColor(Color.BLACK);
	g2.drawString("0", zeroX - 3 * yStepsMargin, zeroY + g2.getFontMetrics().getHeight() / 3);
	g2.setColor(new Color(200, 200, 200));
	//Y axis step
	for (double i = zeroY + gridYStepSize * 5; i >= zeroY - gridYStepSize * 5 - 1; i -= gridYStepSize)
	{
	    double cdValue = (zeroY - i) / scalingFactor;
	    //omit printing zero mark due to its present
	    if (Math.abs((i - zeroY) / scalingFactor) > gridYStepSize / (2 * scalingFactor))
	    {
		g2.drawLine(zeroX + 1, (int)i, zeroX + distance, (int)i);
		String csValue = String.format("%1.2f", cdValue);
		g2.setColor(Color.BLACK);
		g2.drawString(csValue, zeroX - g2.getFontMetrics().stringWidth(csValue) - yStepsMargin, (int)i + g2.getFontMetrics().getHeight() / 3);
		g2.setColor(new Color(200, 200, 200));
	    }
	}

	g2.setColor(Color.BLACK);
	//draw coordinates system
	g2.drawLine(leftMarginX, zeroY, this.getWidth() - rightMarginX + xBorder, zeroY);
	g2.drawLine(leftMarginX, topMarginY, leftMarginX, bottomYBorder);
	//0y arrow
	g2.drawLine(leftMarginX, topMarginY, leftMarginX - arrowWidth, topMarginY + arrowHeight);
	g2.drawLine(leftMarginX, topMarginY, leftMarginX + arrowWidth, topMarginY + arrowHeight);
	//0x arrow
	g2.drawLine(this.getWidth() - rightMarginX + xBorder, zeroY, this.getWidth() - rightMarginX - arrowHeight + xBorder, zeroY - arrowWidth);
	g2.drawLine(this.getWidth() - rightMarginX + xBorder, zeroY, this.getWidth() - rightMarginX - arrowHeight + xBorder, zeroY + arrowWidth);

	//legend x
	g2.drawString(lX, this.getWidth() - rightMarginX, zeroY + g2.getFontMetrics().getHeight());
	//legend y
	g2.drawString(lY, zeroX - g2.getFontMetrics().stringWidth(lY) - yStepsMargin, topMarginY);

	//set stroke
	Stroke cStroke = new BasicStroke(2);
	g2.setStroke(cStroke);
	for (List<DataVizualizatorProvider> cldvp: this.chartData)
	{
	    chartIndex++;
	    if (chartIndex == 1)
		g2.setColor(Color.BLUE);
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
	    //remember real previous value to draw impulses properly
	    double prevYValue = 0;
	    while (currentTime <= totalTime)
	    {
		//controls, which piece of chart must be used
		if (currentTime > onePiece * (index + 1))
		    index++;
		//gets real Y value
		double yValue = cldvp.get(index).getFunction(currentTime);
		//calculates scaled Y value
		int newY;
		//have to do this because of chart jumping
		if (yValue == 0)
		    newY = zeroY;
		else
		    newY = (int) (zeroY - scalingFactor * yValue);
		//if it is impulse, draw its front straightly
		if (prevYValue * yValue == 0)
		    g2.drawLine(currentX, currentY, currentX++, newY);
		else
		    g2.drawLine(currentX, currentY, ++currentX, newY);
		//sets new step
		currentY = newY;
		currentTime += step;
		//remember old Y real value
		prevYValue = yValue;
	    }
	    //draw line to zero level at the end
	    g2.drawLine(currentX, currentY, currentX, zeroY);
	}
    }
}
