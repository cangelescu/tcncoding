/*

 Copyright (C) 2009-2011 Oleksandr Natalenko aka post-factum

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

package tcncoding;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Vizualizes input data
 * @author Oleksandr Natalenko aka post-factum
 */
public class DataVizualizator extends JPanel
{
    private List<DataVizualizatorProvider> chartData;
    private Paint paint;
    private String lX, lY;
    private int lineWidth;

    /**
     * Creates vizualizator for provided data
     * @param _data list of lists of provided data
     * @param _width width of output area
     * @param _height height of output area
     * @param _legendX X axis legend
     * @param _legendY Y axis legend
     * @param _lineWidth width of line, px
     */
    public DataVizualizator(List<DataVizualizatorProvider> _data, int _width, int _height, String _legendX, String _legendY, int _lineWidth)
    {
	setSize(_width, _height);
	chartData = _data;
	lX = _legendX;
	lY = _legendY;
        lineWidth = _lineWidth;
    }

    /**
     * Saves chart to file
     * @param _file file to save to
     */
    public void saveAsPNG(File _file)
    {
        //adds .png extension automatically if it hasn't been added before
        File remastered;
        if (!_file.getAbsolutePath().endsWith(".png") &&  !_file.getAbsolutePath().endsWith(".PNG"))
            remastered = new File(_file.getAbsolutePath() + ".png");
        else
            remastered = _file;

        //creates image
        BufferedImage bufferedImage = (BufferedImage) createImage(getWidth(), getHeight());
        Graphics imageContext = bufferedImage.getGraphics();
        paint(imageContext);
        imageContext.dispose();
        try {
            ImageIO.write(bufferedImage, "png", remastered);
        } catch (Exception ex)
        {
            System.err.println(ex.getLocalizedMessage());
        }
    }

    /**
     * 
     * @param g
     */
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
	final int leftMarginX = 22 * yStepsMargin;
	final int rightMarginX = 10;
	final int topMarginY = 10;
	final int bottomMarginY = 10;
	final int xBorder = 10;
	final int yBorder = 10;
	final int xSteps = 10;
	final int ySteps = 10;
	final int topYBorder = topMarginY + yBorder;
	final int bottomYBorder = getHeight() - bottomMarginY;

	//longtitude of chart
	int distance = getWidth() - (leftMarginX + rightMarginX + xBorder);

	//arrows size
	final int arrowWidth = 2;
	final int arrowHeight = 7;

	//zero levels
	final int zeroX = leftMarginX;
	final int zeroY = getHeight() / 2;

        //formatters
        DecimalFormatter XFormatter = new DecimalFormatter(1);
        DecimalFormatter YFormatter = new DecimalFormatter(2);

	//find min/max values
	double minX = chartData.get(0).getStart();
	double maxX = chartData.get(0).getEnd();
	double maxY = chartData.get(0).getMaxValue();
	for (DataVizualizatorProvider cdvp: chartData)
        {
            double currentMax = Math.max(Math.abs(cdvp.getMaxValue()), Math.abs(cdvp.getMinValue()));
            if (currentMax > maxY)
                maxY = currentMax;
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

	double gridYStepSize;
	if (maxY != 0)
	    gridYStepSize = 2 * maxY * yScalingFactor / ySteps;
	else
	    gridYStepSize = (bottomYBorder - topYBorder + 1) / ySteps;

	//X axis steps
        //minimal step quantum
        double stepQuantum = Math.pow(10, -(Math.floor(Math.abs(Math.log10(maxX))) + 2));
        //count of minimal steps
        double minimalStepsCount = maxX / stepQuantum;
        //length of each step of xSteps
        double stepLength = Math.round(minimalStepsCount / xSteps);
        //time of each step
        double xAxisStep = stepLength * stepQuantum;

	//calculate amount of graphic points
	double gridXStepSize = xScalingFactor * xAxisStep;

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
		String csValue = YFormatter.formatValue(cdValue);
		g2.setColor(Color.BLACK);
		g2.drawString(csValue, zeroX - g2.getFontMetrics().stringWidth(csValue) - yStepsMargin, (int)i + g2.getFontMetrics().getHeight() / 3);
		g2.setColor(gridColor);
	    }
	}

	g2.setColor(Color.BLACK);
	//draw coordinates system
	g2.drawLine(leftMarginX, zeroY, getWidth() - rightMarginX + xBorder, zeroY);
	g2.drawLine(leftMarginX, topMarginY, leftMarginX, bottomYBorder);
	//0y arrow
	g2.drawLine(leftMarginX, topMarginY, leftMarginX - arrowWidth, topMarginY + arrowHeight);
	g2.drawLine(leftMarginX, topMarginY, leftMarginX + arrowWidth, topMarginY + arrowHeight);
	//0x arrow
	g2.drawLine(getWidth() - rightMarginX + xBorder, zeroY, getWidth() - rightMarginX - arrowHeight + xBorder, zeroY - arrowWidth);
	g2.drawLine(getWidth() - rightMarginX + xBorder, zeroY, getWidth() - rightMarginX - arrowHeight + xBorder, zeroY + arrowWidth);

	//legend x
	g2.drawString(lX, getWidth() - g2.getFontMetrics().stringWidth(lX), zeroY + g2.getFontMetrics().getHeight());
	//legend y
	g2.drawString(lY, zeroX - g2.getFontMetrics().stringWidth(lY) - yStepsMargin, topMarginY);

	//set stroke
	Stroke cStroke = new BasicStroke(lineWidth);
	g2.setStroke(cStroke);
	for (DataVizualizatorProvider cdvp: chartData)
	{
	    //add right-click description
	    JMenuItem cItem = new JMenuItem(cdvp.getDescription());
	    g2.setColor(cdvp.getChartColor());
	    cItem.setForeground(cdvp.getChartColor());
	    pMenu.add(cItem);
	    //current position of pen
	    int currentX = zeroX;
	    int currentY = zeroY;

	    //gets total time of function
	    double totalTime = cdvp.getEnd() - cdvp.getStart();
	    //step of drawing
	    double step = totalTime / (double)distance;
	    //initial time is start point of zero piece
	    double currentTime = minX;
	    //remember real previous value to draw impulses properly
	    double prevYValue = 0;
	    //draw chart
	    
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
	    //draw line to zero level at the end
	    g2.drawLine(currentX, currentY, currentX, zeroY);
	}

	//X axis steps values
	g2.setColor(Color.BLACK);
	for (double i = zeroX + gridXStepSize; i <= zeroX + distance; i += gridXStepSize)
	{
	    double cdValue = (i - zeroX) / xScalingFactor;
	    String csValue = XFormatter.formatValue(cdValue);
	    g2.drawString(csValue, (int)i - g2.getFontMetrics().stringWidth(csValue), zeroY + g2.getFontMetrics().getHeight());
	}

        //creates item of image saving
        JMenuItem saveImageItem = new JMenuItem(java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SAVE IMAGE…"));
        ActionListener saveImageAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("PNG FILES"), "png", "PNG"));
                fileChooser.setDialogTitle(java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SAVE IMAGE"));

                int returnValue = fileChooser.showSaveDialog(DataVizualizator.this);

                switch (returnValue)
                {
                    case JFileChooser.APPROVE_OPTION:
                        saveAsPNG(fileChooser.getSelectedFile());
                        break;
                    case JFileChooser.CANCEL_OPTION:
                        break;
                    case JFileChooser.ERROR_OPTION:
                        JOptionPane.showMessageDialog(DataVizualizator.this, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("ERROR OCCURED WHILE SAVING IMAGE!"), java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("ERROR"), JOptionPane.ERROR_MESSAGE);
                        break;
                    default:
                        break;
                }
            }
        };
        saveImageItem.addActionListener(saveImageAction);
        pMenu.add(saveImageItem);

	setComponentPopupMenu(pMenu);
    }
}
