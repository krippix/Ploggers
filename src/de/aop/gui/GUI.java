package de.aop.gui;

import de.aop.exceptions.SyntaxError;
import de.aop.parser.Function;
import de.aop.parser.Parser;
import de.aop.plotter.*;

import javax.swing.*;
import java.awt.Image;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class GUI extends JFrame
{
	JFrame window;
	JPanel basePanel;
	JPanel menuPanelLeft;
	JPanel menuPanelBottom;
	Plot contentPanel;
	//JTextField functionInput;
	JTextField[] functionInput;
	int function_amount = 1;
	JSlider scaleSlider;
	JButton buttonGenerate;
	JButton buttonClear;
	Image graph;
	
	
	/**
	 * Builds the GUI
	 */
	public GUI()
	{
		// Main Window
		this.window = new JFrame();
		this.window.setIconImage(new ImageIcon("src/img/ploggers.png").getImage());
		this.window.setBackground(Color.black);
		this.window.setTitle("Ploggers Graph Visualizer");
	
		// Window close event
		this.window.addWindowListener
		(
			new WindowAdapter()
			{
				@Override
				public void windowClosing(WindowEvent e)
				{
					System.out.println("Exiting Software!");
					System.exit(0);
				}		
			}
		);
		
		// Base panel
		this.basePanel = new JPanel();
		this.basePanel.setLayout(new BorderLayout());
		this.basePanel.setBackground(Color.white);
		
		this.window.add(this.basePanel);
			
	
		// Menu Panel Left
		this.menuPanelLeft = new JPanel();
		this.menuPanelLeft.setLayout(new GridBagLayout());
		this.menuPanelLeft.setBackground(Color.lightGray);
	
			// Menu panel - content
			GridBagConstraints format = new GridBagConstraints();
			
			// Function input text field(s)
			format.gridx = 0;
			format.gridy = 0;
			format.weighty = 1;
			format.insets = new Insets(4,4,4,4); // top, left, bottom, right
			format.anchor = GridBagConstraints.NORTH;
			
			this.functionInput = new JTextField[5];

			this.functionInput[0] = new JTextField(1);
			this.functionInput[0].setText("(x+4)*0.1*x*(x-4)");
			this.functionInput[0].setColumns(15); // width, height
			this.functionInput[0].setToolTipText("f(x)");
			this.functionInput[0].addActionListener(e->generatePlot());
			
			this.menuPanelLeft.add(this.functionInput[0], format);
 
		this.basePanel.add(menuPanelLeft, BorderLayout.WEST);
		
		
			
		// Menu Panel Bottom
		this.menuPanelBottom = new JPanel();
		this.menuPanelBottom.setLayout(new GridBagLayout());
		this.menuPanelBottom.setBackground(Color.gray);
		this.menuPanelBottom.setToolTipText("");
	
		JButton buttonGenerate = new JButton("Generate Graph");
			buttonGenerate.addActionListener(e->generatePlot());
			format.anchor = GridBagConstraints.WEST;
			format.insets = new Insets(4,4,4,4);
			format.gridx = 0;
			format.gridy = 0;

			this.menuPanelBottom.add(buttonGenerate, format);
			

		JButton buttonClear = new JButton("Clear");
			buttonClear.addActionListener(e->clearGraphs());
			format.anchor = GridBagConstraints.EAST;
			format.insets = new Insets(4,4,4,4);
			format.gridx = 1;       //aligned with button 2
			format.gridy = 0;       //third row
			
			this.menuPanelBottom.add(buttonClear, format);
		this.basePanel.add(menuPanelBottom, BorderLayout.SOUTH);	
		
		
		// Content panel
		this.contentPanel = new Plot();
		this.contentPanel.setBackground(Color.white);
		this.basePanel.add(this.contentPanel, BorderLayout.CENTER);

		// React to mouse wheel
		this.contentPanel.addMouseWheelListener(new MouseWheelListener()
		{
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				contentPanel.changeScale(e.getWheelRotation());
			}
		});
		
		this.window.pack();
		this.window.setVisible(true);
	}
	
	
	/**
	 * Generates the Graph Background and Plots the function
	 */
	public void generatePlot()
	{
		Function function = new Function(this.functionInput[0].getText(), contentPanel.pixelToFunction(0, 0).x, contentPanel.pixelToFunction(contentPanel.getWidth(), 0).x);
		if(!function.isValid())
		{
			// TODO: Display Error in GUI
			System.err.println(function.getSyntaxError().toString());
			return;
		}
		
		this.contentPanel.setData(function);
		this.contentPanel.repaint();
		
	}

	/**
	 * Clears all function inputs
	 */
	private void clearGraphs()
	{
		this.contentPanel.setData(null);
		this.contentPanel.repaint();
	}


	/**
	 * Handle the amount of configurable functions
	 */
	private void addFunction()
	{
		
	}


	/**
	 * Generates all input text fields for functions
	 */
	private void generateFunctionInputs()
	{

	}
}
