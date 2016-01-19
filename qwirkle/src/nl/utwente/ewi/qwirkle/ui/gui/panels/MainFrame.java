package nl.utwente.ewi.qwirkle.ui.gui.panels;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
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

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

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
		setBounds(100, 100, 948, 687);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panelBoard = new JPanel();
		panelBoard.setBounds(0, 0, 797, 445);
		contentPane.add(panelBoard);

		JPanel panelChat = new JPanel();
		panelChat.setBounds(0, 441, 805, 207);
		contentPane.add(panelChat);

		JPanel panelHand = new JPanel();
		panelHand.setBounds(802, 0, 130, 648);
		contentPane.add(panelHand);
		panelChat.setLayout(null);

		final JTextField textField_1 = new JTextField();
		textField_1.setBounds(10, 174, 714, 20);
		panelChat.add(textField_1);
		textField_1.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(803, 168, -799, -167);
		panelChat.add(scrollPane);

		JRootPane rp = SwingUtilities.getRootPane(this);

		final JTextArea textArea = new JTextArea();
		textArea.setBounds(10, 5, 785, 158);
		panelChat.add(textArea);

		JButton btnSend = new JButton("Send!");
		btnSend.setBounds(734, 173, 61, 23);
		panelChat.add(btnSend);

		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.append(textField_1.getText());
			}
		});
		rp.setDefaultButton(btnSend);

	}
}
