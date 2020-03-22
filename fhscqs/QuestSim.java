package fhscqs;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class QuestSim {
	/*
	 * Entry point for the application. This class is responsible for keeping track of the primary JFrame used in the application. 
	 */
	
	//Define global variables
	public static VarContainer<String> inputDirectories = new VarContainer<String>("");
	public static VarContainer<String> outputDirectories = new VarContainer<String>("");
	public static ArrayList<Problem> problemList = new ArrayList<Problem>();
	public static VarContainer<String> scriptFilePath = new VarContainer<String>("");	//Note for the future, maybe make this not-static
	public static VarContainer<String> environmentPath = new VarContainer<String>("");
	
	static QuestPanel questPanel = new QuestPanel();	//Also try to make this non-static
	static JScrollPane questScrollPanel = new JScrollPane(questPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
	static JFrame frame = new JFrame();
	static consoleFrame cf = new consoleFrame();

	//Settings
	public static Boolean enableProgramOutput = false;
	public static Boolean enableErrorOutput = false;
	public static Boolean enableKeyComparison = false;
	public static Boolean enableKeyDisplay = false;
	public static Boolean custom = false;
	public static Boolean compiled = false;
	
	public static void main(String[] args) {
		//Build GUI
		JMenuBar mb = new JMenuBar();
		JMenu options = new JMenu("Options");
		JMenu help =  new JMenu("Help");
		JMenuItem settings  = new JMenuItem("Settings");
		JMenuItem about = new JMenuItem("About");
		JMenuItem exit = new JMenuItem("Exit");
		JMenuItem export = new JMenuItem("Export Results");
		JMenuItem instructHelp = new JMenuItem("Help");
		JMenuItem showConsole = new JMenuItem("Show Console");
		
		//implement system theming (looks so much better than the default swing theme)
		try {
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		} catch (ClassNotFoundException e1) {} 
		catch (InstantiationException e1) {}
		catch (IllegalAccessException e1) {}
		catch (UnsupportedLookAndFeelException e1) {}
		
		//Give actions to Menu Items
		settings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Settings();
			}
		});
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Create new JFrame with content
				JFrame aboutFrame = new JFrame();
				aboutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				JTextArea aboutArea = new JTextArea(6, 12);
				aboutArea.setEditable(false);
				JScrollPane aboutScrollPane = new JScrollPane(aboutArea);
				aboutArea.setText("INSPIRE Version 1.0 Multi-Platform\n"
						+ "Developed by Tyler Renken.\n"
						+ "Made for the Fairview High School Code Quest Team\n"
						+ "Rights and Source Code are Property of FHS, (C) 2020\n"
						+ "Send questions to gallopinggoose6@outlook.com");
				aboutFrame.setSize(300, 200);
				aboutFrame.add(aboutScrollPane);
				aboutFrame.setTitle("About INSPIRE");
				aboutFrame.setLocationRelativeTo(null);
				aboutFrame.setVisible(true);
			}
		});
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		export.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (problemList.isEmpty()) {
					JOptionPane.showMessageDialog(frame, "Error: No data to export", "Error:", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				//Define strings
				JFileChooser jfc = new JFileChooser("Save To");
				String exportPath = "";
				String exportName = "INSPIRE_Export_";
				
				//Get place user wants to save file
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int selected = jfc.showOpenDialog(frame);
				if (selected == JFileChooser.APPROVE_OPTION) exportPath = jfc.getSelectedFile().getAbsolutePath();
				else return;
				
				//Create File Name
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
				LocalDateTime now = LocalDateTime.now();
				exportName += dtf.format(now) + ".csv";
				
				File tempDirectory = new File(exportPath);
				File export = new File(tempDirectory, exportName);
				
				//create the file
				try {
					export.createNewFile();
					FileWriter w = new FileWriter(export);
					JOptionPane.showMessageDialog(frame, "Created File: " + exportName + " in directory " + exportPath);
					w.write("Problem,Status,Attempts,Time\n");
					for (Problem p : problemList) {
						//writes data in a csv format.
						w.write(p.getIn().getName().substring(0, 6) + "," + p.testLabel.getText() + "," + p.attemptsLabel.getText().substring(10) + "," + p.timeLabel.getText().substring(6) + "\n");
					}
					w.flush();
					w.close();
				} catch (IOException e1) {JOptionPane.showMessageDialog(frame, "Unable to create file", "Error:", JOptionPane.ERROR_MESSAGE);}
			}
		});
		//create new JFrame
		instructHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame aboutFrame = new JFrame();
				aboutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				JTextArea aboutArea = new JTextArea(6, 12);
				aboutArea.setEditable(false);
				JScrollPane aboutScrollPane = new JScrollPane(aboutArea);
				aboutArea.setText("Basic how to This Program:\n"
						+ "1. Download pprevious problems from Lockheed Martin:\n"
						+ "https://www.lockheedmartin.com/en-us/who-we-are/communities/codequest/code-quest-past-quests/codequest-2019.html\n"
						+ "\n2. Extract all Files\n"
						+ "\n3. Use the first Browse Button to find the directorie for the Input Files (Judging or Official Inputs)\n"
						+ "\n4. Use the second Browse Button to find the directorie for the Output Files (Judging or Official Outputs)\n"
						+ "\n5. Press Select, and the problems should load, if not double check the directory paths.\n"
						+ "\n6. Select an execution environment: (get a teacher's help) browse (using the 4th button) for the execution file of the interpreter\n"
						+ "    (java.exe for java, python3.exe for python). Wherever the virtual machine program is located\n"
						+ "\n7. Choose a problem to practice and write a program to solve the problem.\n"
						+ "\n8. Use the 3rd Browse Button to get the path of your program source code.\n"
						+ "\n9. Press the \"Test\" Button next to the desired problem to test your code.\n"
						+ "\n10. After the program runs, learn about its performance from the readouts given.\n"
						+ "\n\nAdvanced:\n\n"
						+ "In Settings (available from the MenuBar -> Options), Enabling competition mode sets the readouts\n"
						+ "to reflect the information available during the actual competition.\n"
						+ "WARNING: This is NOT an accurate approximation of the competition. DO NOT ASSUME that information\n"
						+ "given by the program in competition mode will be available at the actual competition.\n"
						+ "\nDisabling Competition Mode gives a variety of options:\n"
						+ "> Seeing the program output in the console\n"
						+ "> Seeing the stderr output in the console\n"
						+ "> Seeing the input file in the console\n"
						+ "> Seeing the failed comparison between expected and actual output.\n"
						+ "These options allow the user to more easily learn about and adapt their programs.\n\n"
						+ "Export Results generates a Comma-Separated-Values file (.csv) that can be opened\n"
						+ "in most spreadsheet programs to help you better keep track of your progress in\n"
						+ "preparing for the competition.\n\n"
						+ "Good Luck!");
				aboutFrame.setSize(300, 200);
				aboutFrame.add(aboutScrollPane);
				aboutFrame.setTitle("Help");
				aboutFrame.setLocationRelativeTo(null);
				aboutFrame.setVisible(true);
			}
		});
		//makes the console visible
		showConsole.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cf.setVisible(true);
			}
		});
		
		//build GUI
		options.add(settings);
		options.add(export);
		options.add(showConsole);
		options.add(exit);
		help.add(instructHelp);
		help.add(about);
		mb.add(options);
		mb.add(help);
		frame.setJMenuBar(mb);
		
		frame.setLayout(new BorderLayout());
		
		JPanel directoryPanel = new JPanel();
		JPanel directoryBrowsePanel = new JPanel();
		JPanel directorySelectPanel = new JPanel();
		
		directoryBrowsePanel.setLayout(new GridLayout(2, 1));
		addDirectoryPanel(directoryBrowsePanel, inputDirectories, "Input Files", JFileChooser.DIRECTORIES_ONLY);
		addDirectoryPanel(directoryBrowsePanel, outputDirectories, "Output Files", JFileChooser.DIRECTORIES_ONLY);
		
		directorySelectPanel.setLayout(new BorderLayout());
		ActionButton select = new ActionButton("Select");
		select.setMethod(new ActionMethod() {
			@Override
			public void work() {
				try {
					if (inputDirectories.getVar() == "" || outputDirectories.getVar() == "") {
						JOptionPane.showMessageDialog(frame, "Mising path for an input or output folder.", "Error:", JOptionPane.ERROR_MESSAGE);
						return;              
					}
					if (inputDirectories.getVar().contentEquals(outputDirectories.getVar())) {
						JOptionPane.showMessageDialog(frame, "Input file Directory and Output File Directory is the same", "Error:", JOptionPane.ERROR_MESSAGE);
						return;
					}
					//Search directories and load files
					File inDirect = new File(inputDirectories.getVar());
					File outDirect = new File(outputDirectories.getVar());
					File[] inList = inDirect.listFiles();
					File[] outList = outDirect.listFiles();
					Arrays.sort(inList);
					Arrays.sort(outList);
					if(inList.length != outList.length) {
						JOptionPane.showMessageDialog(frame, "Files in directories do not sync", "Error:", JOptionPane.ERROR_MESSAGE);
						return;
					}
					questPanel.destroySet();
					problemList.clear();
					for(int i = 0; i < inList.length; i++) {
						problemList.add(new Problem(i, inList[i], outList[i]));
					}
					questPanel.loadSet();
				}
				catch (Exception ee) {
					JOptionPane.showMessageDialog(frame, "Directories could not be read.", "Error:", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		//Build GUI
		directorySelectPanel.add(select, BorderLayout.CENTER);
		
		directoryPanel.setLayout(new FlowLayout());
		directoryPanel.add(directoryBrowsePanel);
		directoryPanel.add(directorySelectPanel);
		
		frame.add(directoryPanel, BorderLayout.NORTH);
		
		JPanel backPanel = new JPanel();
		JPanel programsPanel = new JPanel();
		programsPanel.setLayout(new GridLayout(2, 1));
		backPanel.setLayout(new BorderLayout());
		
		addDirectoryPanel(programsPanel, scriptFilePath, "Program File", JFileChooser.FILES_ONLY);
		addDirectoryPanel(programsPanel, environmentPath, "Environment Or Compiler", JFileChooser.FILES_ONLY);
		
		backPanel.add(programsPanel, BorderLayout.NORTH);
		backPanel.add(questScrollPanel);
		
		frame.add(backPanel);
		
		frame.setTitle("INSPIRE");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 400);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	private static void addDirectoryPanel(JPanel f, VarContainer<String> directories, String name, int mode) {
		//GUI Builder to reduce redundant code
		JPanel directPanel = new JPanel();
		JTextField ta = new JTextField();
		ta.setColumns(25);
		ActionButton browse = new ActionButton("Browse");
		JLabel inputText = new JLabel(name);
		browse.setMethod(new ActionMethod() {
			@Override
			public void work() {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(mode);
				int selected = jfc.showOpenDialog(f);
				if (selected == JFileChooser.APPROVE_OPTION) {
					ta.setText(jfc.getSelectedFile().getAbsolutePath());
					directories.setVar(jfc.getSelectedFile().getAbsolutePath());
					ta.setColumns(30);
				}
			}
		});
		//Change directory based on changes in the text boxes
		ta.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				react();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				react();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				react();
			}
			private void react() {
				directories.setVar(ta.getText());
			}
		});
		
		directPanel.setLayout(new FlowLayout());
		directPanel.add(inputText);
		directPanel.add(browse);
		directPanel.add(ta);
		
		f.add(directPanel);
	}
}