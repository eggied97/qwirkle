package nl.utwente.ewi.qwirkle.ui.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextArea textArea;
	private JLabel scoreboard;
	
	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	public JLabel getScoreboard() {
		return scoreboard;
	}

	public void setScoreboard(JLabel scoreboard) {
		this.scoreboard = scoreboard;
	}

	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
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
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 800);
		contentPane = new JPanel();
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 800, 200 };
		gbl_contentPane.rowHeights = new int[] {600, 200};
		gbl_contentPane.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		
		// BOARD PANEL
		
		JPanel panelBoard = new JPanel();
		GridBagConstraints gbc_panelBoard = new GridBagConstraints();
		gbc_panelBoard.fill = GridBagConstraints.BOTH;
		gbc_panelBoard.gridx = 0;
		gbc_panelBoard.gridy = 0;
		contentPane.add(panelBoard, gbc_panelBoard);

		
		// CHAT PANEL
		
		JPanel panelChat = new JPanel();
		GridBagConstraints gbc_panelChat = new GridBagConstraints();
		gbc_panelChat.fill = GridBagConstraints.BOTH;
		gbc_panelChat.gridx = 0;
		gbc_panelChat.gridy = 1;
		contentPane.add(panelChat, gbc_panelChat);
		
		GridBagLayout gbl_panelChat = new GridBagLayout();
		gbl_panelChat.columnWidths = new int[] {700, 100 };
		gbl_panelChat.rowHeights = new int[] { 150, 50 };
		gbl_panelChat.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panelChat.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panelChat.setLayout(gbl_panelChat);
		
		textArea = new JTextArea();
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.insets = new Insets(0, 0, 5, 5);
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 0;
		panelChat.add(textArea, gbc_textArea);
		textArea.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(textArea);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		gbc_scrollPane.gridwidth = 2;
		panelChat.add(scrollPane, gbc_scrollPane);

		final JTextField textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.insets = new Insets(0, 0, 0, 5);
		gbc_textField_1.gridx = 0;
		gbc_textField_1.gridy = 1;
		panelChat.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);

		JButton btnSend = new JButton("Send!");
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 0, 5);
		gbc_btnSend.gridx = 1;
		gbc_btnSend.gridy = 1;
		panelChat.add(btnSend, gbc_btnSend);

		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.append("You> " + textField_1.getText() + "\n");
				textField_1.setText("");;
			}
		});
		
		JRootPane rp = SwingUtilities.getRootPane(this);
		rp.setDefaultButton(btnSend);
		
		
		// SCORE PANEL
		
		JPanel panelScore = new JPanel();
		GridBagConstraints gbc_panelScore = new GridBagConstraints();
		gbc_panelScore.fill = GridBagConstraints.BOTH;
		gbc_panelScore.gridx = 1;
		gbc_panelScore.gridy = 1;
		contentPane.add(panelScore, gbc_panelScore);
		
		scoreboard = new JLabel();
		scoreboard.setMinimumSize(new Dimension(150, 150));
		scoreboard.setPreferredSize(new Dimension(150, 150));
		scoreboard.setMaximumSize(new Dimension(150, 150));
		scoreboard.setAlignmentX(CENTER_ALIGNMENT);
		scoreboard.setAlignmentY(TOP_ALIGNMENT);
		scoreboard.setText("MWHAHAHAHAHAHA");
		panelScore.add(scoreboard);
		
		
		
		
		
		// HAND PANEL

		JPanel panelHand = new JPanel();
		GridBagConstraints gbc_panelHand = new GridBagConstraints();
		gbc_panelHand.fill = GridBagConstraints.BOTH;
		gbc_panelHand.gridx = 1;
		gbc_panelHand.gridy = 0;
		contentPane.add(panelHand, gbc_panelHand);

	}
}
