package fhscqs;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.*;

public class Settings extends JFrame {
	/**
	 * JFrame that contains all of the GUI elements that change the settings of the application.
	 * A new instance is created every time the settings option has been selected and destroyed every time it is closed.
	 */
	private static final long serialVersionUID = -4150353828361240160L;

	public Settings() {
		//Define and initialize elements.
		JPanel radioPanel = new JPanel();
		JPanel checkPanel = new JPanel();
		JCheckBox compiledCode = new JCheckBox("Compiled");
		ButtonGroup mainGroup = new ButtonGroup();
		JRadioButton compMode = new JRadioButton("Simulate Competition");
		JRadioButton custom = new JRadioButton("Custom Settings");
		JCheckBox programOutput = new JCheckBox("Print stdout to Console");
		JCheckBox errorOutput = new JCheckBox("Print stderr to Console");
		JCheckBox keyComparison = new JCheckBox("Reveal Comparison");
		JCheckBox keyOutput = new JCheckBox("Reveal Answer Key");
		
		//Build GUI
		mainGroup.add(compMode);
		mainGroup.add(custom);
		
		//Determine if custom settings are allowed, and change GUI appearance accordingly
		if(QuestSim.custom) mainGroup.setSelected(custom.getModel(), true); 
		else {
			mainGroup.setSelected(compMode.getModel(), true);
			programOutput.setEnabled(false);
			errorOutput.setEnabled(false);
			keyComparison.setEnabled(false);
			keyOutput.setEnabled(false);
		}
		
		//Initialize current settings
		if(QuestSim.compiled) compiledCode.setSelected(true);
		if(QuestSim.enableErrorOutput) errorOutput.setSelected(true);
		if(QuestSim.enableKeyComparison) keyComparison.setSelected(true);
		if(QuestSim.enableKeyDisplay) keyOutput.setSelected(true);
		if(QuestSim.enableProgramOutput) programOutput.setSelected(true);
		
		//Build GUI
		Border blackline = BorderFactory.createLineBorder(Color.black);
		
		radioPanel.setBorder(BorderFactory.createTitledBorder(blackline, "Configuration"));
		radioPanel.setLayout(new GridLayout(2, 1));
		radioPanel.add(compMode);
		radioPanel.add(custom);
		
		//Give check-boxes and buttons functionality
		compiledCode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(compiledCode.isSelected()) {
					QuestSim.compiled = true;
				} else {
					QuestSim.compiled = false;
				}
			}
		});
		
		compMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				QuestSim.enableErrorOutput = false;
				QuestSim.enableKeyComparison = false;
				QuestSim.enableKeyDisplay = false;
				QuestSim.enableProgramOutput = false;
				
				programOutput.setSelected(false);
				programOutput.setEnabled(false);
				errorOutput.setSelected(false);
				errorOutput.setEnabled(false);
				keyComparison.setSelected(false);
				keyComparison.setEnabled(false);
				keyOutput.setSelected(false);
				keyOutput.setEnabled(false);
				QuestSim.custom = false;
			}
		});
		custom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				programOutput.setEnabled(true);
				errorOutput.setEnabled(true);
				keyComparison.setEnabled(true);
				keyOutput.setEnabled(true);
				QuestSim.custom = true;
			}
		});
		
		programOutput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(programOutput.isSelected()) {
					QuestSim.enableProgramOutput = true;
				} else {
					QuestSim.enableProgramOutput = false;
				}
			}
		});
		errorOutput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(errorOutput.isSelected()) {
					QuestSim.enableErrorOutput = true;
				} else {
					QuestSim.enableErrorOutput = false;
				}
			}
		});
		keyComparison.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(keyComparison.isSelected()) {
					QuestSim.enableKeyComparison = true;
				} else {
					QuestSim.enableKeyComparison = false;
				}
			}
		});
		keyOutput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(keyOutput.isSelected()) {
					QuestSim.enableKeyDisplay = true;
				} else {
					QuestSim.enableKeyDisplay = false;
				}
			}
		});
		
		//Build GUI
		checkPanel.setLayout(new GridLayout(4, 1));
		checkPanel.add(programOutput);
		checkPanel.add(errorOutput);
		checkPanel.add(keyComparison);
		checkPanel.add(keyOutput);
		
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BorderLayout());
		northPanel.add(compiledCode, BorderLayout.NORTH);
		northPanel.add(radioPanel);
		
		//Set frame settings and finish GUI
		setLayout(new BorderLayout());
		add(northPanel, BorderLayout.NORTH);
		add(checkPanel);
		setSize(200, 250);
		setResizable(false);
		setTitle("Settings");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
