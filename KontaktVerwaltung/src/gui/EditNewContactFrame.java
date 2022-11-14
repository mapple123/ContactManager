package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import add.JTextFieldLimit;
import add.Methods;
import add.MyIcon;
import classes.Contact;
import classes.nObj;
import main.MainMethod;

public class EditNewContactFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private MainFrame frame;
	private JList list;
	private ArrayList<Contact> alContacts;
	private boolean search;
	private final String[] LABELS = { "Vorname*:", "Nachname*:", "Straße/Nr.:", "PLZ/Ort:", "Land:", "Telefon/Handy:",
			"E-Mail:" };
	private JLabel[] labels = new JLabel[7];
	private JTextField[] fields = new JTextField[7];
	private JPanel[] wrapperPanels = new JPanel[7];
	private Contact contact;

	private JSplitPane splitPane;
	private JScrollPane scrollPaneDetails;

	private JPanelContactDetails contactDetails;

	private DefaultListModel model;

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public EditNewContactFrame(MainFrame frame, JList list, final ArrayList<Contact> allContacts, boolean search,
			Contact contact, final DefaultListModel model, ResourceBundle bundle) {
		super();
		this.search = search;
		this.list = list;
		this.frame = frame;
		this.alContacts = allContacts;
		this.model = model;

		buildPanel(contact);

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String firstName = fields[0].getText().replace(",", "").trim();
				String lastName = fields[1].getText().replace(",", "").trim();
				String street = fields[2].getText().replace(",", "").replace(";", "").trim();
				String ort = fields[3].getText().replace(",", "").replace(";", "").trim();
				String country = fields[4].getText().replace(",", "").replace(";", "").trim();
				String phone = fields[5].getText().replace(",", "").trim();
				String mail = fields[6].getText().replace(",", "").trim();

				if (contact == null) {
					if ((firstName.length() == 0 && lastName.length() == 0))
						JOptionPane.showMessageDialog(EditNewContactFrame.this,
								"Die mit * gekenntzeichneten Felder müssen ausgefüllt werden!", "Achtung",
								JOptionPane.WARNING_MESSAGE);
					else {
						Contact contact = Methods.saveContact(
								new Contact(firstName, lastName, street + ";" + ort + ";" + country, phone, mail));

						if (!search) {
							DefaultListModel<Object> model = (DefaultListModel<Object>) list.getModel();
							model.addElement(new nObj(new Font("Arial", Font.PLAIN, 20), Color.BLACK, new MyIcon(),
									contact.getFirstName() + " " + contact.getLastName(), contact));
						}
						System.out.println("Test");
						frame.setEnabled(true);

						EditNewContactFrame.this.dispose();
					}

				} else if (contact != null) {
					if ((firstName.length() == 0 && lastName.length() == 0))
						JOptionPane.showMessageDialog(EditNewContactFrame.this,
								"Die mit * gekenntzeichneten Felder müssen ausgefüllt werden!", "Achtung",
								JOptionPane.WARNING_MESSAGE);
					else {
						System.out.println("Gespeichert:" + street);
						File f = new File(MainMethod.userHomeDir + MainMethod.DIR + MainMethod.FILE);
						Methods.editItem(f.getAbsolutePath(), new Contact(firstName, lastName,
								street + ";" + ort + ";" + country, phone, mail, contact.getId()));
						frame.setEnabled(true);

						DefaultListModel filteredItems = new DefaultListModel<>();
						EditNewContactFrame.this.alContacts = Methods
								.readAllContacts(new File(MainMethod.userHomeDir + MainMethod.DIR + MainMethod.FILE));

						for (int i = 0; i < alContacts.size(); i++) {
							Contact contact = alContacts.get(i);

							System.out.println(contact.getId());
							String firstNameS = alContacts.get(i).getFirstName();
							String lastNameS = alContacts.get(i).getLastName();
							nObj obj = new nObj(new Font("Arial", Font.PLAIN, 20), Color.BLACK, new MyIcon(),
									firstNameS + " " + lastNameS, contact);

							filteredItems.addElement(obj);
						}

						DefaultListModel model = (DefaultListModel) list.getModel();
						model = filteredItems;
						list.setModel(model);
						list.validate();
						contactDetails.setVisible(false);
						scrollPaneDetails.setVisible(false);
						splitPane.setDividerSize(0);
						
						EditNewContactFrame.this.dispose();
					}
				}

			}
		});

		JButton btnCancel = new JButton("Abbrechen");
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setEnabled(true);

				EditNewContactFrame.this.dispose();
			}
		});

		JPanel panelWrapper = new JPanel();
		panelWrapper.setLayout(new GridLayout());

		JPanel panelHolderSave = new JPanel();
		panelHolderSave.setLayout(new FlowLayout(Label.RIGHT));

		JPanel panelHolderCancel = new JPanel();
		panelHolderCancel.setLayout(new FlowLayout(Label.LEFT));

		panelHolderSave.add(btnOk);
		panelHolderCancel.add(btnCancel);

		panelWrapper.add(panelHolderCancel);
		panelWrapper.add(panelHolderSave);

		add(panelWrapper, BorderLayout.SOUTH);

		WindowListener listener = new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				frame.setEnabled(true);
			}
		};
		addWindowListener(listener);
		setSize(new Dimension(750, 500));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	private void buildPanel(Contact contact) {
		setComp();
		if (contact == null) {
			setTitle("Neuer Benutzer");

		} else {
			setTitle("Benutzer bearbeiten");
			setAllData(contact);
		}
	}

	private void setComp() {
		JPanel container = new JPanel();

		for (int i = 0; i < labels.length; i++) {
			labels[i] = new JLabel(LABELS[i]);
			fields[i] = new JTextField();

			fields[i].setDocument(new JTextFieldLimit(100));
			fields[i].setToolTipText("Max. 100 Zeichen");

			labels[i].setPreferredSize(new Dimension(100, 35));
			fields[i].setPreferredSize(new Dimension(300, 35));

			wrapperPanels[i] = new JPanel();

			wrapperPanels[i].setBackground(new Color(0, 0, 0, 0));
			wrapperPanels[i].setBorder(new LineBorder(Color.BLACK));

			wrapperPanels[i].add(labels[i]);
			wrapperPanels[i].add(fields[i]);

			container.add(wrapperPanels[i]);
		}

		container.setLayout(new GridLayout(0, 1));

		add(container, BorderLayout.CENTER);
	}

	private void setAllData(Contact data) {
		String[] adresse = data.getAddress().split(";");
		String street;
		String plzOrt;
		String country;

		if (adresse.length <= 0) {
			street = "";
			plzOrt = "";
			country = "";
		} else {
			
			if (adresse.length == 1 && adresse[0] != null && !adresse[0].isEmpty())
				street = adresse[0];
			else
				street = "";
			
			
			if (adresse.length == 2 && adresse[1] != null && !adresse[1].isEmpty()) {
				if( adresse[0] != null)
					street = adresse[0];
				else 
					street = "";
				plzOrt = adresse[1];
			}
				
			else {
				plzOrt = "";
			}
				
			if (adresse.length == 3 && adresse[2] != null && !adresse[2].isEmpty()) {
				if( adresse[0] != null  && !adresse[0].isEmpty())
					street = adresse[0];
				else 
					street = "";
				if( adresse[1] != null  && !adresse[1].isEmpty())
					plzOrt = adresse[1];
				else 
					plzOrt = "";
				
				country = adresse[2];
			}else {
				country = "";
			}		

		}
		fields[0].setText(data.getFirstName());
		fields[1].setText(data.getLastName());
		fields[2].setText(street);
		fields[3].setText(plzOrt);
		fields[4].setText(country);
		fields[5].setText(data.getPhone());
		fields[6].setText(data.getMail());

	}

	public void setSplitPane(JSplitPane splitPane) {
		this.splitPane = splitPane;
	}

	public void setScrollPane(JScrollPane scrollPaneDetails) {
		this.scrollPaneDetails = scrollPaneDetails;
	}

	public void setContactDetails(JPanelContactDetails paneDetails) {
		this.contactDetails = paneDetails;
	}
}
