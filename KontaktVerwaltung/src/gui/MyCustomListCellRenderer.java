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

import classes.nObj;

public class MyCustomListCellRenderer implements ListCellRenderer<Object> {
	protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		Font theFont = null;
		Color theForeground = null;
		Icon theIcon = null;
		String theText = null;
		JPanel panel = new JPanel();
		JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected,
				cellHasFocus);

		if (value instanceof nObj) {
			nObj values = (nObj) value;
			theFont = values.getFont();
			theForeground = values.getColor();
			theIcon = values.getMyIcon();
			theText = values.getName();
		} else {
			theFont = list.getFont();
			theForeground = list.getForeground();
			theText = "";
		}
		if (!isSelected) {
			renderer.setForeground(theForeground);

		} else {
			renderer.setForeground(Color.LIGHT_GRAY);
			renderer.setBackground(new Color(0, 0, 0, 0));
			renderer.setBorder(null);
		}
		if (theIcon != null) {
			renderer.setIcon(theIcon);
		}
		renderer.setText(theText);
		renderer.setFont(theFont);
		renderer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		renderer.setBackground(new Color(0, 0, 0, 0));
		panel.setLayout(new FlowLayout(Label.LEFT));

		panel.setBorder(new MatteBorder(2, 0, 0, 0, Color.black));
		panel.add(renderer);
		return panel;
	}
}
