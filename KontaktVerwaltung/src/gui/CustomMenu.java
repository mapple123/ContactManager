package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import add.Methods;
import classes.Contact;
import classes.nObj;

public class CustomMenu extends JMenuBar implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JMenuBar menuBar;
	private JMenu menu, menu2, menu3;
	private JMenuItem menuItem, menuItem2;

	private MainFrame frame;

	private String path;

	private JList<?> list;
	private ArrayList<Contact> allContacts;
	private JPanelContactDetails panelDetails;
	private JSplitPane splitPane;
	private JScrollPane scrollPaneDetails;

	private boolean search;

	public CustomMenu(MainFrame frame, JList<?> list, ArrayList<Contact> allContacts, String path,
			JPanelContactDetails panelDetails, JSplitPane splitPane, JScrollPane scrollPaneDetails) {
		this.path = path;
		this.frame = frame;
		this.list = list;
		this.allContacts = allContacts;
		this.panelDetails = panelDetails;
		this.splitPane = splitPane;
		this.scrollPaneDetails = scrollPaneDetails;

		menuBar = new JMenuBar();

		menu = new JMenu("Neu");
		menu.setFont(new Font("Arial", Font.PLAIN, 30));
		menu.setMnemonic(KeyEvent.VK_N);

		menuBar.add(menu);

		menu3 = new JMenu("Löschen");
		menu3.setFont(new Font("Arial", Font.PLAIN, 30));
		menu3.setMnemonic(KeyEvent.VK_L);

		menuBar.add(menu3);

		menuItem = new JMenuItem("Wirklich Löschen?", KeyEvent.VK_J);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, ActionEvent.ALT_MASK));

		menuItem.addActionListener(this);
		menu3.add(menuItem);

		menuItem2 = new JMenuItem("Neuer Kontakt", KeyEvent.VK_P);
		menuItem2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));

		menuItem2.addActionListener(this);
		menu.add(menuItem2);

		menu2 = new JMenu("Filter/Sortieren");
		menu2.setFont(new Font("Arial", Font.PLAIN, 30));
		menu2.setMnemonic(KeyEvent.VK_R);
		menu2.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
		menuBar.add(Box.createHorizontalGlue());

		menuBar.add(menu2);

		frame.setJMenuBar(menuBar);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == menuItem2) {
			frame.setEnabled(false);
			EditNewContactFrame eFrame = new EditNewContactFrame(frame, list, allContacts, search, null, null);
			eFrame.setContact(null);
		} else if (e.getSource() == menuItem) {
			if (list.getSelectedIndices().length > 0) {
				System.out.println("Löschen");
				ArrayList<nObj> li = new ArrayList<>();
				for (Object item : list.getSelectedValuesList()) {
					li.add((nObj) item);
				}
				System.out.println(li.size());
				Methods.deleteItemFile(path, li);
				DefaultListModel<Object> model = (DefaultListModel<Object>) list.getModel();

				if (list.getSelectedIndices().length > 0) {
					int[] alIn = list.getSelectedIndices();

					for (int i = alIn.length - 1; i >= 0; i--) {
						model.removeElementAt(alIn[i]);
					}
					list.clearSelection();
					panelDetails.setVisible(false);
					scrollPaneDetails.setVisible(false);
					splitPane.setDividerSize(0);
				}

			} else {
				JOptionPane.showMessageDialog(frame, "Achtung", "Kein Eintrag egal", JOptionPane.WARNING_MESSAGE);
			}
		}

	}

	public boolean isSearch() {
		return search;
	}

	public void setSearch(boolean search) {
		this.search = search;
	}

}
