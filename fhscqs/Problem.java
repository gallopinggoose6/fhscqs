package fhscqs;

import java.io.*;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Problem {
	/*
	 * SubGui added onto the QuestPanel. This class is responsible for fetching information from QuestSim and spawnign ProblemRunner threads.
	 */
	
	private int num;
	private File in;
	private File out;
	private ActionButton b = new ActionButton("Test");
	private int attempts = 0;
	public JLabel testLabel, timeLabel, attemptsLabel;
	
	long time = 0;
	
	public Problem(int n, File i, File o) {
		this.num = n;
		this.in = i;
		this.out = o;
		b.setLayout(null);
		b.setMethod(new ActionMethod() {
			@Override
			public void work() {
				attempts++;
		        attemptsLabel.setText("Attempts: " + attempts);
				start();
			}
		});
	}
	public int getNum() {return num;}
	public File getIn() {return in;}
	public File getOut() {return out;}
	public ActionButton getButton() {return b;}
	
	private void start(){
		//Collect file information and pass it to problem runner
		File scriptFileTest = new File(QuestSim.scriptFilePath.getVar());
		File executionEnvironmentTest = new File(QuestSim.environmentPath.getVar());
		
		Boolean kill = false;
		if(!scriptFileTest.exists()) {
			JOptionPane.showMessageDialog(QuestSim.frame, "Execution File does not exist", "Error:", JOptionPane.ERROR_MESSAGE);
			kill = true;
		}
		if(!executionEnvironmentTest.exists() && !QuestSim.compiled) {
			JOptionPane.showMessageDialog(QuestSim.frame, "Execution Environment does not exist", "Error:", JOptionPane.ERROR_MESSAGE);
			kill = true;
		}
		if(kill) return;
		
		ProblemRunner pr;
		
		if (!QuestSim.compiled) {
			String[] command = {QuestSim.environmentPath.getVar(), QuestSim.scriptFilePath.getVar()};
			pr = new ProblemRunner(command, in, out, testLabel, timeLabel);
		} else {
			String[] command = {QuestSim.scriptFilePath.getVar()};
			pr = new ProblemRunner(command, in, out, testLabel, timeLabel);
		}
		
        for (Problem p : QuestSim.problemList) p.getButton().setEnabled(false);
        
        Thread t = new Thread(pr);
        t.start();
    }
}
