package nl.utwente.ewi.qwirkle.server.model;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import nl.utwente.ewi.qwirkle.server.connect.Server;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class ServerGUI extends JFrame {

	private int port = 0;
	private JPanel contentPane;
	private JTextField textField;
	private Server server;
	private JButton btnStart;

	/**
	 * Create the frame.
	 */
	public ServerGUI(final Server serv) {
		this.server = serv;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		textField = new JTextField("Enter server port");
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(textField, BorderLayout.CENTER);
		textField.setColumns(1);
		
		btnStart = new JButton("Start server");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					port = Integer.parseInt(textField.getText());
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(contentPane, "Enter a valid port number", "Error!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		contentPane.add(btnStart, BorderLayout.SOUTH);
	}
	
	public void showRunning() {
		JOptionPane.showMessageDialog(contentPane, "The Server is running", "Running...!", JOptionPane.PLAIN_MESSAGE);
		btnStart.setEnabled(false);
	}
	
	public int getPort() {
		return port;
	}

}