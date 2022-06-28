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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class GUI extends JFrame
{
	JFrame window;
	JPanel basePanel;
	JPanel menuPanel;
	JPanel infoPanel;
	Plot contentPanel;
	JTextField functionInput;
	JButton buttonGenerate;
	Image graph;
	
	
	public GUI()
	{
		this.window = new JFrame();
		this.window.setIconImage(new ImageIcon("src/img/ploggers.png").getImage());
		this.window.setBackground(Color.black);
		this.window.setTitle("Ploggers Graph Visualizer");
	
		
		// react to window beeing closed
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
			
	
		// Menu panel
		this.menuPanel = new JPanel();
		this.menuPanel.setLayout(new GridBagLayout());
		this.menuPanel.setBackground(Color.lightGray);
		
		this.basePanel.add(menuPanel, BorderLayout.WEST);
		
		// Menu panel - content
		GridBagConstraints format = new GridBagConstraints();
		
		// Text input
		format.gridx = 0;
		format.gridy = 0;
		format.weighty = 1;
		format.insets = new Insets(4,4,4,4); // top, left, bottom, right
		format.anchor = GridBagConstraints.NORTH;
		format.fill = GridBagConstraints.HORIZONTAL;
		this.functionInput = new JTextField(1);
		this.functionInput.setText("f(x)");
		this.functionInput.setPreferredSize(new Dimension(0,30)); // width, height
		// TODO this.functionInput.addFocusListener(null)
		this.functionInput.addActionListener(e->generatePlot());
		
		this.menuPanel.add(this.functionInput, format);
					
		// Button "generate" --> This button determines the size of the side panel
		format.gridx = 0;
		format.gridy = 10;
		format.insets = new Insets(4,4,4,4); // top, left, bottom, right
		format.anchor = GridBagConstraints.SOUTH;
		format.fill = GridBagConstraints.HORIZONTAL;
		this.buttonGenerate = new JButton("Generate Graph");
		this.buttonGenerate.setPreferredSize(new Dimension(150,30)); // width, height
		this.buttonGenerate.addActionListener(e->generatePlot());
		
		this.menuPanel.add(this.buttonGenerate, format);
		
		
		// Information panel
		this.infoPanel = new JPanel();
		this.infoPanel.setLayout(new GridBagLayout());
		this.infoPanel.setBackground(Color.lightGray);
		this.infoPanel.setToolTipText("Yeet");
		
		this.basePanel.add(this.infoPanel, BorderLayout.SOUTH);
		
		// Information panel - content
		JTextField infoText1 = new JTextField("Read only test text");
		infoText1.setEditable(false);
		this.infoPanel.add(infoText1);
		
		
		// Content panel
		this.contentPanel = new Plot();
		this.contentPanel.setBackground(Color.white);
		
		this.basePanel.add(this.contentPanel, BorderLayout.CENTER);



		/*
		// content panel with graph image
		GridBagConstraints contentFormat = new GridBagConstraints();
		
		
		contentFormat.gridx = 2;
		contentFormat.gridy = 0;
		contentFormat.weightx = 0.8;
		contentFormat.insets = new Insets(4,0,0,4); // top, left, bottom, right
		
		contentFormat.anchor = GridBagConstraints.NORTHEAST;
		contentFormat.fill = GridBagConstraints.BOTH;
		
		this.basePanel.add(this.contentPanel, contentFormat);
		*/
				
		// window settings
		//this.window.setSize(1200,800);
		this.window.pack();
		this.window.setVisible(true);
	}
	
	
	private void generatePlot()
	{
		Function function = new Function(this.functionInput.getText(), -5.0, 5.0);
		if(!function.isValid())
		{
			// TODO: Display Error in GUI
			System.err.println(function.getSyntaxError().toString());
			return;
		}
		
		this.contentPanel.setData(function);
		this.contentPanel.repaint();
		
	}
}
