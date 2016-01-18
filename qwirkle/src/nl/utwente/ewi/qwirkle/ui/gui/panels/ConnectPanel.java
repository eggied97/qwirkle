package nl.utwente.ewi.qwirkle.ui.gui.panels;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConnectPanel extends JFrame {

	private JPanel contentPane;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConnectPanel frame = new ConnectPanel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ConnectPanel() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 163, 166);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = (String)JOptionPane.showInputDialog(contentPane, "Enter your name: ", "Name?", JOptionPane.PLAIN_MESSAGE);
				System.out.println(name);
			}
		});
		btnConnect.setBounds(10, 11, 124, 23);
		contentPane.add(btnConnect);
		
		JButton btnEnterQueues = new JButton("Enter Queue(s)");
		btnEnterQueues.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBox two = new JCheckBox("Two Players");
				JCheckBox three = new JCheckBox("Three Players");
				JCheckBox four = new JCheckBox("Four Players");
				String message = "Select which queue(s) you want to join";
				Object[] params = {message, two, three, four};
				int n = JOptionPane.showConfirmDialog(contentPane, params, "Queues?", JOptionPane.PLAIN_MESSAGE);
				boolean twoSel = two.isSelected();
				boolean threeSel = three.isSelected();
				boolean fourSel = four.isSelected();
			}
		});
		btnEnterQueues.setBounds(10, 45, 124, 23);
		contentPane.add(btnEnterQueues);
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnQuit.setBounds(10, 79, 124, 23);
		contentPane.add(btnQuit);
	}
}
