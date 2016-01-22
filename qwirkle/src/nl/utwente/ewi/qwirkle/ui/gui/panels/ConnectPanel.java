package nl.utwente.ewi.qwirkle.ui.gui.panels;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import nl.utwente.ewi.qwirkle.callback.UserInterfaceCallback;
import nl.utwente.ewi.qwirkle.model.player.ComputerPlayer;
import nl.utwente.ewi.qwirkle.model.player.HumanPlayer;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.model.player.strategy.SuperStrategy;

import javax.swing.JButton;
import javax.swing.JCheckBox;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;

public class ConnectPanel extends JFrame {

	private JPanel contentPane;
	
	private JButton btnConnect;
	private JButton btnEnterQueues;
	private JButton btnQuit;
	
	private UserInterfaceCallback callback;
	

	/**
	 * Create the frame.
	 */
	public ConnectPanel(UserInterfaceCallback callback) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 169, 135);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		this.callback = callback;

		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		initButtons();
		
		setActionListeners();
			
	}
	
	private void initButtons(){
		btnEnterQueues = new JButton("Enter Queue(s)");		
		
		btnConnect = new JButton("Connect");
		
		btnQuit = new JButton("Quit");
		
		contentPane.add(btnConnect);
		contentPane.add(btnEnterQueues);
		contentPane.add(btnQuit);
		
		btnEnterQueues.setEnabled(false);
		btnConnect.setEnabled(true);
	}
	
	private void setActionListeners(){
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = (String) JOptionPane.showInputDialog(contentPane, "Enter your name: ", "Name?",
						JOptionPane.PLAIN_MESSAGE);
				
				Player p;
				
				if(name.equals("COMPUTERMAN")){
					p = new ComputerPlayer("pcman" + (int)(Math.random() * 4));
				}else if(name.equals("COMPUTERMANSLIM")){
					p =  new ComputerPlayer("pcmanslim" + (int)(Math.random() * 4), new SuperStrategy());
				}else{
					p = new HumanPlayer(name);
				}
				
				callback.login(p);
				
				btnEnterQueues.setEnabled(true);
				btnConnect.setEnabled(false);
			}
		});
		
		btnEnterQueues.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBox two = new JCheckBox("Two Players");
				JCheckBox three = new JCheckBox("Three Players");
				JCheckBox four = new JCheckBox("Four Players");
				String message = "Select which queue(s) you want to join";
				Object[] params = { message, two, three, four };
				int n = JOptionPane.showConfirmDialog(contentPane, params, "Queues?", JOptionPane.PLAIN_MESSAGE);
				boolean twoSel = two.isSelected();
				boolean threeSel = three.isSelected();
				boolean fourSel = four.isSelected();
				List<Boolean> queues = new ArrayList<>();
				queues.add(twoSel);
				queues.add(threeSel);
				queues.add(fourSel);
				
				int j = 0;
				for(int i = 0; i < queues.size(); i++) {
					if(queues.get(i)) {
						j++;
					}
				}
				
				int[] enteredQueues = new int[j];				
				j = 0;
				
				for(int i = 0; i < queues.size(); i++) {
					if(queues.get(i)) {
						enteredQueues[j] = i+2;
						j++;
					}
				}
				
				callback.queue(enteredQueues);
				
				btnEnterQueues.setEnabled(false);
				JOptionPane.showMessageDialog(contentPane, "You're added to the queue(s)" + "\n" + "Please wait for the game to start");
			}
		});
		
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
	
	/**
	 * Is called when the name that was entered was not valid / unique
	 * */
	public void resetName(){
		btnEnterQueues.setEnabled(false);
		btnConnect.setEnabled(true);
	}
	
	public void resetQueue(){
		btnEnterQueues.setEnabled(true);
	}
}
