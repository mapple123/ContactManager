package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import add.DeselectOnClickListener;
import add.Methods;
import add.MyIcon;
import classes.Contact;
import classes.nObj;
import res.Consts;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private Container mainpane;

	private JList<Object> listFiles;
	private JScrollPane scrollPane, scrollPaneDetails;

	private CustomMenu menuBar;
	private JPanelContactDetails jPanelContactDetails;

	private Dimension screenSize;
	private int width;
	private int height;
	private File file;
	private ArrayList<Contact> allContacts;

	private DefaultListModel model;

	private JSplitPane splitpane;
	private ResourceBundle bundle;
	public MainFrame(File file, ResourceBundle bundle) {
		super(bundle.getString(Consts.TITEL));
		setExtendedState(MAXIMIZED_BOTH);
		this.file = file;
		this.bundle = bundle;
		initComponents();
		setComponents();

		setSize(new Dimension(width, height));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		setVisible(true);
	}

	private void initComponents() {
		allContacts = Methods.readAllContacts(file);
		Object elements[] = getAllContacts();

		listFiles = new JList<Object>();
		model = new DefaultListModel<>();
		for (Object item : elements) {
			model.addElement(item);
		}

		listFiles.setModel(model);
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) screenSize.getWidth() / 2;
		height = (int) screenSize.getHeight() / 2;

		jPanelContactDetails = new JPanelContactDetails(listFiles, this, allContacts, false, model, bundle);

		mainpane = getContentPane();

	}

	private void setComponents() {
		mainpane.setLayout(new BorderLayout());
		mainpane.setSize(width, height);

		MyCustomListCellRenderer renderer = new MyCustomListCellRenderer();
		listFiles.setCellRenderer(renderer);

		scrollPane = new JScrollPane(listFiles);
		scrollPaneDetails = new JScrollPane(jPanelContactDetails);
		scrollPaneDetails.setVisible(false);

		splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, scrollPaneDetails);
		jPanelContactDetails.setScrollPane(scrollPaneDetails);
		jPanelContactDetails.setSplitPane(splitpane);
		listFiles.addMouseListener(new DeselectOnClickListener(jPanelContactDetails, splitpane, scrollPaneDetails));

		splitpane.setDividerSize(0);
		splitpane.setDividerLocation(scrollPane.getLocation().x + (int) (getContentPane().getSize().getWidth() / 2));// jPanelContactDetails.getPreferredSize().width
		listFiles.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				System.out.println("First index: " + listSelectionEvent.getFirstIndex());
				System.out.println(", Last index: " + listSelectionEvent.getLastIndex());
				boolean adjust = listSelectionEvent.getValueIsAdjusting();
				System.out.println(", Adjusting? " + adjust);
				if (!adjust) {
					jPanelContactDetails.setVisible(true);
					scrollPaneDetails.setVisible(true);
					JList list = (JList) listSelectionEvent.getSource();
					list.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
					int selections[] = list.getSelectedIndices();
					Object selectionValues[] = list.getSelectedValues();
					for (int i = 0, n = selections.length; i < n; i++) {
						if (i == 0) {
							System.out.println(" Selections: ");
						}
						System.out
								.println(selections[i] + "/" + ((nObj) selectionValues[i]).getContact().getId() + " ");

						
						System.out.println(((nObj) selectionValues[i]).getContact().toString());

						splitpane.setDividerSize(10);
						splitpane.setDividerLocation(
								scrollPane.getLocation().x + (int) (getContentPane().getSize().getWidth() / 2));// jPanelContactDetails.getPreferredSize().width
						System.out.println("WIDTH:" + mainpane.getWidth());

						if (i < n) {
							jPanelContactDetails.setContact(((nObj) selectionValues[i]).getContact());
							jPanelContactDetails.setAllData(((nObj) selectionValues[i]).getContact());
						}
							

					}
				}
			}
		});

		splitpane.setResizeWeight(0.5);

		menuBar = new CustomMenu(this, listFiles, allContacts, file.getAbsolutePath(), jPanelContactDetails, splitpane,
				scrollPaneDetails, bundle);
		JTextField jtfSearch = new JTextField();
		jtfSearch.setPreferredSize(new Dimension(300, 35));
		jtfSearch.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {

				searchFilter(jtfSearch.getText());
				if (jtfSearch.getText().length() == 0) {
					menuBar.setSearch(false);
				} else {
					menuBar.setSearch(true);
				}
				jPanelContactDetails.setVisible(false);
				scrollPaneDetails.setVisible(false);
				splitpane.setDividerSize(0);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {

				searchFilter(jtfSearch.getText());
				if (jtfSearch.getText().length() == 0) {
					menuBar.setSearch(false);
				} else {
					menuBar.setSearch(true);
				}
				jPanelContactDetails.setVisible(false);
				scrollPaneDetails.setVisible(false);
				splitpane.setDividerSize(0);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				searchFilter(jtfSearch.getText());
				if (jtfSearch.getText().length() == 0) {
					menuBar.setSearch(false);
				} else {
					menuBar.setSearch(true);

					jPanelContactDetails.setVisible(false);
					scrollPaneDetails.setVisible(false);
					splitpane.setDividerSize(0);
				}
			}
		});
		JButton btnSearch = new JButton("Suchen");
		btnSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				searchFilter(jtfSearch.getText());
				jPanelContactDetails.setVisible(false);
				scrollPaneDetails.setVisible(false);
				splitpane.setDividerSize(0);
			}
		});
		btnSearch.setPreferredSize(new Dimension(100, 35));
		JPanel panelNorthContainer = new JPanel();
		panelNorthContainer.setLayout(new FlowLayout(Label.LEFT));
		panelNorthContainer.add(jtfSearch);
		panelNorthContainer.add(btnSearch);

		mainpane.add(panelNorthContainer, BorderLayout.NORTH);
		mainpane.add(splitpane, BorderLayout.CENTER);
	}

	private Object[] getAllContacts() {
		this.allContacts = Methods.readAllContacts(file);
		Object elements[] = new Object[allContacts.size()];

		for (int i = 0; i < elements.length; i++) {
			Contact contact = allContacts.get(i);

			System.out.println(contact.getId());
			String firstName = allContacts.get(i).getFirstName();
			String lastName = allContacts.get(i).getLastName();
			elements[i] = new nObj(new Font("Arial", Font.PLAIN, 20), Color.BLACK, new MyIcon(),
					firstName + " " + lastName, contact);
		}

		return elements;
	}

	private void searchFilter(String searchItem) {
		DefaultListModel filteredItems = new DefaultListModel<>();
		this.allContacts = Methods.readAllContacts(file);

		for (int i = 0; i < allContacts.size(); i++) {
			Contact contact = allContacts.get(i);

			System.out.println(contact.getId());
			String firstName = allContacts.get(i).getFirstName();
			String lastName = allContacts.get(i).getLastName();
			nObj obj = new nObj(new Font("Arial", Font.PLAIN, 20), Color.BLACK, new MyIcon(),
					firstName + " " + lastName, contact);

			String name = contact.getFirstName().toLowerCase() + " " + contact.getLastName().toLowerCase();
			if (name.contains(searchItem.toLowerCase())) {
				filteredItems.addElement(obj);
			}
		}

		model = filteredItems;
		listFiles.setModel(model);
	}

}
