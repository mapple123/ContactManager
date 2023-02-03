package add;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import gui.JPanelContactDetails;

public class DeselectOnClickListener extends MouseAdapter {

    private JPanelContactDetails jPanelContactDetails;
    private JSplitPane splitPane;
    private JScrollPane scrollPaneDetails;

    public DeselectOnClickListener(JPanelContactDetails jPanelContactDetails, JSplitPane splitPane,
                                   JScrollPane scrollPaneDetails) {
        this.jPanelContactDetails = jPanelContactDetails;
        this.splitPane = splitPane;
        this.scrollPaneDetails = scrollPaneDetails;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        clearselection(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        clearselection(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        clearselection(e);
    }

    public void clearselection(MouseEvent e) {
        if (e.getComponent() instanceof JList) {
            Point pClicked = e.getPoint();
            JList<?> list = (JList<?>) e.getSource();
            int index = list.locationToIndex(pClicked);
            Rectangle rec = list.getCellBounds(index, index);
            if (rec == null || !rec.contains(pClicked)) {
                list.clearSelection();
                this.jPanelContactDetails.setVisible(false);
                this.scrollPaneDetails.setVisible(false);
                splitPane.setDividerSize(0);
            }
        }
    }
}