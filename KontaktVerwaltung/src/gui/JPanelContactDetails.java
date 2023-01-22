package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import add.Methods;
import classes.Contact;
import classes.nObj;
import main.MainMethod;
import res.Consts;

public class JPanelContactDetails extends JPanel{

	
	private JLabel[] labels = new JLabel[7];
	private JLabel[] fields = new JLabel[7];
	private JPanel[] wrapperPanels = new JPanel[7], holderPanel;
	private JSplitPane splitPane;
	private JScrollPane scrollPaneDetails;
	private JList list;
	private Contact contact;
	private ResourceBundle bundle;
	
	private final String[] LABELS; 

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public JPanelContactDetails(JList list, MainFrame frame, ArrayList<Contact> alContacts, boolean search,
			DefaultListModel model, ResourceBundle bundle) {

		this.list = list;
		this.bundle = bundle;
		
		LABELS = new String[]{
				bundle.getString(Consts.VORNAME),  
				bundle.getString(Consts.NACHNAME),  
				bundle.getString(Consts.STRASSENR),  
				bundle.getString(Consts.PLZORT),  
				bundle.getString(Consts.LAND), 
				bundle.getString(Consts.TELEFON),
				bundle.getString(Consts.MAIL) 
				};
		BorderLayout layout = new BorderLayout();
		setLayout(layout);

		JPanel container = new JPanel();

		for (int i = 0; i < labels.length; i++) {
			labels[i] = new JLabel(LABELS[i]);
			fields[i] = new JLabel();
			fields[i].setBackground(Color.WHITE);
			fields[i].setOpaque(true);
			labels[i].setPreferredSize(new Dimension(100, 35));
			fields[i].setPreferredSize(new Dimension(300, 35));

			wrapperPanels[i] = new JPanel();
			wrapperPanels[i].setBorder(
					BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.BLACK)));
			wrapperPanels[i].setBackground(Color.LIGHT_GRAY);

			Border border = fields[i].getBorder();
			Border margin = new EmptyBorder(10, 10, 10, 10);
			fields[i].setBorder(new CompoundBorder(border, margin));

			wrapperPanels[i].add(labels[i]);
			wrapperPanels[i].add(fields[i]);
			container.add(wrapperPanels[i]);
		}

		container.setAlignmentX(Component.LEFT_ALIGNMENT);
		container.setAlignmentY(Component.TOP_ALIGNMENT);
		BoxLayout layout2 = new BoxLayout(container, BoxLayout.PAGE_AXIS);
		container.setLayout(layout2);

		JButton btnLoeschen = new JButton(bundle.getString(Consts.LOESCHEN));
		btnLoeschen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				System.out.println("Löschen");
				ArrayList<nObj> li = new ArrayList<>();

				li.add((nObj) list.getSelectedValue());

				Methods.deleteItemFile(Methods.userHomeDir + Methods.DIR + Methods.FILE, li);
				DefaultListModel<Object> model = (DefaultListModel<Object>) list.getModel();

				model.removeElementAt(list.getSelectedIndex());

				list.clearSelection();
				setVisible(false);
				scrollPaneDetails.setVisible(false);
				splitPane.setDividerSize(0);

			}
		});

		JButton btnBearbeiten = new JButton(bundle.getString(Consts.BEARBEITEN));
		btnBearbeiten.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.setEnabled(false);

				EditNewContactFrame eFrame = new EditNewContactFrame(frame, list, alContacts, search, contact, model, bundle);

				eFrame.setContact(contact);
				eFrame.setSplitPane(splitPane);
				eFrame.setScrollPane(scrollPaneDetails);
				eFrame.setContactDetails(JPanelContactDetails.this);

			}
		});
		JButton btnClose = new JButton(bundle.getString(Consts.SCHLIESSEN));
		btnClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				list.clearSelection();

				splitPane.setDividerSize(0);
				scrollPaneDetails.setVisible(false);
				setVisible(false);
				
			}
		} );

		JPanel panelWrapper = new JPanel();
		panelWrapper.setLayout(new GridLayout());

		JPanel panelHolderEdit = new JPanel();
		panelHolderEdit.setLayout(new FlowLayout(Label.LEFT));

		JPanel panelHolderDelete = new JPanel();
		panelHolderDelete.setLayout(new FlowLayout(Label.RIGHT));

		JPanel panelHolderClose = new JPanel();
		panelHolderClose.setLayout(new FlowLayout(Label.RIGHT));

		panelHolderEdit.add(btnBearbeiten);
		panelHolderDelete.add(btnLoeschen);

		panelWrapper.add(panelHolderEdit);
		panelWrapper.add(panelHolderDelete);

		panelHolderClose.add(btnClose);

		add(panelHolderClose, BorderLayout.NORTH);
		add(panelWrapper, BorderLayout.SOUTH);
		add(container, BorderLayout.CENTER);

	
		setVisible(false);
		
	}

	public void setSplitPane(JSplitPane splitPane) {
		this.splitPane = splitPane;
	}

	public void setScrollPane(JScrollPane scrollPaneDetails) {
		this.scrollPaneDetails = scrollPaneDetails;
	}

	public void setAllData(Contact data) {
		String[] adresse = data.getAddress().split(";");
		String street;
		String plzOrt;
		String country;
		System.out.println(adresse.length);
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
		System.out.println("S:" + street);
		System.out.println("P:" + plzOrt);
		System.out.println("C:" + country);
		fields[0].setText(data.getFirstName());
		fields[1].setText(data.getLastName());
		fields[2].setText(street);
		fields[3].setText(plzOrt);
		fields[4].setText(country);
		fields[5].setText(data.getPhone());
		fields[6].setText(data.getMail());

	}

	

}
