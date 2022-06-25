package de.aop.plotter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class Plot extends JPanel
{
	private static final int[] data = {1,2,3,4,5,6,7,8,9,29}; //ToDo: replace with plot stuff
	private static final int X_SCALE_MARKERS = 10; // Amount of markers going from middle to top and bottom each
	private static final int Y_SCALE_MARKERS = 10; // Amount of markers going from middle to left and right each
	private int markerGap;
	private static final Stroke GRAPH_STROKE = new BasicStroke(3f); //Nur bei extrempunkten?
	private Coordinate middle;
	
	/**
	 * 
	 */
	public Plot()
	{
		// ToDo, Roberts dateityp entgegenehmen, oder in anderer Klasse die wichtigen Punkte berechnen
		// Klasse daf�r wird vermutlich Graph, in der sollen dann die Wichtigen Punkte und der Parse abgelegt werden.
	}	
	
	

		
	/**
	 * This is handling all drawing an is called when needed by swing
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    
	    // Sets x and yMiddle to usable ints
	    calcCanvasMiddle();
	    
	    // Calculate biggest marker gap that keeps the graph intact
	    calcMarkerGap();
	    
	    // Draw x- and y-axis helper lines
	    drawSecondaryLines(g2);
	    
	    // Draw x- and y-axis 
	    g2.setColor(Color.black);
	    g2.drawLine(middle.x, 0, middle.x, getHeight());
	    g2.drawLine(0, middle.y, getWidth(), middle.y);
	    

	    
	    
	    // TODO Anhand der Extrempunkte usw. feststellen welcher Teil des Graphen �berhaupt interessant ist.
	    
	}
    
	
	/**
	 * This function determines the canvas size for the drawing
	 */
	@Override
    public Dimension getPreferredSize()
	{
       return new Dimension(601, 601);
    }
	
	
	
	/**
	 * This function calculates the canvas middle and writes it to this.middle
	 */
	private void calcCanvasMiddle() {
		this.middle = new Coordinate(determineMiddle(getWidth()),determineMiddle(getHeight()));
	}
	
	
	/**
	 * Helper function for calcCanvasMiddle doing the calculation
	 * @param size to find middle from
	 * @return returns middle
	 */
	private int determineMiddle(int size)
	{
		if (size % 2 == 0)
		{
			return size/2;
		}
		else
		{
			return (size - 1)/2 +1;
		}
	}
	
	
	/**
	 * Returns scale that allows for all scale markers, while keeping the scale of x and y equal
	 */
	private void calcMarkerGap()
	{
		// TODO: This could fail if the canvas is too small or there are too many markers
		int xAvailableSpace = getWidth()/2;
		int yAvailableSpace = getHeight()/2;
		int xGap = 0;
		int yGap = 0;

		// Calculate biggest possible gap between each line
		xGap = calculateGap(xAvailableSpace, X_SCALE_MARKERS);
		yGap = calculateGap(yAvailableSpace, Y_SCALE_MARKERS);
		
		this.markerGap = Math.min(xGap, yGap);
	}
	
	
	/**
	 * Helper function for calcMarkerGap()
	 * @param space available pixels
	 * @param markers amount of used markers
	 * @return determined gap
	 */
	private int calculateGap(int space, int markers)
	{
		int foundGap = 1;
		
		while ((foundGap+1)*markers < space)
		{
			foundGap++;
		}
		System.out.println("Found Gap!: "+foundGap);
		return foundGap;	
	}
	
	
	/**
	 * @param Graphics object to wich lines are drawn
	 */
	private void drawSecondaryLines(Graphics2D g)
	{
		// X-Axis additions
		int currentPosition = middle.y;
		g.setColor(Color.lightGray);
		
		// middle -> y=MAX
		while (currentPosition + this.markerGap < getHeight())
		{
			g.drawLine(0, currentPosition+this.markerGap, getWidth(), currentPosition+this.markerGap);
			currentPosition += this.markerGap;
			System.out.println("CurrentPosition y->MAX: "+currentPosition+", "+this.markerGap);			
		}
		
		currentPosition = middle.y;
		// middle -> y=0
		while (currentPosition - markerGap > 0)
		{
			g.drawLine(0, currentPosition-markerGap, getWidth(), currentPosition-markerGap);
			currentPosition -= markerGap;
			System.out.println("CurrentPosition y->0: "+currentPosition);
		}
		
		// Y-Axis additions
		currentPosition = middle.x;
		g.setColor(Color.lightGray);
		
		// middle -> x=MAX
		while (currentPosition + this.markerGap < getWidth())
		{
			g.drawLine(currentPosition+this.markerGap, 0, currentPosition+this.markerGap, getHeight());
			currentPosition += this.markerGap;
			System.out.println("CurrentPosition x->MAX: "+currentPosition+", "+this.markerGap);			
		}
		
		currentPosition = middle.x;
		// middle -> x=0
		while (currentPosition - this.markerGap > 0)
		{
			g.drawLine(currentPosition-this.markerGap, 0, currentPosition-this.markerGap, getHeight());
			currentPosition -= this.markerGap;
			System.out.println("CurrentPosition x->0: "+currentPosition+", "+this.markerGap);			
		}
		
	}
	
	
	// Convert coordinate to 
}
