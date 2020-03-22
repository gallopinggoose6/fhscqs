package fhscqs;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class consoleFrame extends JFrame {
	
	/**
	 * A JFrame to hold the console
	 */
	private static final long serialVersionUID = 5578341618345534439L;
	private static String consoleText = "";
	private JTextArea consoleArea = new JTextArea(6, 12);
	
	public consoleFrame() {
		//Build GUI
		try {
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		} catch (ClassNotFoundException e1) {} 
		catch (InstantiationException e1) {}
		catch (IllegalAccessException e1) {}
		catch (UnsupportedLookAndFeelException e1) {}
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		consoleArea.setEditable(false);
		JScrollPane aboutScrollPane = new JScrollPane(consoleArea);
		consoleArea.setText(consoleText);				
		this.setSize(300, 200);
		this.setTitle("Console");		
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		ActionButton clear = new ActionButton("Clear");
		ActionButton hide = new ActionButton("Hide");
		clear.setMethod(new ActionMethod() {
			@Override
			public void work() {
				consoleText = "";
				consoleArea.setText(consoleText);
				revalidate();
			}
		});
		hide.setMethod(new ActionMethod() {
			@Override
			public void work() {
				setVisible(false);
			}
		});
		
		buttonPanel.add(clear);
		buttonPanel.add(hide);
		
		this.setLayout(new BorderLayout());
		this.add(aboutScrollPane, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	public void updateFrame(String s) {
		//Add text to console
		consoleText += s;
		consoleText += "\n";
		consoleArea.setText(consoleText);
		this.revalidate();
	}
}
