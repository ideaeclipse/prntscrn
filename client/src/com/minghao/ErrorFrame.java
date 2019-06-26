package com.minghao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to display any message to the debug console
 * The debug console is toggleable by hitting the pause button
 *
 * @author {CompanyName}
 */
public class ErrorFrame extends JFrame {
    /**
     * The textbox which displays the messages
     */
    private static JTextArea box;

    /**
     * Sets up the jframe
     */
    public ErrorFrame() {
        super("Debug Console");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(800, 500);
        setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);

        // JPanel
        add(new ErrorPanel(this));
    }

    /**
     * Writes an error the box
     *
     * @param message message you want to add
     * @param e       the exception
     * @param aClass  and the class the error happened in
     */
    void writeError(final String message, final Exception e, Class<?> aClass) {
        // Adding to error JFrame
        if (aClass != null)
            box.append(message + " Error in " + aClass + ". Error message is: " + e + "\n");
        else
            box.append(message + "\n");

        // Writing the log to the Text file
        Logger logger = Logger.getLogger(aClass != null ? aClass.toString() : "unknown");
        logger.log(Level.SEVERE, message, e);
        /*
        try {
            FileHandler FH = new FileHandler(aClass != null ? aClass.toString() + ".txt" : "unknown.txt");
            logger.addHandler(FH);
            SimpleFormatter formatter = new SimpleFormatter();
            FH.setFormatter(formatter);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        */

    }

    /**
     * Writing a simple message to console
     *
     * @param message message to write
     */
    void writeMessage(final String message) {
        this.writeError(message, null, null);
    }

    /**
     * The Actual panel that displays the textbox
     *
     * @author {CompanyName}
     */
    private class ErrorPanel extends JPanel {

        /**
         * @param parent parent frame {@link ErrorFrame}
         */
        ErrorPanel(final JFrame parent) {
            this.setBorder(new EmptyBorder(5, 5, 5, 5));
            this.setLayout(new BorderLayout(0, 0));
            box = new JTextArea(parent.getWidth(), parent.getHeight());
            this.add(box);
            JScrollPane sp = new JScrollPane(box, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            this.add(sp);
        }
    }
}
