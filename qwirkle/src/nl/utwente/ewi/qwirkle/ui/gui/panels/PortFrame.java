package nl.utwente.ewi.qwirkle.ui.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridBagConstraints;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class PortFrame extends JFrame {

	private JPanel contentPane;
	private BufferedImage qwirkle;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PortFrame frame = new PortFrame();
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
	public PortFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 349, 242);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{50, 150};
		gbl_contentPane.rowHeights = new int[]{0, 35, 0, 0, 35, 35};
		gbl_contentPane.columnWeights = new double[]{1.0};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0};
		contentPane.setLayout(gbl_contentPane);
		
		try {
			qwirkle = ImageIO.read(new File("Qwirkle.PNG"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JPanel panel = new JPanel() {
			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(qwirkle.getScaledInstance(qwirkle.getWidth()/2, qwirkle.getHeight()/2, 1), 0, 0, null);
            }
		};
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 2;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);
		
		JLabel insPort = new JLabel("Enter port number");
		insPort.setPreferredSize(new Dimension(150, 35));
		GridBagConstraints gbc_insPort = new GridBagConstraints();
		gbc_insPort.insets = new Insets(0, 0, 5, 5);
		gbc_insPort.gridx = 0;
		gbc_insPort.gridy = 1;
		contentPane.add(insPort, gbc_insPort);
		
		JTextField port = new JTextField();
		port.setPreferredSize(new Dimension(150, 35));
		GridBagConstraints gbc_port = new GridBagConstraints();
		gbc_port.insets = new Insets(0, 0, 5, 0);
		gbc_port.gridx = 1;
		gbc_port.gridy = 1;
		contentPane.add(port, gbc_port);
		
		JButton connect = new JButton("Connect to server!");
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		JLabel insIP = new JLabel("Enter IP address");
		insIP.setPreferredSize(new Dimension(150, 35));
		GridBagConstraints gbc_insIP = new GridBagConstraints();
		gbc_insIP.insets = new Insets(0, 0, 5, 5);
		gbc_insIP.gridx = 0;
		gbc_insIP.gridy = 3;
		contentPane.add(insIP, gbc_insIP);
		
		JTextField ip = new JTextField();
		ip.setPreferredSize(new Dimension(150, 35));
		GridBagConstraints gbc_ip = new GridBagConstraints();
		gbc_ip.insets = new Insets(0, 0, 5, 0);
		gbc_ip.gridx = 1;
		gbc_ip.gridy = 3;
		contentPane.add(ip, gbc_ip);
		connect.setPreferredSize(new Dimension(150, 35));
		GridBagConstraints gbc_connect = new GridBagConstraints();
		gbc_connect.gridheight = 2;
		gbc_connect.gridx = 0;
		gbc_connect.gridy = 4;
		gbc_connect.gridwidth = 2;
		contentPane.add(connect, gbc_connect);
	}

}
