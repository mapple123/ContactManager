package gui;

import java.awt.*;
import java.awt.event.*;
import java.time.Clock;
import java.util.*;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import add.ESortOrder;
import add.Methods;
import add.MyIcon;
import classes.Contact;
import classes.nObj;
import res.Consts;

import static add.ESortOrder.ASC;
import static add.ESortOrder.DESC;

public class CustomMenu extends JMenuBar implements ActionListener , ItemListener {

    private static final long serialVersionUID = 1L;
    private JMenuBar menuBar;
    private JMenu menu, menu2, menu3, submenu;
    private JMenuItem menuItem, menuItem2, menuItem3;

    private MainFrame frame;

    private String path;

    private JList<?> list;
    private ArrayList<Contact> allContacts;
    private JPanelContactDetails panelDetails;
    private JSplitPane splitPane;
    private JScrollPane scrollPaneDetails;
    private ResourceBundle bundle;

    private boolean search;

    protected JRadioButtonMenuItem rbMenuItem, rbMenuItem2, rbMenuItem3, rbMenuItem4;

    protected boolean basc = false, bdesc = false;

    public CustomMenu(MainFrame frame, JList<?> list, ArrayList<Contact> allContacts, String path,
                      JPanelContactDetails panelDetails, JSplitPane splitPane, JScrollPane scrollPaneDetails, ResourceBundle bundle) {
        this.path = path;
        this.frame = frame;
        this.list = list;
        this.allContacts = allContacts;
        this.panelDetails = panelDetails;
        this.splitPane = splitPane;
        this.scrollPaneDetails = scrollPaneDetails;
        this.bundle = bundle;
        menuBar = new JMenuBar();

        menu = new JMenu(bundle.getString(Consts.NEU));
        menu.setFont(new Font("Arial", Font.PLAIN, 30));
        menu.setMnemonic(KeyEvent.VK_N);

        menuBar.add(menu);

        menu3 = new JMenu(bundle.getString(Consts.LOESCHEN));
        menu3.setFont(new Font("Arial", Font.PLAIN, 30));
        menu3.setMnemonic(KeyEvent.VK_L);

        menuBar.add(menu3);

        menuItem = new JMenuItem(bundle.getString(Consts.SUBLOESCHEN), KeyEvent.VK_J);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, ActionEvent.ALT_MASK));

        menuItem.addActionListener(this);
        menu3.add(menuItem);

        menuItem2 = new JMenuItem(bundle.getString(Consts.NEUTITEL), KeyEvent.VK_P);
        menuItem2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));

        menuItem2.addActionListener(this);
        menu.add(menuItem2);

        menu2 = new JMenu(bundle.getString(Consts.FILERSORTIEREN));
        menu2.setFont(new Font("Arial", Font.PLAIN, 30));
        menu2.setMnemonic(KeyEvent.VK_R);
        menu2.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBar.add(Box.createHorizontalGlue());

       /* menuItem3 = new JMenuItem(bundle.getString(Consts.SORTIEREN), KeyEvent.VK_P);
        menuItem3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));

        menuItem3.addActionListener(this);*/

        //a group of radio button menu items

        ButtonGroup group = new ButtonGroup();
        rbMenuItem = new JRadioButtonMenuItem(bundle.getString(Consts.SUBSORTIERENASC));
        rbMenuItem.setSelected(true);
        rbMenuItem.setMnemonic(KeyEvent.VK_R);
        group.add(rbMenuItem);

        rbMenuItem2 = new JRadioButtonMenuItem(bundle.getString(Consts.SUBSORTIERENDESC));

        rbMenuItem2.setMnemonic(KeyEvent.VK_R);
        group.add(rbMenuItem2);

        rbMenuItem3 = new JRadioButtonMenuItem(bundle.getString(Consts.SUBSORTIERENLASTASC));

        rbMenuItem3.setMnemonic(KeyEvent.VK_R);
        group.add(rbMenuItem3);

        rbMenuItem4 = new JRadioButtonMenuItem(bundle.getString(Consts.SUBSORTIERENLASTDESC));

        rbMenuItem4.setMnemonic(KeyEvent.VK_R);
        group.add(rbMenuItem4);

        rbMenuItem.addItemListener(this);
        rbMenuItem2.addItemListener(this);
        rbMenuItem3.addItemListener(this);
        rbMenuItem4.addItemListener(this);
        //menu2.add(menuItem3);
        menu2.add(rbMenuItem);
        menu2.add(rbMenuItem2);
        menu2.add(rbMenuItem3);
        menu2.add(rbMenuItem4);

        submenu = new JMenu(bundle.getString(Consts.SORTIEREN));
        submenu.setMnemonic(KeyEvent.VK_S);


        submenu.add(rbMenuItem);
        submenu.add(rbMenuItem2);
        submenu.add(rbMenuItem3);
        submenu.add(rbMenuItem4);
        menu2.add(submenu);

        menuBar.add(menu2);

        frame.setJMenuBar(menuBar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ESortOrder eSortOrder = null;
        if (rbMenuItem.isSelected())
            eSortOrder = ASC;
        else if(rbMenuItem2.isSelected())
            eSortOrder = DESC;
        if (e.getSource() == menuItem2) {
            frame.setEnabled(false);
            EditNewContactFrame eFrame = new EditNewContactFrame(frame, list, allContacts, search, null, null, bundle, eSortOrder);
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

            }  else {
                //TODO: Strings erstellen in Consts und in Textbundle
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



    @Override
    public void itemStateChanged(ItemEvent e) {

        if(e.getSource() == rbMenuItem && e.getStateChange() == ItemEvent.SELECTED){

            sortAsc(allContacts);



        }else if(e.getSource() == rbMenuItem2 && e.getStateChange() == ItemEvent.SELECTED){
            //Collections.reverseOrder((Comparator<Contact>) (o1, o2) -> o1.getFirstName().compareTo(o2.getFirstName()));
            sortDesc(allContacts);

        } else if(e.getSource() == rbMenuItem3 && e.getStateChange() == ItemEvent.SELECTED){

        }else if(e.getSource() == rbMenuItem4 && e.getStateChange() == ItemEvent.SELECTED){

        }
         DefaultListModel sortedItems = (DefaultListModel) list.getModel();
        sortedItems.clear();
        sortedItems = frame.filterItems(frame.jtfSearch.getText());
        list.setModel(sortedItems);

    }

    protected static void sortAsc(ArrayList<Contact> allContacts){
        Collections.sort(allContacts, new Comparator<Contact>(){

            public int compare(Contact o1, Contact o2)
            {
                return o1.getFirstName().compareToIgnoreCase(o2.getFirstName());
            }
        });
    }
    protected static void sortDesc(ArrayList<Contact> allContacts){
        Collections.sort(allContacts, new Comparator<Contact>(){

            public int compare(Contact o1, Contact o2)
            {
                return o2.getFirstName().compareToIgnoreCase(o1.getFirstName());
            }
        });
    }
    public void setAllContacts(ArrayList<Contact> allContacts){
        this.allContacts = allContacts;
    }
}
