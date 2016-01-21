package nl.utwente.ewi.qwirkle.ui.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.border.EmptyBorder;

import nl.utwente.ewi.qwirkle.model.Point;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.ScrollPaneConstants;
import java.awt.CardLayout;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextArea textArea;
	private JLabel scoreboard;
	private JLabel messageLabel;
	private List<JToggleButton> handTiles;
	private Map<JButton, Point> butCord;
	private JPanel buttons;

	public JLabel getMessageLabel() {
		return messageLabel;
	}

	public List<JToggleButton> getHandTiles() {
		return handTiles;
	}

	public void setMessageLabel(String message) {
		messageLabel.setText(message);
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(String message) {
		textArea.append(message + "\n");
	}

	public JLabel getScoreboard() {
		return scoreboard;
	}

	public void setScoreboard(JLabel scoreboard) {
		this.scoreboard = scoreboard;
	}

	public void addButton(JButton but) {
		Point p = butCord.get(but);
		int x = p.getX();
		int y = p.getY();
		Point left = new Point(x + 1, y);
		Point right = new Point(x - 1, y);
		Point up = new Point(x, y - 1);
		Point down = new Point(x, y + 1);
		boolean lft = true;
		boolean rght= true;
		boolean Up = true;
		boolean dwn = true;
		
		for (JButton b : butCord.keySet()) {
			if (butCord.get(b).equals(left)) {
				lft = false;
			}else if (butCord.get(b).equals(right)) {
				rght = false;
			}else if (butCord.get(b).equals(up)) {
				Up = false;
			}else if (butCord.get(b).equals(down)) {
				dwn = false;
			}
		}
		
		if(lft) {
			addButton(left);
		}
		if(rght) {
			addButton(right);
		}
		if(Up) {
			addButton(up);
		}
		if(dwn) {
			addButton(down);
		}

	}
	
	public void addButton(Point p) {
		int x = p.getX() - 144;
		int y = p.getY() - 144;
		JButton but = new JButton("(" + x + "," + y + ")");
		but.setPreferredSize(new Dimension(30,30));
		GridBagConstraints gbc_but = new GridBagConstraints();
		gbc_but.gridx = p.getX();
		gbc_but.gridy = p.getY();
		buttons.add(but, gbc_but);
		butCord.put(but, p);
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
		butCord = new HashMap<>();
		handTiles = new ArrayList<>();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 800);
		contentPane = new JPanel();
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 800, 200 };
		gbl_contentPane.rowHeights = new int[] { 600, 200 };
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

		buttons = new JPanel();
		JScrollPane boardScroll = new JScrollPane(buttons);
		GridBagLayout gbl_buttons = new GridBagLayout();
		buttons.setLayout(gbl_buttons);
		
		JButton start = (new JButton("(0,0)"));
		start.setPreferredSize(new Dimension(30,30));
		GridBagConstraints gbc_start = new GridBagConstraints();
		gbc_start.gridx = 144;
		gbc_start.gridy = 144;
		butCord.put(start, new Point(gbc_start.gridx, gbc_start.gridy));
		buttons.add(start, gbc_start);
		
		boardScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		boardScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		boardScroll.setPreferredSize(new Dimension(750, 550));
		panelBoard.add(boardScroll);

		// CHAT PANEL

		JPanel panelChat = new JPanel();
		GridBagConstraints gbc_panelChat = new GridBagConstraints();
		gbc_panelChat.fill = GridBagConstraints.BOTH;
		gbc_panelChat.gridx = 0;
		gbc_panelChat.gridy = 1;
		contentPane.add(panelChat, gbc_panelChat);

		GridBagLayout gbl_panelChat = new GridBagLayout();
		gbl_panelChat.columnWidths = new int[] { 700, 100 };
		gbl_panelChat.rowHeights = new int[] { 150, 50 };
		gbl_panelChat.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panelChat.rowWeights = new double[] { 0.0, 0.0 };
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
				if (!textField_1.getText().equals("")) {
					textArea.append("You> " + textField_1.getText() + "\n");
				}
				textField_1.setText("");
				;
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
		panelScore.add(scoreboard);

		// HAND PANEL

		JPanel panelHand = new JPanel();
		GridBagConstraints gbc_panelHand = new GridBagConstraints();
		gbc_panelHand.fill = GridBagConstraints.BOTH;
		gbc_panelHand.gridx = 1;
		gbc_panelHand.gridy = 0;
		contentPane.add(panelHand, gbc_panelHand);

		GridBagLayout gbl_panelHand = new GridBagLayout();
		gbl_panelHand.columnWidths = new int[] { 100, 100 };
		gbl_panelHand.rowHeights = new int[] { 475, 75, 50 };
		gbl_panelHand.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panelHand.rowWeights = new double[] { 0.0, 0.0, 0.0 };
		panelHand.setLayout(gbl_panelHand);

		JPanel panelTiles = new JPanel();
		GridBagConstraints gbc_panelTiles = new GridBagConstraints();
		gbc_panelTiles.fill = GridBagConstraints.BOTH;
		gbc_panelTiles.gridx = 0;
		gbc_panelTiles.gridy = 0;
		gbc_panelTiles.gridwidth = 2;
		panelHand.add(panelTiles, gbc_panelTiles);
		panelTiles.setLayout(new BoxLayout(panelTiles, BoxLayout.Y_AXIS));

		JToggleButton btnTile1 = new JToggleButton("");
		btnTile1.setAlignmentX(CENTER_ALIGNMENT);
		btnTile1.setMinimumSize(new Dimension(75, 75));
		btnTile1.setPreferredSize(new Dimension(75, 75));
		btnTile1.setMaximumSize(new Dimension(75, 75));
		panelTiles.add(btnTile1);

		JToggleButton btnTile2 = new JToggleButton("");
		btnTile2.setAlignmentX(CENTER_ALIGNMENT);
		btnTile2.setMinimumSize(new Dimension(75, 75));
		btnTile2.setPreferredSize(new Dimension(75, 75));
		btnTile2.setMaximumSize(new Dimension(75, 75));
		panelTiles.add(btnTile2);

		JToggleButton btnTile3 = new JToggleButton("");
		btnTile3.setAlignmentX(CENTER_ALIGNMENT);
		btnTile3.setMinimumSize(new Dimension(75, 75));
		btnTile3.setPreferredSize(new Dimension(75, 75));
		btnTile3.setMaximumSize(new Dimension(75, 75));
		panelTiles.add(btnTile3);

		JToggleButton btnTile4 = new JToggleButton("");
		btnTile4.setAlignmentX(CENTER_ALIGNMENT);
		btnTile4.setMinimumSize(new Dimension(75, 75));
		btnTile4.setPreferredSize(new Dimension(75, 75));
		btnTile4.setMaximumSize(new Dimension(75, 75));
		panelTiles.add(btnTile4);

		JToggleButton btnTile5 = new JToggleButton("");
		btnTile5.setAlignmentX(CENTER_ALIGNMENT);
		btnTile5.setMinimumSize(new Dimension(75, 75));
		btnTile5.setPreferredSize(new Dimension(75, 75));
		btnTile5.setMaximumSize(new Dimension(75, 75));
		panelTiles.add(btnTile5);

		JToggleButton btnTile6 = new JToggleButton("");
		btnTile6.setAlignmentX(CENTER_ALIGNMENT);
		btnTile6.setMinimumSize(new Dimension(75, 75));
		btnTile6.setPreferredSize(new Dimension(75, 75));
		btnTile6.setMaximumSize(new Dimension(75, 75));
		panelTiles.add(btnTile6);

		handTiles.add(btnTile1);
		handTiles.add(btnTile2);
		handTiles.add(btnTile3);
		handTiles.add(btnTile4);
		handTiles.add(btnTile5);
		handTiles.add(btnTile6);

		messageLabel = new JLabel();
		GridBagConstraints gbc_messageLabel = new GridBagConstraints();
		gbc_messageLabel.fill = GridBagConstraints.BOTH;
		gbc_messageLabel.gridx = 0;
		gbc_messageLabel.gridy = 1;
		gbc_messageLabel.gridwidth = 2;
		panelHand.add(messageLabel, gbc_messageLabel);

		JButton btnTrade = new JButton("Trade");
		GridBagConstraints gbc_btnTrade = new GridBagConstraints();
		gbc_btnTrade.gridx = 0;
		gbc_btnTrade.gridy = 2;
		gbc_btnTrade.anchor = GridBagConstraints.NORTH;
		panelHand.add(btnTrade, gbc_btnTrade);

		JButton btnMove = new JButton("Move");
		GridBagConstraints gbc_btnMove = new GridBagConstraints();
		gbc_btnMove.gridx = 1;
		gbc_btnMove.gridy = 2;
		gbc_btnMove.anchor = GridBagConstraints.NORTH;
		panelHand.add(btnMove, gbc_btnMove);

	}
}
