package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import add.ESortOrder;
import add.Methods;
import classes.Contact;
import classes.ContactExt;
import res.Consts;

import static add.ESortOrder.ASC;
import static add.ESortOrder.DESC;
import static res.Consts.DIR;
import static res.Consts.FILE;

/**
 * Klasse für den Kontaktdetailscontainer
 *
 * Entwickler: Jan Schwenger
 */
public class JPanelContactDetails extends JPanel {
    private JLabel[] labels = new JLabel[EditNewContactFrame.ITEM_SIZE];
    private JLabel[] fields = new JLabel[EditNewContactFrame.ITEM_SIZE];
    private JPanel[] wrapperPanels = new JPanel[EditNewContactFrame.ITEM_SIZE], holderPanel;
    private JSplitPane splitPane;
    private JScrollPane scrollPaneDetails;
    private Contact contact;
    private final String[] LABELS;

    public JPanelContactDetails(JList contactList, MainFrame frame, ArrayList<Contact> allContacts, boolean searchState,
                                DefaultListModel model, ResourceBundle bundle) {

        LABELS = new String[]{
                bundle.getString(Consts.VORNAME),
                bundle.getString(Consts.NACHNAME),
                bundle.getString(Consts.STRASSENR),
                bundle.getString(Consts.PLZORT),
                bundle.getString(Consts.LAND),
                bundle.getString(Consts.TELEFON),
                bundle.getString(Consts.MAIL),
                bundle.getString(Consts.IMG)
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
            container.add(Box.createVerticalGlue());
        }
        container.setLayout(new FlowLayout(FlowLayout.TRAILING));
        container.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.setAlignmentY(Component.TOP_ALIGNMENT);
        BoxLayout layout2 = new BoxLayout(container, BoxLayout.PAGE_AXIS);

        container.setLayout(layout2);

        JScrollPane scrollPane = new JScrollPane(container);
        fixScrolling(scrollPane);

        ScrollPaneLayout layoutP = new ScrollPaneLayout();
        scrollPane.setLayout(layoutP);

        JButton btnLoeschen = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("resources/delete.png"))));
        editButton(btnLoeschen);
        btnLoeschen.setToolTipText(bundle.getString(Consts.LOESCHEN));
        btnLoeschen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Löschen");
                ArrayList<ContactExt> li = new ArrayList<>();
                System.out.println(((ContactExt) contactList.getSelectedValue()).getContact().getFirstName());
                allContacts.remove(((ContactExt) contactList.getSelectedValue()).getContact());
                li.add((ContactExt) contactList.getSelectedValue());

                Methods.deleteItemFile(Methods.userHomeDir + File.separator + DIR + File.separator +  FILE, li);
                DefaultListModel<Object> model = (DefaultListModel<Object>) contactList.getModel();

                model.removeElementAt(contactList.getSelectedIndex());

                contactList.clearSelection();
                setVisible(false);
                scrollPaneDetails.setVisible(false);
                splitPane.setDividerSize(0);
            }
        });

        JButton btnBearbeiten = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("resources/edit.png"))));
        editButton(btnBearbeiten);
        btnBearbeiten.setToolTipText(bundle.getString(Consts.BEARBEITEN));
        btnBearbeiten.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                frame.setEnabled(false);
                ESortOrder eSortOrder = null;

                if (frame.getCustomMenu().rbMenuItemSortASC.isSelected())
                    eSortOrder = ASC;
                else if(frame.getCustomMenu().rbMenuItemSortDESC.isSelected())
                    eSortOrder = DESC;
                EditNewContactFrame eFrame = new EditNewContactFrame(frame, contactList, allContacts, searchState, contact, model, bundle, eSortOrder, frame.getCustomMenu().getPath());

                //eFrame.setContact(contact);
                eFrame.setSplitPane(splitPane);
                eFrame.setScrollPane(scrollPaneDetails);
                eFrame.setContactDetails(JPanelContactDetails.this);
            }
        });
        JButton btnClose = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("resources/closewindow.png"))));

        editButton(btnClose);
        btnClose.setToolTipText(bundle.getString(Consts.SCHLIESSEN));
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contactList.clearSelection();
                splitPane.setDividerSize(0);
                scrollPaneDetails.setVisible(false);
                setVisible(false);
            }
        });

        JButton buttonMenu = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("resources/menu.png"))));
        editButton(buttonMenu);
        buttonMenu.setToolTipText(bundle.getString(Consts.MENU));
        JPanel panelWrapper = new JPanel();
        panelWrapper.setLayout(new GridLayout());

        JPanel panelHolderEdit = new JPanel();
        panelHolderEdit.setLayout(new FlowLayout(FlowLayout.LEFT));

        JPanel panelHolderDelete = new JPanel();
        panelHolderDelete.setLayout(new FlowLayout(Label.RIGHT));

        JPanel panelHolderMenu = new JPanel();
        panelHolderMenu.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JPanel panelHolderClose = new JPanel();
        panelHolderClose.setLayout(new FlowLayout(FlowLayout.LEFT));

        panelHolderDelete.setVisible(false);
        panelHolderEdit.setVisible(false);
        buttonMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!panelHolderDelete.isVisible() && !panelHolderEdit.isVisible()){
                    buttonMenu.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader()
                            .getResource("resources/openmenu.png"))));
                    panelHolderDelete.setVisible(true);
                    panelHolderEdit.setVisible(true);
                }else{
                    buttonMenu.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader()
                            .getResource("resources/menu.png"))));
                    panelHolderDelete.setVisible(false);
                    panelHolderEdit.setVisible(false);
                }
            }
        });

        panelHolderEdit.add(btnBearbeiten);
        panelHolderDelete.add(btnLoeschen);
        panelHolderMenu.add(buttonMenu);

        panelWrapper.add(panelHolderClose);
        panelWrapper.add(panelHolderEdit);
        panelWrapper.add(panelHolderDelete);
        panelWrapper.add(panelHolderMenu);

        panelHolderClose.add(btnClose);

        add(panelWrapper, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(false);
    }

    public Contact getContact() {
        return contact;
    }

    public void setSplitPane(JSplitPane splitPane) {
        this.splitPane = splitPane;
    }

    public void setScrollPane(JScrollPane scrollPaneDetails) {
        this.scrollPaneDetails = scrollPaneDetails;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
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
                if (adresse[0] != null)
                    street = adresse[0];
                else
                    street = "";
                plzOrt = adresse[1];
            } else {
                plzOrt = "";
            }
            if (adresse.length == 3 && adresse[2] != null && !adresse[2].isEmpty()) {
                if (adresse[0] != null && !adresse[0].isEmpty())
                    street = adresse[0];
                else
                    street = "";
                if (adresse[1] != null && !adresse[1].isEmpty())
                    plzOrt = adresse[1];
                else
                    plzOrt = "";
                country = adresse[2];
            } else {
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
        fields[7].setText(data.getImgPath());
    }

    public static void fixScrolling(JScrollPane scrollpane) {
        JLabel systemLabel = new JLabel();
        FontMetrics metrics = systemLabel.getFontMetrics(systemLabel.getFont());
        int lineHeight = metrics.getHeight();
        int charWidth = metrics.getMaxAdvance();

        JScrollBar systemVBar = new JScrollBar(JScrollBar.VERTICAL);
        JScrollBar systemHBar = new JScrollBar(JScrollBar.HORIZONTAL);
        int verticalIncrement = systemVBar.getUnitIncrement();
        int horizontalIncrement = systemHBar.getUnitIncrement();

        scrollpane.getVerticalScrollBar().setUnitIncrement(lineHeight * verticalIncrement);
        scrollpane.getHorizontalScrollBar().setUnitIncrement(charWidth * horizontalIncrement);
    }

    private void editButton(JButton button) {
        button.setBorderPainted(false);
        button.setBorder(null);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
