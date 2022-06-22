package de.aop.gui;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI extends JFrame
{
	JFrame window;
	JPanel basePanel;
	JButton buttonGenerate;
	
	public GUI()
	{
		this.window = new JFrame();
		
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
		
		
		this.basePanel = new JPanel();
		this.window.add(this.basePanel);
		
		this.buttonGenerate = new JButton("Generate");
		this.buttonGenerate.addActionListener(e->generatePlot());
		
		
		
		this.window.setVisible(true);
	}
	
	
	private void generatePlot()
	{
		System.out.println("Totally Printing out a plot now :D");
	}
}
