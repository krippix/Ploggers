package de.aop.plotter;
import de.aop.parser.Function;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.*;


public class Plot extends JPanel
{
	private static final int X_SCALE_MARKERS_MIN = 10; // Minimum amount of markers going from middle to top and bottom each
	private static final int Y_SCALE_MARKERS_MIN = 10; // Minimum amount of markers going from middle to left and right each
	private int xScaleMarkers = 0; // actual number of scale markers
	private int yScaleMarkers = 0; // actual number of scale markers
	private Function data;
	private int markerGap;
	private Coordinate middle;

	private Coordinate scale; // Scale of the whole grid
	
	/**
	 * 
	 */
	public Plot()
	{
		this.scale = new Coordinate(1,1);
		// ToDo, Roberts dateityp entgegenehmen, oder in anderer Klasse die wichtigen Punkte berechnen
		// Klasse dafür wird vermutlich Graph, in der sollen dann die Wichtigen Punkte und der Parse abgelegt werden.
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
	    drawAxis(g2);
	    
	    // Draw numbers on x- and y-axis
	    drawPlotNumbering(g2);
	    
	    
	    if (data != null) {
	    	drawFunction(g2);
	    }
	    
	    
	    // TODO Anhand der Extrempunkte usw. feststellen welcher Teil des Graphen überhaupt interessant ist.
	    //System.out.println("Result: "+getLabel(-1.4123));
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
		xGap = calculateGap(xAvailableSpace, X_SCALE_MARKERS_MIN);
		yGap = calculateGap(yAvailableSpace, Y_SCALE_MARKERS_MIN);
		
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
		return foundGap;	
	}
	
	
	/**
	 * @param Graphics object to wich lines are drawn
	 */
	private void drawSecondaryLines(Graphics2D g)
	{
		// X-Axis additions
		int currentPosition = middle.yAsInt();
		g.setColor(Color.lightGray);
		
		// middle -> y=MAX
		while (currentPosition + this.markerGap < getHeight())
		{
			g.drawLine(0, currentPosition+this.markerGap, getWidth(), currentPosition+this.markerGap);
			currentPosition += this.markerGap;
			this.yScaleMarkers++;
			// DEBUG System.out.println("CurrentPosition y->MAX: "+currentPosition+", "+this.markerGap);			
		}
		
		currentPosition = middle.yAsInt();
		// middle -> y=0
		while (currentPosition - markerGap > 0)
		{
			g.drawLine(0, currentPosition-markerGap, getWidth(), currentPosition-markerGap);
			currentPosition -= markerGap;
			// DEBUG System.out.println("CurrentPosition y->0: "+currentPosition);
		}
		
		// Y-Axis additions
		currentPosition = middle.xAsInt();
		g.setColor(Color.lightGray);
		
		// middle -> x=MAX
		while (currentPosition + this.markerGap < getWidth())
		{
			g.drawLine(currentPosition+this.markerGap, 0, currentPosition+this.markerGap, getHeight());
			currentPosition += this.markerGap;
			this.xScaleMarkers++;
			// DEBUG System.out.println("CurrentPosition x->MAX: "+currentPosition+", "+this.markerGap);			
		}
		
		currentPosition = middle.xAsInt();
		// middle -> x=0
		while (currentPosition - this.markerGap > 0)
		{
			g.drawLine(currentPosition-this.markerGap, 0, currentPosition-this.markerGap, getHeight());
			currentPosition -= this.markerGap;
			// DEBUG System.out.println("CurrentPosition x->0: "+currentPosition+", "+this.markerGap);			
		}
		
	}
	
	/**
	 * Adds axis labels to Graph
	 * @param graphics object to draw on
	 */
	private void drawAxis(Graphics2D g)
	{
		FontMetrics metrics = g.getFontMetrics();
		
		// Just drawing black lines
		g.setColor(Color.black);
	    g.drawLine(middle.xAsInt(), 0, middle.xAsInt(), getHeight());
	    g.drawLine(0, middle.yAsInt(), getWidth(), middle.yAsInt());
	    
	    // Draw Arrows and x,y
	    g.drawString("x", getWidth()-metrics.stringWidth("x")-6, middle.yAsInt()-6);
	    g.drawLine(getWidth(), middle.yAsInt(), getWidth()-4, middle.yAsInt()-4);
	    g.drawLine(getWidth(), middle.yAsInt(), getWidth()-4, middle.yAsInt()+4);
	    
	    g.drawString("y", middle.xAsInt()+6, metrics.getHeight());
	    g.drawLine(middle.xAsInt(), 0, middle.xAsInt()+4, 4);
	    g.drawLine(middle.xAsInt(), 0, middle.xAsInt()-4, 4);
	}
	
	
	// real number to pixel position (0,0) -> (middle.x,middle.y)
	public Coordinate functionToPixel(double x, double y)
	{
		Coordinate result = new Coordinate(0,0);
		
		result.x = x * this.markerGap * this.scale.y + this.middle.x;
		result.y = y * (-1) *this.markerGap * this.scale.y + this.middle.y ; 
		
		return result; 
	}
	
	
	// convert pixel position to real numbers
	private Coordinate pixelToFunction(double x, double y)
	{
		Coordinate result = new Coordinate(0,0);
		
		result.x = (x - this.middle.x) / (this.markerGap * this.scale.y);
		result.y = (y - this.middle.y) / (this.markerGap * this.scale.y) * (-1);
		
		return result;
	}
	
	
	// Drawing the function on top of the canvas
	private void drawFunction(Graphics2D g)
	{
		g.setColor(Color.red);
		g.setStroke(new BasicStroke(2f));
		
		
		int width = getWidth();
		int xPixel = 0; // iterator from left to right of visible canvas
		double xReal = 0; // x as Real number for function to use
		boolean firstRun = true;
		
		Coordinate currentPoint = new Coordinate(0,0);
		Coordinate previousPoint = new Coordinate(0,0);
		
		
		while (xPixel <= width)
		{
			xReal = pixelToFunction(xPixel, 0).x;
			currentPoint.setCoordinates(xReal, data.at(xReal));
			currentPoint = functionToPixel(currentPoint.x, currentPoint.y);
		
			if (firstRun)
			{
				previousPoint = currentPoint.clone();
				firstRun = false;
			}
			
			if (currentPoint.y < getHeight() * 1.2 && currentPoint.y > -getHeight()*0.2)
			{
				g.drawLine(previousPoint.xAsInt(), previousPoint.yAsInt(), currentPoint.xAsInt(), currentPoint.yAsInt());
				previousPoint = currentPoint.clone();
			}
			xPixel++;
		}
	}
	
	/**
	 * Draws numbers on x- and y-axis for scale
	 * More details about the mesurement: https://docs.oracle.com/javase/tutorial/2d/text/measuringtext.html
	 * @param the plot graphics object
	 */
	private void drawPlotNumbering(Graphics2D g)
	{
		// object to get info about strings, other variables
		FontMetrics metrics = g.getFontMetrics();
		String label = "";
		int offset = -2;
		int offset2 = 0; // used for all but '0'
		int xPos = 0;
		int yPos = 0;
		
		// place 0 at origin
		label = "0";
		xPos = (middle.xAsInt()-metrics.stringWidth("0"));
		yPos = (middle.yAsInt()+metrics.getHeight());
		g.drawString(label, xPos+offset, yPos+offset);
		
		// draw the rest of the numbers
		// x-axis
		for (int i=2; i < xScaleMarkers/2 - 1; i+=2)
		{
			// Positive numbers
			xPos = (i * this.markerGap) + middle.xAsInt();
			label = getLabel(pixelToFunction(xPos,0).x);
			offset2 = -(metrics.stringWidth(label)/2);
			g.drawString(label, xPos+offset+offset2, yPos+offset);
			
			// Negative numbers
			xPos = -(i * this.markerGap) + middle.xAsInt();
			label = getLabel(pixelToFunction(xPos,0).x);
			offset2 = -(metrics.stringWidth(label)/2);
			g.drawString(label, xPos+offset+offset2, yPos+offset);
		}
		
		//y-axis
		xPos = this.middle.xAsInt();
		offset = -4;
		for (int i=2; i < yScaleMarkers/2 - 1; i+=2)
		{
			// Positive numbers
			yPos = (i * this.markerGap) + middle.yAsInt();
			label = getLabel(pixelToFunction(0,yPos).y);
			offset2 = -(metrics.stringWidth(label));
			g.drawString(label, xPos+offset+offset2, yPos-offset);
			
			// Negative numbers
			yPos = -(i * this.markerGap) + middle.yAsInt();
			label = getLabel(pixelToFunction(0,yPos).y);
			offset2 = -(metrics.stringWidth(label));
			g.drawString(label, xPos+offset+offset2, yPos-offset);
		}
		
	}
	
	/**
	 * Takes Double and returns fitting label for x/y-axis
	 * @param number
	 * @return Number String as int or double with two decimals
	 */
	private String getLabel(double number)
	{
		String str_number = Double.toString(number);
		String[] str_part = str_number.split("\\.");
		boolean isNegative = false;
		
		if (str_part[0].charAt(0) == '-')
		{
			isNegative = true;
			str_part[0] = str_part[0].substring(1);
		}
		
		// Check if first decimal is not 0
		if (str_part[1].charAt(0) == '0')
		{
			str_number = str_part[0];
		}
		else
		{
			str_number = str_part[0]+"."+str_part[1].substring(0,1);
		}
		
		// Re-Add '-' if removed earlier
		if (isNegative)
		{
			return "-"+str_number;
		}
		else
		{
			return str_number;
		}
	}
	
	
	/**
	 * Changes Scale by some percentage depending on the input variable 
	 * @param change
	 */
	public void changeScale(int change)
	{
		boolean isNegative = false;
		
		if (change < 0)
		{
			isNegative = true;
			change = change * -1;
		}
		
		
		for (int i=0; i < change; i++)
		{
			if (isNegative)
			{
				this.scale.x += 1;
			}
			else
			{
				this.scale.x -= 1;
			}
			this.scale.y = Math.pow(2,this.scale.x/2);
		}
		repaint();
	}
	
	
	/**
	 * Sets data that will be used for drawing the graph
	 * @param data
	 */
	public void setData(Function data)
	{
		this.data = data;
	}
}