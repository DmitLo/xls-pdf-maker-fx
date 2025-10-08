import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.io.FileReader;
import java.util.List;

/**
 * Добавление Drag And Drop
 */

public class DragAndDrop {

    public void enableDragAndDrop(JTextArea textArea)
    {
        DropTarget target=new DropTarget(textArea, new DropTargetListener(){
            public void dragEnter(DropTargetDragEvent e)
            {
            }

            public void dragExit(DropTargetEvent e)
            {
            }

            public void dragOver(DropTargetDragEvent e)
            {
            }

            public void dropActionChanged(DropTargetDragEvent e)
            {
            }

            public void drop(DropTargetDropEvent e)
            {
                try
                {
                    // Accept the drop first, important!
                    e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);


                    List list = (java.util.List) e.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    File file = (File) list.get(0);

//                    for (File file : droppedFiles) {
//                        /*
//                         * NOTE:
//                         *  When I change this to a println,
//                         *  it prints the correct path
//                         */
//                        myPanel.setText(file.getAbsolutePath());

                    String filename = file.getAbsolutePath();
                    // jt.read(new FileReader(file),null);
                    //добавление в элемент с новой строки
                    textArea.getDocument().insertString(0, filename + "\n", null);

                }catch(Exception ignored){}
            }
        });
    }
}
