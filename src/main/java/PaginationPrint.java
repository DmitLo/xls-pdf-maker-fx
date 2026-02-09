import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;

public class PaginationPrint implements Printable {

    int[] pageBreaks;  // array of page break line positions.
    List<List<Print>> listPrint;

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) {

        Font font = new Font("Serif", Font.PLAIN, 6);
        FontMetrics metrics = g.getFontMetrics(font);
        //int lineHeight = metrics.getHeight();
        int lineHeight = 12;
        int listPrintLength = 0;


        if (pageBreaks == null) {
            //initTextLines();

            int linesPerPage = (int) (pf.getImageableHeight() / lineHeight);

            listPrintLength = listPrint.size();
            System.out.println("listPrintLength = " + listPrintLength);

            //int numBreaks = (textLines.length-1)/linesPerPage;
            int numBreaks = (listPrintLength - 1) / linesPerPage;
            pageBreaks = new int[numBreaks];
            for (int b = 0; b < numBreaks; b++) {
                pageBreaks[b] = (b + 1) * linesPerPage;
                System.out.println("b = " + pageBreaks[b]);
            }
        }

        if (pageIndex > pageBreaks.length) {
            System.out.println("length = " + pageBreaks.length);
            return NO_SUCH_PAGE;
        }

        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         * Since we are drawing text we
         */
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        /* Draw each line that is on this page.
         * Increment 'y' position by lineHeight for each line.
         */
        int y = 0;
        //int coordY = 0;
        int start = (pageIndex == 0) ? 0 : pageBreaks[pageIndex - 1];
        System.out.println("start = " + start);
        int end = (pageIndex == pageBreaks.length)
                ? listPrintLength : pageBreaks[pageIndex];
        System.out.println("end = " + end);

        for (int line = start; line < end; line++) {
            y += lineHeight;
            for (int i = 0; i < listPrint.get(line).size(); i++) {
                Print tempLine = listPrint.get(line).get(i);
                //if (tempLine.getCurrentX() < 50) {
                    g.drawString(tempLine.getCurrentName(), tempLine.getCurrentX(), y);
               // }
                System.out.println("getCurrentName() = " + tempLine.getCurrentName()
                        + ", getCurrentX() = " + tempLine.getCurrentX() + ", getCurrentY() = " + tempLine.getCurrentY());
            }
        }

        System.out.println("повтор = " + pageIndex);

        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }
    
    public void mainPrint(JTextArea textArea) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        boolean ok = job.printDialog();
        if (ok) {
            try {
                listPrint = AdvancedExcelPrinter.initTextLines(textArea);
                System.out.println("listPrint length = " + listPrint.size());
                job.print();
            } catch (PrinterException ex) {
                /* The job did not successfully complete */
            }
        }
    }

    public static void printPage(JTextArea textArea) {
        new PaginationPrint().mainPrint(textArea);
    }
}
