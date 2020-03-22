package fhscqs;

import javax.swing.*;
import java.awt.event.*;

public class ActionButton extends JButton {
	/**
	 * Button to replace JButton that allows better duplication of modified button.
	 * So far, I have changed the JButtons so that when one is tab selected, the enter key will trigger it.
	 */
	private static final long serialVersionUID = 5900848439059029932L;
	private ActionMethod am = new ActionMethod();
	public ActionButton(String name) {
		this.setText(name);
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				perform();
			}
		});
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) perform();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent arg0) {}
			
		});
	}
	public void setMethod(ActionMethod newam) {
		am = newam;
	}
	
	public void perform() {
		am.work();
	}
}
