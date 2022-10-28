package add;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class MyIcon implements Icon {

	public MyIcon() {
	}

	public int getIconHeight() {
		return 54;
	}

	public int getIconWidth() {
		return 54;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {

		g.setColor(Color.DARK_GRAY);
		g.fillOval(42 / 2, 64 / 3, 30, 30);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(15, 44, 44, 18);
	}
}