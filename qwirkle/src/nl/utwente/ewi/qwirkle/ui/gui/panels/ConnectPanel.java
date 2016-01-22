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
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;

public class ConnectPanel extends JFrame {

	private JPanel contentPane;
	private String name;
	private boolean nameSet = false;
	private boolean queueSet = false;
	private List<Boolean> queues;
	
	
	public boolean isQueueSet() {
		return queueSet;
	}

	public void setQueueSet(boolean queueSet) {
		this.queueSet = queueSet;
	}

	public boolean isNameSet() {
		return nameSet;
	}

	public void setNameSet(boolean nameSet) {
		this.nameSet = nameSet;
	}

	public void setName(String name) {
		this.name = name;
	}


	public List<Boolean> getQueues() {
		return queues;
	}

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
		setBounds(100, 100, 169, 135);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		final JButton btnEnterQueues = new JButton("Enter Queue(s)");
		btnEnterQueues.setEnabled(false);
		
		final JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = (String) JOptionPane.showInputDialog(contentPane, "Enter your name: ", "Name?",
						JOptionPane.PLAIN_MESSAGE);
				
				
				
				
				setName(name);
				setNameSet(true);
				btnEnterQueues.setEnabled(true);
				btnConnect.setEnabled(false);
			}
		});
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		contentPane.add(btnConnect);

		
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
				queues = new ArrayList<>();
				queues.add(twoSel);
				queues.add(threeSel);
				queues.add(fourSel);
				setQueueSet(true);
				btnEnterQueues.setEnabled(false);
				JOptionPane.showMessageDialog(contentPane, "You're added to the queue(s)" + "\n" + "Please wait for the game to start");
			}
		});
		contentPane.add(btnEnterQueues);

		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		contentPane.add(btnQuit);
	}

	public String getName() {
		return this.name;
	}
}
