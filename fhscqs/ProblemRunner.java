package fhscqs;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import javax.swing.JLabel;

public class ProblemRunner implements Runnable {
	
	/*
	 * Independent threads for executing student-written code and then testing it. 
	 */
	
	private String[] c;
	private long time = 0;
	private ArrayList<String> outputLines = new ArrayList<String>();
	private File in;
	private File out;
	private JLabel sLabel, tLabel;
	
	public ProblemRunner(String[] command, File input, File output, JLabel lstatus, JLabel ltime) {
		this.c = command;
		this.in = input;
		this.out = output;
		this.sLabel = lstatus;
		this.tLabel = ltime;
	}
	public void run() {
		try{
			//Create student process and add listeners
        	time = System.currentTimeMillis();
        	Process p = Runtime.getRuntime().exec(c);

            InputStream stdout = p.getInputStream();
            InputStream stderr = p.getErrorStream();
            OutputStream stdin = p.getOutputStream();

            StreamListener stdoutReader = new StreamListener(stdout, "stdout");
            StreamListener stderrReader = new StreamListener(stderr, "stderr");

            Thread t_stdoutReader = new Thread(stdoutReader);
            Thread t_stderrReader = new Thread(stderrReader);
            
            //Start listening threads
            t_stdoutReader.start();
            t_stderrReader.start();
            
            //Feed input into student program
            PrintWriter pw = new PrintWriter(stdin);
            Scanner inputFileScanner = new Scanner(in);
            while(inputFileScanner.hasNext()) {
            	pw.println(inputFileScanner.nextLine());
            }
            pw.flush();
        	pw.close();
            inputFileScanner.close();
            
            //kill process if it runs for too long
            p.waitFor(180, TimeUnit.SECONDS);
            p.destroy();
        }catch(IOException | InterruptedException n){
            JOptionPane.showMessageDialog(QuestSim.frame, "I/O Exception: " + n.getLocalizedMessage(), "INTERNAL ERROR", JOptionPane.ERROR_MESSAGE);
        }

	}
	private class StreamListener implements Runnable{
        private Scanner outputScanner;
        private String name;

        public StreamListener(InputStream s, String n){
        	outputScanner = new Scanner(new InputStreamReader(s));
        	name = n;
        }
        @Override
        public void run(){
        	if(name.contentEquals("stdout")) outputLines.clear();
            String line;
            while(outputScanner.hasNext()){
            	//Write to console
				line = outputScanner.nextLine();
				if(name.equals("stdout")) outputLines.add(line);
			    if(name.equals("stdout") && QuestSim.enableProgramOutput) QuestSim.cf.updateFrame("stdout: " + line);
			    if(name.equals("stderr") && QuestSim.enableErrorOutput) QuestSim.cf.updateFrame("stderr: " + line);
			}
            if(name.equals("stdout")) {
            	//monitor time of program (should only occur once)
            	time = System.currentTimeMillis() - time;
            	time = time / 1000;
            }
            if(time > 179 && name.equals("stdout")) {
        		JOptionPane.showMessageDialog(QuestSim.frame, "Solution took too long. Either an infinite loop or inefficient solution", "Test Failed:", JOptionPane.ERROR_MESSAGE);
        		reset(false);
       			return;
        	}
            LinkedList<String> keyList = new LinkedList<String>();
            if(name.equals("stdout")) {
            	//Test program output for inconsistencies
            	Scanner keyScanner;
    			try {
    				keyScanner = new Scanner(out);
    				while(keyScanner.hasNext()) {
    					String keyline = keyScanner.nextLine();
    					keyList.add(keyline);
            			if(QuestSim.enableKeyDisplay) QuestSim.cf.updateFrame("Key:" + keyline);
    				}
    				keyScanner.close();
    			} catch (FileNotFoundException e1) {
    				e1.printStackTrace();
    			}
           		if(outputLines.size() < 1) {	//check if the same amount of output has been generated
           			JOptionPane.showMessageDialog(QuestSim.frame, "No output, probably a runtime error.", "Test Failed:", JOptionPane.ERROR_MESSAGE);
           			reset(false);
           			return;
           		}
            	for(String s : outputLines) {
            		if(keyList.isEmpty()) {
            			JOptionPane.showMessageDialog(QuestSim.frame, "Inconsistency in outputs: too much output", "Test Failed:", JOptionPane.ERROR_MESSAGE);
            			reset(false);
            			return;
            		}
            		String newline;
            		if (!s.equals(newline = keyList.pop())) {	//check the output to make sure it's correct
            			if(QuestSim.enableKeyComparison) QuestSim.cf.updateFrame("INCONSISTENCY key:" + newline + " actual:" + s);
            			JOptionPane.showMessageDialog(QuestSim.frame, "Inconsistency in Outputs: Incorrect Output", "Test Failed:", JOptionPane.ERROR_MESSAGE);
            			reset(false);
            			return;
            		}
            	}
            	if (!keyList.isEmpty()) {
           			JOptionPane.showMessageDialog(QuestSim.frame, "Inconsistency in outputs: not enough output", "Test Failed:", JOptionPane.ERROR_MESSAGE);
           			reset(false);
           			return;
            	}
            	JOptionPane.showMessageDialog(QuestSim.frame, "Test Passed, onto the next problem!\nExecution time was approximately: " + time + " second(s)", "Test Succeeded", JOptionPane.INFORMATION_MESSAGE);
            	reset(true);
            }
        }
        void reset(Boolean success) { //code for re-enabling the button for testing
        	 for (Problem p : QuestSim.problemList) p.getButton().setEnabled(true);
        	 tLabel.setText("Time: " + time + " s");
        	 if (success) {
        		 sLabel.setText("Completed");
        		 sLabel.setForeground(Color.GREEN);
        	 } else {
        		 sLabel.setText("Failed");
        		 sLabel.setForeground(Color.RED);
        	 }
        }
    }
}
