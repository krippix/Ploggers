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
	private static final int BORDER_GAP = 30;
	private static final int MAX_SCORE = 20;
	private static final int[] data = {1,2,3,4,5,6,7,8,9,29}; //ToDo: replace with plot stuff
	private static final int Y_HATCH_CNT = 10; // Remove this (or more pR�ZisE)
	private static final int GRAPH_POINT_WIDTH = 0; //Nur bei extrempunkten?
	private static final Stroke GRAPH_STROKE = new BasicStroke(3f); //Nur bei extrempunkten?
	
	// Constructor
	public Plot()
	{
		// ToDo, Roberts dateityp entgegenehmen, oder in anderer Klasse die wichtigen Punkte berechnen
		// Klasse daf�r wird vermutlich Graph, in der sollen dann die Wichtigen Punkte und der Parse abgelegt werden.
	}
	
	public JPanel drawGraph()
	{
		JPanel graph = new JPanel();
		graph.add(this);
		return graph;
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    
	    // check what this is even doing
	    double xScale = ((double) getWidth() - 2 * BORDER_GAP) / (this.data.length - 1);
	    double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (MAX_SCORE - 1);
	    
	    List<Point> graphPoints = new ArrayList<Point>();
	    for (int i = 0; i < data.length; i++) 
	    {
	         int x1 = (int) (i * xScale + BORDER_GAP);
	         int y1 = (int) ((MAX_SCORE - data[i]) * yScale + BORDER_GAP);
	         graphPoints.add(new Point(x1, y1));
	    }
	    
	    // THE INTERESTING PART !!
	    g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
	    g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);
	    
	    // create hatch marks for y axis. Seitenbeschrf?
	    for (int i = 0; i < Y_HATCH_CNT; i++)
	    {
	    	int x0 = BORDER_GAP;
	    	int x1 = GRAPH_POINT_WIDTH + BORDER_GAP;
	    	int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
	    	int y1 = y0;
	    	g2.drawLine(x0, y0, x1, y1);
	    }
	    
	    // x-lass ich mal als test weg
	    
	    Stroke oldStroke = g2.getStroke();
	      g2.setColor(Color.red);
	      g2.setStroke(GRAPH_STROKE);
	      for (int i = 0; i < graphPoints.size() - 1; i++) {
	         int x1 = graphPoints.get(i).x;
	         int y1 = graphPoints.get(i).y;
	         int x2 = graphPoints.get(i + 1).x;
	         int y2 = graphPoints.get(i + 1).y;
	         g2.drawLine(x1, y1, x2, y2);         
	      }
	      
	      g2.setStroke(oldStroke);      
	      g2.setColor(Color.black);
	      for (int i = 0; i < graphPoints.size(); i++) {
	         int x = graphPoints.get(i).x - GRAPH_POINT_WIDTH / 2;
	         int y = graphPoints.get(i).y - GRAPH_POINT_WIDTH / 2;;
	         int ovalW = GRAPH_POINT_WIDTH;
	         int ovalH = GRAPH_POINT_WIDTH;
	         g2.fillOval(x, y, ovalW, ovalH);
	      }
	      

	}
    
	@Override
    public Dimension getPreferredSize()
	{
       return new Dimension(500, 500);
    }
	
	
	
}
