import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Обработка ComboBox
 */

class ComboBoxHandler implements ActionListener {
    private JComboBox selectedItem;
    private JFrame frame;
    String title;


    public ComboBoxHandler(JComboBox selectedItem, JFrame frame) {

        this.selectedItem = selectedItem;
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent e) {

        title = (String) selectedItem.getSelectedItem();
        frame.setTitle(title);
    }

}
