package fhscqs;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;

public class QuestPanel extends JPanel {
	/*
	 * JPanel for maintaining the list of questions
	 */
	
	private static final long serialVersionUID = 7406145698400364161L;
	ArrayList<JPanel> actionList = new ArrayList<JPanel>();
	
	public QuestPanel() {
		
	}
	
	public void loadSet() {
		//Build GUI
		setLayout(new GridLayout(QuestSim.problemList.size(), 1));
		for(int i = 0; i < QuestSim.problemList.size(); i++) {
			JPanel p = new JPanel();
			p.setBorder(BorderFactory.createLineBorder(Color.black));
			p.setLayout(new FlowLayout());
			p.add(new JLabel(QuestSim.problemList.get(i).getIn().getName().substring(0, 6)));
			JLabel testComplete = new JLabel("Not Attempted");
			JLabel time = new JLabel("Time: -- s");
			JLabel attempts = new JLabel("Attempts: 0");
			QuestSim.problemList.get(i).testLabel = testComplete;
			QuestSim.problemList.get(i).timeLabel = time;
			QuestSim.problemList.get(i).attemptsLabel = attempts;
			p.add(QuestSim.problemList.get(i).getButton());
			p.add(testComplete);
			p.add(time);
			p.add(attempts);
			actionList.add(p);
			add(p);
			revalidate();
		}
	}
	public void destroySet() {
		//Remove GUI elements when a different problem directory is selected
		for(int i = 0; i < QuestSim.problemList.size(); i++) {
			remove(QuestSim.problemList.get(i).getButton());
		}
		for(JPanel p : actionList) remove(p);
	}
}
