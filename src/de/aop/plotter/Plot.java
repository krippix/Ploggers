package de.aop.plotter;
import de.aop.exceptions.SyntaxError;
import de.aop.parser.Function;
import de.aop.parser.Interval;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.*;


public class Plot extends JPanel
{
	private static final int X_SCALE_MARKERS_MIN = 10; // Minimum amount of markers going from middle to top and bottom each
	private static final int Y_SCALE_MARKERS_MIN = 10; // Minimum amount of markers going from middle to left and right each
	private int xScaleMarkers = 0; // actual number of scale markers
	private int yScaleMarkers = 0; // actual number of scale markers
	private double[] xAxisLabels;
	private double[] yAxisLabels;
	private Function data;
	private int markerGap;
	private Coordinate middle;
	private Coordinate origin; // pixel location of origin

	private Coordinate scale; // Scale of the whole grid
	
	/**
	 * 
	 */
	public Plot()
	{
		this.scale = new Coordinate(1,1);
		// ToDo, Roberts dateityp entgegenehmen, oder in anderer Klasse die wichtigen Punkte berechnen
		// Klasse daf��r wird vermutlich Graph, in der sollen dann die Wichtigen Punkte und der Parse abgelegt werden.
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

		// Draw grid of the canvas
		drawGrid(g2);
	    
		// Draw x- and y-axis 
	    drawAxis(g2);
	    
	    // Draw numbers on x- and y-axis
	    //drawPlotNumbering(g2);
	    
	    if (data != null) {
	    	drawFunction(g2);
	    }
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
	 * Function that draws the grid lines onto the canvas
	 * @param g
	 */
	private void drawGrid(Graphics2D canvas)
	{
		//Interval domain = this.data.getDomain();
		canvas.setColor(Color.lightGray);

		// x-Axis ===
		for (int i=0; i*markerGap < getHeight()/2; i++)
		{
			canvas.drawLine(0, this.middle.yAsInt()+i*markerGap, getWidth(), this.middle.yAsInt()+i*markerGap);
			canvas.drawLine(0, this.middle.yAsInt()-i*markerGap, getWidth(), this.middle.yAsInt()-i*markerGap);
		}

		// y-axis ||||
		for (int i=0; i*markerGap < getWidth(); i++)
		{
			canvas.drawLine(this.middle.xAsInt()+i*markerGap, getHeight(), this.middle.xAsInt()+i*markerGap, 0);
			canvas.drawLine(this.middle.xAsInt()-i*markerGap, getHeight(), this.middle.xAsInt()-i*markerGap, 0);
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
	

	/**
	 * Converts input value from one interval to another
	 * @param from initial interval
	 * @param to new interval
	 * @param val value to convert
	 * @return value in new interval
	 */
	public double map(Interval from, Interval to, double val)
	{
		return (val - from.min) * to.length() / from.length() + to.min;
	}
	
	/**
	 * Converts function value to their Pixel position equivalent
	 * @param x
	 * @param y
	 * @return pixel position coordinates
	 */
	public Coordinate functionToPixel(double x, double y)
	{
		Coordinate result = new Coordinate(0,0);
		result.x = x * this.markerGap * this.scale.x + this.middle.x;
		result.y = y * (-1) *this.markerGap * this.scale.y + this.middle.y ; 
		
		return result; 
	}
	
	
	/**
	 * Converts pixel coordinates to real number equivalent
	 * @param x
	 * @param y
	 * @return real number coordinates
	 */
	public Coordinate pixelToFunction(double x, double y)
	{
		Coordinate result = new Coordinate(0,0);
		// old : result.x = (x - this.middle.x) / (this.markerGap * this.scale.x);
		result.x = (x - this.middle.x) / (this.markerGap * this.scale.x);
		result.y = (y - this.middle.y) / (this.markerGap * this.scale.y) * (-1);
		
		return result;
	}
	
	
	/**
	 * Draws function on top of the graphic object
	 * @param g Graphics object to draw on
	 */
	private void drawFunction(Graphics2D g)
	{
		// Settings for the drawn line
		g.setColor(Color.red);
		g.setStroke(new BasicStroke(2f));
		
		// Define interval over which Graph will be visible
		Interval screenWidth = new Interval(0, getWidth());
		Interval screenHeight = new Interval(getHeight(), 0);
				
		ArrayList<Double> poles = data.getPoles();
		
		Interval domain = data.getDomain();
		Interval range = data.getRange();
		
		// Keep some free space above and below the drawn function
		range.min -= range.length()* 0.1;
		range.max += range.length()* 0.1;
		
		// Set starting coordinates for drawing
		Coordinate currentPoint = new Coordinate(0,0);
		Coordinate previousPoint = new Coordinate(map(domain,screenWidth, domain.min-1),map(range,screenHeight,data.at(domain.min-1)));

		// Drawing the Graph onto the grid
		for(int i = 0; i < screenWidth.max; i++)
		{
			double x = domain.min + domain.length() * (double)i / screenWidth.max;
			double y = data.at(x);

			// System.out.println("x: "+x+" y: "+y);

			currentPoint = new Coordinate(
				map(domain, screenWidth, x),
				map(range, screenHeight, y)
			);

			// System.out.println("Mapped: x:"+currentPoint.x+" y: "+currentPoint.y);
			
			// Avoid drawing lines through poles
			boolean poleInbetween = false;
			for(double pole : poles)
			{
				double poleScreen = map(domain, screenWidth, pole);
				
				if(previousPoint.x < poleScreen && poleScreen < currentPoint.x)
				{
					poleInbetween = true;
					break;
				}
			}
			if(!poleInbetween)
			{
				// System.out.println("Connecting: "+previousPoint.xAsInt()+" ("+previousPoint.y+") and "+currentPoint.xAsInt());
				g.drawLine(previousPoint.xAsInt(), previousPoint.yAsInt(), currentPoint.xAsInt(), currentPoint.yAsInt());
				
			}
			else	// If there is a pole, connect the points with some point at infinity above/below them
			{
				System.out.println("Pole found between: "+previousPoint.x+" and "+currentPoint.x);
				connectWithInfinity(g, previousPoint, true);
				connectWithInfinity(g, currentPoint, false);
			}
			previousPoint = currentPoint.clone();
		}
		System.out.println("Range: "+range.min+", "+range.max);
	}
	

	/**
	 * Connects a coordinate with infinity. Takes into account whether the function approaches
	 * +inf or -inf at this point.
	 * @param g Canvas to draw to
	 * @param from Point to connect with infinity
	 * @param beforePole Whether this point is before or after a pole
	 */
	private void connectWithInfinity(Graphics2D g, Coordinate from, boolean beforePole)
	{
		// The slope of the function at the point determines whether we connect with +inf or -inf
		double x = map(new Interval(0,getWidth()), data.getDomain(), from.x);
		double slope = data.at(x, 1);
		
		// If the point is after a pole, the logic needs to be inverted
		double factor = beforePole ? -1.0 : 1.0;
		
		Coordinate infinity = new Coordinate(from.x, Math.signum(slope) * getHeight() * factor + 10);
		g.drawLine(from.xAsInt(), from.yAsInt(), infinity.xAsInt(), infinity.yAsInt());
	}
	

	/**
	 * Calculates Gap between each scale marker from left to right
	 * @return distance (Real number) between markers
	 */
	private double calculateXAxisGap()
	{
		double gap = (this.data.getDomain().max - this.data.getDomain().min)/xScaleMarkers;
		this.xAxisLabels = new double[xScaleMarkers];

		for (int i=0; i < xScaleMarkers; i++)
		{
			this.xAxisLabels[i] = this.data.getDomain().max + i * gap;
		}

		return gap;
	}


	/**
	 * Draws numbers on x- and y-axis for scale
	 * More details about the mesurement: https://docs.oracle.com/javase/tutorial/2d/text/measuringtext.html
	 * @param g Graphic object to draw to
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
		/*
		label = "0";
		xPos = (middle.xAsInt()-metrics.stringWidth("0"));
		yPos = (middle.yAsInt()+metrics.getHeight());
		g.drawString(label, xPos+offset, yPos+offset);
		*/

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
		
		data.setDomain(pixelToFunction(0, 0).x, pixelToFunction(getWidth(), 0).x);
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
	
	public Function getData()
	{
		return this.data;
	}


	/**
	 * Set's x-Axis range displayed
	 * @param from
	 * @param to
	 */
	public void setDomain(double from, double to)
	{
		if (from < to)
		{
			this.data.setDomain(from, to);
			return;
		}
	}
}