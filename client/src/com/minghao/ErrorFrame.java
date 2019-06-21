package com.minghao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ErrorFrame extends JFrame {
    private static JTextArea box;

    public ErrorFrame(){
        super("Debug Console");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(800, 500);
        setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);

        // JPanel
        add(new ErrorPanel(this));
    }

    void writeError(final String message, final Exception e, Class<?> aClass){
        // Adding to error JFrame
        box.append(message + " Error in " + aClass + ". Error message is: " + e + "\n");

        // Writing the log to the Text file
        Logger logger = Logger.getLogger(aClass.toString());

        try {
            FileHandler FH = new FileHandler(aClass.toString());
            logger.addHandler(FH);
            SimpleFormatter formatter = new SimpleFormatter();
            FH.setFormatter(formatter);
            logger.log(Level.SEVERE, message, e);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private class ErrorPanel extends JPanel{

        ErrorPanel(final JFrame parent) {
            this.setBorder(new EmptyBorder(5,5,5,5));
            this.setLayout(new BorderLayout(0,0));
            box = new JTextArea(parent.getWidth(), parent.getHeight());
            this.add(box);
            JScrollPane sp = new JScrollPane(box, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            this.add(sp);
        }
    }
}
