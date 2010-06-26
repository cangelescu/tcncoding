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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

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
     * @param legendX X axis legend
     * @param legendY Y axis legend
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

	//popup menu with help
	JPopupMenu pMenu = new JPopupMenu();

	//grid color
	final Color gridColor = new Color(200, 200, 200);

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

	//find min/max values
	double minX = this.chartData.get(0).get(0).getStart();
	double maxX = this.chartData.get(0).get(0).getEnd();
	double maxY = this.chartData.get(0).get(0).getMaxValue();
	for (List<DataVizualizatorProvider> cldvp: this.chartData)
	    for (DataVizualizatorProvider cdvp: cldvp)
	    {
		if (cdvp.getMaxValue() > maxY)
		    maxY = cdvp.getMaxValue();
		if (cdvp.getEnd() > maxX)
		    maxX = cdvp.getEnd();
		if (cdvp.getStart() < minX)
		    minX = cdvp.getStart();
	    }
	//chart scaling factor
	double yScalingFactor = (zeroY - topMarginY - yBorder) / maxY;
	double xScalingFactor = distance / maxX;

	//draw grid
	g2.setColor(gridColor);
	//use double type due to required accuracy
	double gridXStepSize = distance / 10;
	double gridYStepSize;
	if (maxY != 0)
	    gridYStepSize = 2 * maxY * yScalingFactor / 10;
	else
	    gridYStepSize = (bottomYBorder - topYBorder + 1) / 10;

	//X axis steps
	for (double i = zeroX + gridXStepSize; i <= zeroX + distance; i += gridXStepSize)
	    g2.drawLine((int)i, bottomYBorder, (int)i, topYBorder);

	//0
	g2.setColor(Color.BLACK);
	g2.drawString("0", zeroX - 3 * yStepsMargin, zeroY + g2.getFontMetrics().getHeight() / 3);
	g2.setColor(gridColor);
	//Y axis step
	for (double i = zeroY + gridYStepSize * 5; i >= zeroY - gridYStepSize * 5 - 1; i -= gridYStepSize)
	{
	    g2.drawLine(zeroX + 1, (int)i, zeroX + distance, (int)i);
	    double cdValue = (zeroY - i) / yScalingFactor;
	    //omit printing zero mark due to its present
	    if (Math.abs((i - zeroY) / yScalingFactor) > gridYStepSize / (2 * yScalingFactor))
	    {
		String csValue = String.format("%1.3f", cdValue);
		g2.setColor(Color.BLACK);
		g2.drawString(csValue, zeroX - g2.getFontMetrics().stringWidth(csValue) - yStepsMargin, (int)i + g2.getFontMetrics().getHeight() / 3);
		g2.setColor(gridColor);
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
	g2.drawString(lX, this.getWidth() - g2.getFontMetrics().stringWidth(lX), zeroY + g2.getFontMetrics().getHeight());
	//legend y
	g2.drawString(lY, zeroX - g2.getFontMetrics().stringWidth(lY) - yStepsMargin, topMarginY);

	//set stroke
	Stroke cStroke = new BasicStroke(2);
	g2.setStroke(cStroke);
	for (List<DataVizualizatorProvider> cldvp: this.chartData)
	{
	    //add right-click description
	    JMenuItem cItem = new JMenuItem(cldvp.get(0).getDescription());
	    g2.setColor(cldvp.get(0).getChartColor());
	    cItem.setForeground(cldvp.get(0).getChartColor());
	    pMenu.add(cItem);
	    //current position of pen
	    int currentX = zeroX;
	    int currentY = zeroY;

	    //gets total time of function
	    double totalTime = 0;
	    for (DataVizualizatorProvider cdvp: cldvp)
		totalTime += cdvp.getEnd() - cdvp.getStart();
	    //step of drawing
	    double step = totalTime / (double)distance;
	    //initial time is start point of zero piece
	    double currentTime = minX;
	    //remember real previous value to draw impulses properly
	    double prevYValue = 0;
	    //draw chart
	    for (DataVizualizatorProvider cdvp: cldvp)
	    {
		while (currentTime <= cdvp.getEnd())
		{
		    //gets real Y value
		    double yValue = cdvp.getFunction(currentTime);
		    //calculates scaled Y value
		    int newY;
		    if (yValue == 0)
			newY = zeroY;
		    else
			newY = (int) (zeroY - yScalingFactor * yValue);
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
	    }
	    //draw line to zero level at the end
	    g2.drawLine(currentX, currentY, currentX, zeroY);
	}

	//X axis steps values
	NumberFormat formatter = new DecimalFormat("0.00E0");
	g2.setColor(Color.BLACK);
	for (double i = zeroX + gridXStepSize; i <= zeroX + distance; i += gridXStepSize)
	{
	    double cdValue = i / xScalingFactor;
	    String csValue = formatter.format(cdValue);
	    g2.drawString(csValue, (int)i - g2.getFontMetrics().stringWidth(csValue), zeroY + g2.getFontMetrics().getHeight());
	}

	this.setComponentPopupMenu(pMenu);
    }
}
