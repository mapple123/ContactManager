package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

import add.DeselectOnClickListener;
import add.Methods;
import add.MyIcon;
import classes.Contact;
import classes.ContactExt;
import res.Consts;

/**
 * Das ist das Hauptfenster des Programmes
 *
 * Entwickler: Jan Schwenger
 */
public class MainFrame extends JFrame implements ComponentListener{
    private static final long serialVersionUID = 1L;
    private Container mainPane;
    private JList<Object> listFiles;
    private JScrollPane scrollPane, scrollPaneDetails;
    private CustomMenu menuBar;
    private JPanelContactDetails jPanelContactDetails;
    private Dimension screenSize;
    private int width;
    private int height;
    private File file;
    protected JTextField jtfSearch;
    private ArrayList<Contact> allContacts;
    private DefaultListModel model;
    private JSplitPane splitPane;
    private ResourceBundle bundle;
    private String userHome;

    public MainFrame(File file, ResourceBundle bundle, String userHome) {
        super(bundle.getString(Consts.TITEL));
        setExtendedState(MAXIMIZED_BOTH);
        this.file = file;
        this.bundle = bundle;
        this.userHome = userHome;
        initComponents();
        setComponents();

        setSize(new Dimension(width, height));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addComponentListener(this);
        setMinimumSize(new Dimension(435, 300));
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

        mainPane = getContentPane();
    }

    private void setComponents() {
        mainPane.setLayout(new BorderLayout());
        mainPane.setSize(width, height);

        MyCustomListCellRenderer renderer = new MyCustomListCellRenderer();
        listFiles.setCellRenderer(renderer);

        scrollPane = new JScrollPane(listFiles);
        scrollPaneDetails = new JScrollPane(jPanelContactDetails);

        jPanelContactDetails.setPreferredSize(new Dimension(660, height));

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, jPanelContactDetails);
        jPanelContactDetails.setScrollPane(scrollPaneDetails);
        jPanelContactDetails.setSplitPane(splitPane);
        listFiles.addMouseListener(new DeselectOnClickListener(jPanelContactDetails, splitPane, scrollPaneDetails));

        splitPane.setContinuousLayout(true);

        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(0);
        splitPane.setDividerLocation(scrollPane.getLocation().x + (int) (getContentPane().getSize().getWidth() / 2));// jPanelContactDetails.getPreferredSize().width
        listFiles.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                System.out.println("First index: " + listSelectionEvent.getFirstIndex());
                System.out.println(", Last index: " + listSelectionEvent.getLastIndex());
                boolean adjust = listSelectionEvent.getValueIsAdjusting();
                System.out.println(", Adjusting? " + adjust);
                if (!adjust) {
                    jPanelContactDetails.setVisible(true);
                    JList list = (JList) listSelectionEvent.getSource();
                    list.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    int selections[] = list.getSelectedIndices();
                    Object selectionValues[] = list.getSelectedValues();
                    for (int i = 0, n = selections.length; i < n; i++) {
                        if (i == 0) {
                            System.out.println(" Selections: ");
                        }
                       /* System.out
                                .println(selections[i] + "/" + ((Contact) selectionValues[i]).getContact().getId() + " ");
                        System.out.println(((Contact) selectionValues[i]).getContact().toString());
*/
                        splitPane.setDividerSize(10);
                        splitPane.setDividerLocation(
                                scrollPane.getLocation().x + (int) (getContentPane().getSize().getWidth() / 2));// jPanelContactDetails.getPreferredSize().width
                        System.out.println("WIDTH:" + mainPane.getWidth());

                        if (i < n) {
                            jPanelContactDetails.setContact(((ContactExt) selectionValues[i]).getContact());
                            jPanelContactDetails.setAllData(((ContactExt) selectionValues[i]).getContact());
                        }
                    }
                }
            }
        });
        menuBar = new CustomMenu(this, listFiles, allContacts, file.getAbsolutePath(), jPanelContactDetails,
                splitPane, scrollPaneDetails, bundle, userHome);
        jtfSearch = new JTextField();
        jtfSearch.setPreferredSize(new Dimension(300, 35));
        jtfSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
               /* searchFilter(jtfSearch.getText());
                hideContactDetails();

                */
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                /*searchFilter(jtfSearch.getText());
                hideContactDetails();

                 */
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
               /* searchFilter(jtfSearch.getText());
                hideContactDetails();
                
                */
            }
        });
        JButton btnSearch = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("resources/search.png"))));
        btnSearch.setBorderPainted(false);
        btnSearch.setBorder(null);

        btnSearch.setMargin(new Insets(0, 0, 0, 0));
        btnSearch.setContentAreaFilled(false);
        btnSearch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSearch.setToolTipText(bundle.getString(Consts.SUCHEN));
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchFilter(jtfSearch.getText());
                hideContactDetails();
            }
        });

        JPanel panelNorthContainer = new JPanel();
        panelNorthContainer.setLayout(new FlowLayout(Label.LEFT));
        panelNorthContainer.add(jtfSearch);
        panelNorthContainer.add(btnSearch);

        mainPane.add(panelNorthContainer, BorderLayout.NORTH);
        mainPane.add(splitPane, BorderLayout.CENTER);
    }

    private Object[] getAllContacts() {
        this.allContacts = Methods.readAllContacts(file);
        Object elements[] = new Object[allContacts.size()];
        for (int i = 0; i < elements.length; i++) {
            Contact contact = allContacts.get(i);
            System.out.println(contact.getId());
            String firstName = allContacts.get(i).getFirstName();
            String lastName = allContacts.get(i).getLastName();
            String imgPath = allContacts.get(i).getImgPath();
            MyIcon icon;

            if(imgPath != null)
                icon = new MyIcon(imgPath);
            else
                icon = new MyIcon();



            elements[i] = new ContactExt(new Font("Arial", Font.PLAIN, 20), Color.BLACK, icon,
                    firstName + " " + lastName, contact);
        }
        return elements;
    }
    private void searchFilter(String searchItem) {
        if (jtfSearch.getText().length() == 0) {
            menuBar.setSearch(false);
        } else {
            menuBar.setSearch(true);
        }
        model = filterItems(searchItem);
        listFiles.setModel(model);
    }

    protected DefaultListModel filterItems(String searchItem){
        DefaultListModel filteredItems = new DefaultListModel<>();
       // DefaultListModel dlm = new DefaultListModel();
       // for( int i=0; i<10000; ++i ) {
            //dlm.addElement( "aaaa");
       // }

        for (int i = 0; i < allContacts.size(); i++) {
            Contact contact = allContacts.get(i);
            String firstName = allContacts.get(i).getFirstName();
            String lastName = allContacts.get(i).getLastName();
           /* ContactExt obj = new ContactExt(new Font("Arial", Font.PLAIN, 20), Color.BLACK, new MyIcon(),
                    firstName + " " + lastName, contact);
                    */
            String name = contact.getFirstName().toLowerCase() + " " + contact.getLastName().toLowerCase();

            String imgPath = allContacts.get(i).getImgPath();
            MyIcon icon;

            if(imgPath != null){
                icon = new MyIcon(imgPath);
                System.out.println("IMG: " + imgPath);}
            else
                icon = new MyIcon();


            ContactExt obj = new ContactExt(new Font("Arial", Font.PLAIN, 20), Color.BLACK, icon,
                    firstName + " " + lastName, contact);
            if (name.contains(searchItem.toLowerCase())) {
                filteredItems.addElement(obj);
            }
        }
        return filteredItems;
    }
    @Override
    public void componentResized(ComponentEvent e) {
        //System.out.println(this.getSize().height);
    }
    @Override
    public void componentMoved(ComponentEvent e) {

    }
    @Override
    public void componentShown(ComponentEvent e) {

    }
    @Override
    public void componentHidden(ComponentEvent e) {

    }
    protected CustomMenu getCustomMenu(){
        return menuBar;
    }
    private void hideContactDetails(){
        jPanelContactDetails.setVisible(false);
        scrollPaneDetails.setVisible(false);
        splitPane.setDividerSize(0);
    }
}
