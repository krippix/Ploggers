package de.aop.gui;

import de.aop.parser.Function;
import de.aop.plotter.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class GUI extends JFrame
{
	JFrame window;
	JPanel basePanel;
	JPanel menuPanelLeft;
		JTextField functionInput;
		JLabel errorMessage;
	JPanel menuPanelBottom;
		JButton buttonGenerate;
		JButton buttonClear;
	Plot contentPanel;
		Image graph;
	
	
	JTextField from;
	JTextField to;
	
	
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
		this.menuPanelLeft.setBackground(Color.lightGray);
	
			// Menu panel - content
			GridBagConstraints format = new GridBagConstraints();
			
			// Function input text field(s)
			format.gridx = 0;
			format.gridy = 0;
			format.weighty = 1;
			format.insets = new Insets(4,4,4,4); // top, left, bottom, right
			format.anchor = GridBagConstraints.NORTH;

			Font font1 = new Font("SansSerif", Font.PLAIN, 20);

			this.functionInput = new JTextField(1);
			this.functionInput.setFont(font1);
			this.functionInput.setText("(x+4)*0.1*x*(x-4)");
			this.functionInput.setColumns(10); // width, height
			this.functionInput.addActionListener(e->generatePlot());
			
			this.menuPanelLeft.add(this.functionInput, format);

			// place error message below Textinput field
			this.errorMessage = new JLabel(" ❓ ");
			this.errorMessage.setFont(font1);
			this.errorMessage.setToolTipText("Enter your function here.");
			format.gridx = 0;
			format.gridy = 1;
			format.weighty = 1;

			this.menuPanelLeft.add(this.errorMessage, format);
 
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

		JLabel labelFrom = new JLabel("from");
			format.anchor = GridBagConstraints.EAST;
			format.gridx = 2;

			this.menuPanelBottom.add(labelFrom, format);

		this.from = new JTextField();
			from.setColumns(8);
			from.setText("-10");
			format.gridx = 3;

			this.menuPanelBottom.add(from, format);
		
		JLabel labelTo = new JLabel("to");
			format.gridx = 4;

			this.menuPanelBottom.add(labelTo, format);

		this.to = new JTextField();
			to.setColumns(8);
			to.setText("10");
			format.gridx = 5;

			this.menuPanelBottom.add(to, format);
		
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
				contentPanel.repaint();
			}
		});

		contentPanel.repaint();
		this.window.pack();
		this.window.setVisible(true);
	}
	
	
	/**
	 * Generates the Graph Background and Plots the function
	 */
	public void generatePlot()
	{
		Function function = new Function(this.functionInput.getText(), contentPanel.pixelToFunction(0, 0).x, contentPanel.pixelToFunction(contentPanel.getWidth(), 0).x);
	
		if(!function.isValid())
		{
			this.functionInput.setBackground(Color.PINK);
			this.errorMessage.setToolTipText(function.getSyntaxError().toString());
			return;
		}

		this.errorMessage.setToolTipText("Enter your function here.");
		this.functionInput.setBackground(Color.white);
		
		try
		{
			this.contentPanel.setData(function);
		} catch (Exception e){
			System.out.println("left cant be smaller than right!");
		}
		this.contentPanel.setRange(Double.parseDouble(this.from.getText()),Double.parseDouble(this.to.getText()));
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
}
