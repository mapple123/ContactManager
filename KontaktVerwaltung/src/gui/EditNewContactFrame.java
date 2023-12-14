package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

import add.ESortOrder;
import add.JTextFieldLimit;
import add.Methods;
import add.MyIcon;
import classes.Contact;
import classes.ContactExt;
import res.Consts;

import static res.Consts.*;

/**
 * Diese Klasse repräsentiert das Fenster, um neue Kontakte anzulegen und zusätzlich um vorhandene Kontakte zu
 * bearbeiten
 *
 * Entwickler: Jan Schwenger
 */
public class EditNewContactFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    protected static final byte ITEM_SIZE = 8;
    private MainFrame mainFrame;
    private JList contactList;
    private ArrayList<Contact> allContacts;
    private boolean searchState;
    private ResourceBundle bundle;
    private final String[] LABELS;
    private JLabel[] labels = new JLabel[ITEM_SIZE];
    private JTextField[] fields = new JTextField[ITEM_SIZE];
    private JPanel[] wrapperPanels = new JPanel[ITEM_SIZE];
    private Contact contact;
    private JSplitPane splitPane;
    private JScrollPane scrollPaneDetails;
    private JPanelContactDetails contactDetails;
    private DefaultListModel model;
    private ESortOrder eSortOrder;
    private String userPath;


    public EditNewContactFrame(MainFrame mainFrame, JList contactList, final ArrayList<Contact> allContacts, boolean searchState,
                               Contact contact, final DefaultListModel model, ResourceBundle bundle, ESortOrder eSortOrder, String userPath) {
        super();
        this.userPath = userPath;
        this.searchState = searchState;
        this.contactList = contactList;
        this.mainFrame = mainFrame;
        this.allContacts = allContacts;
        this.model = model;
        this.bundle = bundle;
        this.eSortOrder = eSortOrder;
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
        buildPanel(contact);

        JButton btnOk = new JButton(bundle.getString(Consts.OK));
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
                String imgPath = fields[7].getText().replace(",", "|").trim();

                // Zuständig für das Abspeichern eines neuen Kontaktes
                if (contact == null) {
                    if ((firstName.length() == 0 && lastName.length() == 0))
                        //TODO: Strings erstellen
                        JOptionPane.showMessageDialog(EditNewContactFrame.this,
                                "Die mit * gekenntzeichneten Felder müssen ausgefüllt werden!", "Achtung",
                                JOptionPane.WARNING_MESSAGE);
                    else {

                        //TODO: change icon implementation
                        MyIcon icon;
                        String outputImagePath = "";
                        if (imgPath.equals(""))
                            icon = new MyIcon();
                        else{
                            String inputImagePath = imgPath;
                            outputImagePath = userPath + File.separator + DIR + "\\img\\image_"+ lastName+ "_"+firstName+ ".png";
                            int targetWidth = 44; // New width of the image
                            int targetHeight = 44; // New height of the image

                            try {
                                // Read the input image
                                BufferedImage originalImage = ImageIO.read(new File(inputImagePath));

                                // Create a new BufferedImage with the desired resolution
                                BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);

                                // Create a Graphics2D object from the resized image
                                Graphics2D g2 = resizedImage.createGraphics();

                                // Set rendering hints to improve the quality of the resized image
                                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                                // Draw the original image onto the resized image
                                g2.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);

                                // Dispose the Graphics2D object
                                g2.dispose();

                                // Write the resized image to the output file
                                ImageIO.write(resizedImage, "png", new File(outputImagePath));

                                System.out.println("Image resolution reduced successfully.");

                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }



                            icon = new MyIcon(outputImagePath);
                        }

                        Contact contact = Methods.saveContact(
                                new Contact(firstName, lastName, street + ";" + ort + ";" + country, phone, mail, outputImagePath));

                        if (!searchState) {
                            DefaultListModel<Object> model = (DefaultListModel<Object>) contactList.getModel();
                            model.addElement(new ContactExt(new Font("Arial", Font.PLAIN, 20), Color.BLACK, icon,
                                    contact.getFirstName() + " " + contact.getLastName(), contact));
                        }
                        System.out.println("Test");
                        mainFrame.setEnabled(true);

                        File f = new File(Methods.userHomeDir + File.separator + DIR + File.separator +  FILE);
                        Methods.editItem(f.getAbsolutePath(), new Contact(firstName, lastName,
                                street + ";" + ort + ";" + country, phone, mail, outputImagePath, contact.getId()));
                        mainFrame.setEnabled(true);
                        allContacts.add(contact);
                        switch(eSortOrder){
                            case ASC: CustomMenu.sortAsc(allContacts);
                                break;
                            case DESC: CustomMenu.sortDesc(allContacts);
                                break;
                        }
                        DefaultListModel sortedItems = (DefaultListModel) contactList.getModel();
                        sortedItems.clear();
                        sortedItems = mainFrame.filterItems(mainFrame.jtfSearch.getText());
                        contactList.setModel(sortedItems);

                        EditNewContactFrame.this.dispose();
                    }

                } else if (contact != null) { // Zuständig für das Abspeichern eines Kontaktes, der bearbeitet wurde
                    if ((firstName.length() == 0 && lastName.length() == 0))
                        //TODO: Strings erstellen
                        JOptionPane.showMessageDialog(EditNewContactFrame.this,
                                "Die mit * gekenntzeichneten Felder müssen ausgefüllt werden!", "Achtung",
                                JOptionPane.WARNING_MESSAGE);
                    else {
                        System.out.println("Gespeichert:" + street);

                        File f = new File(Methods.userHomeDir + File.separator + DIR + File.separator + FILE);
                        allContacts.remove(contact);
                        Contact newContact = new Contact(firstName, lastName,
                                street + ";" + ort + ";" + country, phone, mail,imgPath, contact.getId());
                        Methods.editItem(f.getAbsolutePath(), newContact);
                        System.err.println(newContact.getId());

                        allContacts.add(newContact);

                        switch(eSortOrder){
                            case ASC: CustomMenu.sortAsc(allContacts);
                            break;
                            case DESC: CustomMenu.sortDesc(allContacts);
                            break;
                        }

                        mainFrame.setEnabled(true);

                        DefaultListModel sortedItems = (DefaultListModel) contactList.getModel();
                        sortedItems.clear();
                        sortedItems = mainFrame.filterItems(mainFrame.jtfSearch.getText());
                        contactList.setModel(sortedItems);

                        contactDetails.setVisible(false);
                        scrollPaneDetails.setVisible(false);
                        splitPane.setDividerSize(0);

                        EditNewContactFrame.this.dispose();
                    }
                }

            }
        });

        JButton btnCancel = new JButton(bundle.getString(Consts.ABBRECHEN));
        btnCancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setEnabled(true);
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
                mainFrame.setEnabled(true);
            }
        };
        addWindowListener(listener);
        setSize(new Dimension(Consts.WIDTH, Consts.HEIGHT));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    private void buildPanel(Contact contact) {
        try {
            setComponents();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (contact == null) {
            setTitle(bundle.getString(Consts.NEUTITEL));
        } else {
            setTitle(bundle.getString(Consts.BEARBEITENTITEL));
            setAllData(contact);
        }
    }

    private void setComponents() throws IOException {
        JPanel container = new JPanel();
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel(LABELS[i]);
            fields[i] = new JTextField();

            fields[i].setDocument(new JTextFieldLimit(LIMIT));
            fields[i].setToolTipText(bundle.getString(Consts.LIMITTEXT));

            labels[i].setPreferredSize(new Dimension(100, 35));
            fields[i].setPreferredSize(new Dimension(300, 35));

            wrapperPanels[i] = new JPanel();

            wrapperPanels[i].setLayout(new FlowLayout(FlowLayout.LEFT));

            wrapperPanels[i].setBackground(new Color(0, 0, 0, 0));
            wrapperPanels[i].setBorder(new LineBorder(Color.BLACK));

            wrapperPanels[i].add(labels[i]);
            wrapperPanels[i].add(fields[i]);

            if(i == 7){
                fields[i].setVisible(true);
                JButton btnSelectImg = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("resources/img.png"))));
                btnSelectImg.setPreferredSize(new Dimension(25,25));

                JLabel imageLabel = new JLabel();
                imageLabel.setPreferredSize(new Dimension(44,44));
                btnSelectImg.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();
                        int result = fileChooser.showOpenDialog(EditNewContactFrame.this);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            File selectedFile = fileChooser.getSelectedFile();
                            fields[7].setText(selectedFile.getAbsolutePath());
                            // Update the image label with the selected image
                           /* ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
                            imageLabel.setIcon(imageIcon);*/

                            // Read the selected image file
                            //BufferedImage image = ImageIO.read(selectedFile);
                            ImageIcon image = new MyIcon(selectedFile.getAbsolutePath()).getImg();
                            // Resize the image to fit the label
                            Image scaledImage = image.getImage().getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
                            // Update the image label with the scaled image
                            ImageIcon imageIcon = new ImageIcon(scaledImage);
                            imageLabel.setIcon(imageIcon);
                        }
                    }
                });
                wrapperPanels[i].add(imageLabel);
                wrapperPanels[i].add(btnSelectImg);
            }

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
        fields[0].setText(data.getFirstName());
        fields[1].setText(data.getLastName());
        fields[2].setText(street);
        fields[3].setText(plzOrt);
        fields[4].setText(country);
        fields[5].setText(data.getPhone());
        fields[6].setText(data.getMail());
        fields[7].setText(data.getImgPath());
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
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

}
