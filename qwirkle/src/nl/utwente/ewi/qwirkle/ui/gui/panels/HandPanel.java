package nl.utwente.ewi.qwirkle.ui.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import nl.utwente.ewi.qwirkle.model.player.Player;

public class HandPanel extends JPanel {

	Player linkedPlayer;
	JButton[] buttonsForTilesInHand = new JButton[6];
	ActionListener AL;

	private static final String EMPTY_STRING = "empty";

	public HandPanel(Player p, ActionListener AL) {
		this.linkedPlayer = p;
		this.AL = AL;

		initPanel();

	}

	private void initPanel() {
		initButtons();

		refreshHand();
	}

	private void initButtons() {
		for (int i = 0; i < 6; i++) {
			buttonsForTilesInHand[i] = new JButton();
			buttonsForTilesInHand[i].addActionListener(this.AL);
		}
	}

	private void refreshHand() {
		for (int i = 0; i < 6; i++) {
			if (this.linkedPlayer.getHand().size() <= i) {
				buttonsForTilesInHand[i].setText("EMPTY_STRING");
			} else {
				buttonsForTilesInHand[i].setText(this.linkedPlayer.getHand().get(i).getHumanReadableString());
				buttonsForTilesInHand[i].setActionCommand("" + this.linkedPlayer.getHand().get(i).getIntOfTile());
			}
		}
	}

}
