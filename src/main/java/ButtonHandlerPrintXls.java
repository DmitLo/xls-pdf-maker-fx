import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;

public class ButtonHandlerPrintXls implements ActionListener {

    private final JFrame frame;
    private final JTextField textFieldResult;
    private final JTextArea textArea;

    public ButtonHandlerPrintXls(JFrame frame, JTextField textFieldResult,
                                 JTextArea textArea) {
        this.frame = frame;
        this.textFieldResult = textFieldResult;
        this.textArea = textArea;
    }

    public void actionPerformed(ActionEvent e) {

//        String title = frame.getTitle();
//        System.out.println(title);
//
//        if (title.equals("Вставить подпись в XLS")) {
//            System.out.println("action occurred for checking");
//            //шкала
//            ProgBar.progress();
//
//            //получение списка файлов
//            List<String> strings = textArea.getText().lines().collect(Collectors.toList());
//            try {
//                ReadFromExcelInsertImage.readFromExcelImage(strings.get(0), textFieldResult.getText());
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//
//            //шкала
//            //ProgBar.progress();
//        }

        try {
            boolean complete = textArea.print(); // Displays a print dialog
            if (complete) {
                System.out.println("Printing complete.");
            } else {
                System.out.println("Printing cancelled.");
            }
        } catch (PrinterException pe) {
            System.err.println("Printing failed: " + pe.getMessage());
        }
    }
}

public class AdvancedExcelPrinter implements Printable {

    private HSSFWorkbook workbook;

    public AdvancedExcelPrinter(String filePath) throws Exception {
        FileInputStream fis = new FileInputStream(filePath);
        workbook = new HSSFWorkbook(fis);
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex >= workbook.getNumberOfSheets()) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        Sheet sheet = workbook.getSheetAt(pageIndex);
        // Implement logic to draw sheet content (rows, cells, text) onto g2d
        // This will involve iterating through rows and cells and drawing their content
        // You'll need to manage layout, font, cell sizes, etc.

        return PAGE_EXISTS;
    }

    public void initiatePrinting() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(null, "Error during printing: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            AdvancedExcelPrinter printer = new AdvancedExcelPrinter("C:/Users/YourUser/Documents/example.xls");
            printer.initiatePrinting();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error preparing Excel for printing: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}