package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.MatteBorder;

import classes.ContactExt;

/**
 * Custom Zelle für die Kontaktliste
 *
 * Entwickler: Jan Schwenger
 */
public class MyCustomListCellRenderer implements ListCellRenderer<Object> {
	protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		Font font;
		Color foreground;
		Icon icon = null;
		String text;
		JPanel panel = new JPanel();
		JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected,
				cellHasFocus);
		if (value instanceof ContactExt) {
			ContactExt values = (ContactExt) value;
			font = values.getFont();
			foreground = values.getColor();
			icon = values.getMyIcon();
			text = values.getName();
		} else {
			font = list.getFont();
			foreground = list.getForeground();
			text = "";
		}
		if (!isSelected) {
			renderer.setForeground(foreground);
		} else {
			renderer.setForeground(Color.LIGHT_GRAY);
			renderer.setBackground(new Color(0, 0, 0, 0));
			renderer.setBorder(null);
		}
		if (icon != null) {
			renderer.setIcon(icon);
		}
		renderer.setText(text);
		renderer.setFont(font);
		renderer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		renderer.setBackground(new Color(0, 0, 0, 0));
		panel.setLayout(new FlowLayout(Label.LEFT));
		panel.setBorder(new MatteBorder(2, 0, 0, 0, Color.black));
		panel.add(renderer);
		return panel;
	}
}
