package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import add.ESortOrder;
import add.Methods;
import classes.Contact;
import classes.ContactExt;
import res.Consts;

import static add.ESortOrder.ASC;
import static add.ESortOrder.DESC;

/**
 * Custom Menü für das Hauptprogramm
 *
 * Entwickler: Jan Schwenger
 */
public class CustomMenu extends JMenuBar implements ActionListener , ItemListener {
    private static final long serialVersionUID = 1L;
    private JMenuBar menuBar;
    private JMenu menuNew, menuFilterSort, menuDelete, submenuSorting;
    private JMenuItem menuItemDelete, menuItemNew, menuItem3;
    private MainFrame frame;
    private String path, userHome;
    private JList<?> list;
    private ArrayList<Contact> allContacts;
    private JPanelContactDetails panelDetails;
    private JSplitPane splitPane;
    private JScrollPane scrollPaneDetails;
    private ResourceBundle bundle;
    private boolean search;
    protected JRadioButtonMenuItem rbMenuItemSortASC, rbMenuItemSortDESC, rbMenuItemSortLastNameASC, rbMenuItemSortLastNameDESC;
    public CustomMenu(MainFrame frame, JList<?> list, ArrayList<Contact> allContacts, String path,
                      JPanelContactDetails panelDetails, JSplitPane splitPane, JScrollPane scrollPaneDetails, ResourceBundle bundle, String userHome) {
        this.path = path;
        this.frame = frame;
        this.list = list;
        this.allContacts = allContacts;
        this.panelDetails = panelDetails;
        this.splitPane = splitPane;
        this.scrollPaneDetails = scrollPaneDetails;
        this.bundle = bundle;
        this.userHome = userHome;

        menuBar = new JMenuBar();

        // Menü "Neu"
        menuNew = new JMenu(bundle.getString(Consts.NEU));
        menuNew.setFont(new Font("Arial", Font.PLAIN, 30));
        menuNew.setMnemonic(KeyEvent.VK_N);

        menuBar.add(menuNew);

        // Menü "Löschen"
        menuDelete = new JMenu(bundle.getString(Consts.LOESCHEN));
        menuDelete.setFont(new Font("Arial", Font.PLAIN, 30));
        menuDelete.setMnemonic(KeyEvent.VK_L);

        menuBar.add(menuDelete);

        // Submenü "Löschen"
        menuItemDelete = new JMenuItem(bundle.getString(Consts.SUBLOESCHEN), KeyEvent.VK_J);
        menuItemDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, ActionEvent.ALT_MASK));
        menuItemDelete.addActionListener(this);
        menuDelete.add(menuItemDelete);

        // Submenü neuen Kontakt erstellen
        menuItemNew = new JMenuItem(bundle.getString(Consts.NEUTITEL), KeyEvent.VK_P);
        menuItemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
        menuItemNew.addActionListener(this);
        menuNew.add(menuItemNew);

        // Menü Filter/Sortieren
        menuFilterSort = new JMenu(bundle.getString(Consts.FILERSORTIEREN));
        menuFilterSort.setFont(new Font("Arial", Font.PLAIN, 30));
        menuFilterSort.setMnemonic(KeyEvent.VK_R);
        menuFilterSort.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBar.add(Box.createHorizontalGlue());

        ButtonGroup groupSorting = new ButtonGroup();

        // Radiobutton nach Vorname Asc sortieren
        rbMenuItemSortASC = new JRadioButtonMenuItem(bundle.getString(Consts.SUBSORTIERENASC));
        rbMenuItemSortASC.setSelected(true);
        rbMenuItemSortASC.setMnemonic(KeyEvent.VK_R);
        groupSorting.add(rbMenuItemSortASC);

        // Radiobutton nach Vorname Desc sortieren
        rbMenuItemSortDESC = new JRadioButtonMenuItem(bundle.getString(Consts.SUBSORTIERENDESC));
        rbMenuItemSortDESC.setMnemonic(KeyEvent.VK_R);
        groupSorting.add(rbMenuItemSortDESC);

        // Radiobutton nach Nachname Asc sortieren
        rbMenuItemSortLastNameASC = new JRadioButtonMenuItem(bundle.getString(Consts.SUBSORTIERENLASTASC));
        rbMenuItemSortLastNameASC.setMnemonic(KeyEvent.VK_R);
        groupSorting.add(rbMenuItemSortLastNameASC);

        // Radiobutton nach Nachname Desc sortieren
        rbMenuItemSortLastNameDESC = new JRadioButtonMenuItem(bundle.getString(Consts.SUBSORTIERENLASTDESC));
        rbMenuItemSortLastNameDESC.setMnemonic(KeyEvent.VK_R);
        groupSorting.add(rbMenuItemSortLastNameDESC);

        // Hinzufügen des Itemlisteners
        rbMenuItemSortASC.addItemListener(this);
        rbMenuItemSortDESC.addItemListener(this);
        rbMenuItemSortLastNameASC.addItemListener(this);
        rbMenuItemSortLastNameDESC.addItemListener(this);

        // Zum Menü Filtern/Sortieren hinzufügen
        submenuSorting = new JMenu(bundle.getString(Consts.SORTIEREN));
        submenuSorting.setMnemonic(KeyEvent.VK_S);
        submenuSorting.add(rbMenuItemSortASC);
        submenuSorting.add(rbMenuItemSortDESC);
        submenuSorting.add(rbMenuItemSortLastNameASC);
        submenuSorting.add(rbMenuItemSortLastNameDESC);
        menuFilterSort.add(submenuSorting);

        menuBar.add(menuFilterSort);
        frame.setJMenuBar(menuBar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ESortOrder eSortOrder = null;
        if (rbMenuItemSortASC.isSelected())
            eSortOrder = ASC;
        else if(rbMenuItemSortDESC.isSelected())
            eSortOrder = DESC;
        if (e.getSource() == menuItemNew) {
            frame.setEnabled(false);
            EditNewContactFrame eFrame = new EditNewContactFrame(frame, list, allContacts, search, null, null, bundle, eSortOrder, userHome);
            eFrame.setContact(null);
        } else if (e.getSource() == menuItemDelete) {
            if (list.getSelectedIndices().length > 0) {
                System.out.println("Löschen");
                ArrayList<ContactExt> li = new ArrayList<>();
                for (Object item : list.getSelectedValuesList()) {
                    li.add((ContactExt) item);
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
        if(e.getSource() == rbMenuItemSortASC && e.getStateChange() == ItemEvent.SELECTED){
            sortAsc(allContacts);
        }else if(e.getSource() == rbMenuItemSortDESC && e.getStateChange() == ItemEvent.SELECTED){
            sortDesc(allContacts);
        } else if(e.getSource() == rbMenuItemSortLastNameASC && e.getStateChange() == ItemEvent.SELECTED){

        }else if(e.getSource() == rbMenuItemSortLastNameDESC && e.getStateChange() == ItemEvent.SELECTED){

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

    public String getPath(){
        return this.userHome;
    }
}
