package com.minghao;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import static java.lang.Math.abs;


// TODO: Let user resize the rectangle and get the coordinates of the corner - done
// TODO: Freeze the screen - done
// TODO: Export the picture
// TODO: Add authentication (?)
// TODO: Fix the rectangle bug
// TODO: Better names for variables


public class Main implements ActionListener {
    private test newPanel;
    private ArrayList<JButton> test1 = new ArrayList<>();
    private Main() {
        overlay();
        CaptureScreenShot();
    }

    /**
     * Description: Take a complete screenshot of the the main monitor
     * @return: Return the capture screenshot as a buffered-image
     */
    private BufferedImage displayFreezeScreen() {
        try {
            Robot robot = new Robot();
            Rectangle captureRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            return robot.createScreenCapture(captureRect);

        } catch (AWTException e) {
            System.out.println("Couldn't take the screenshot error");
        }
        return null;
    }

    /**
     * Description: Capture a screenshot based on a rectangle the user draws
     * (current only save to file)
     */
    private void CaptureScreenShot() {
        try {
            // Declaring variables
            if (newPanel.getX2() > 0 || newPanel.getY2() > 0) {
                Robot robot = new Robot();
                String format = "jpg";
                String fileName = "PartialScreenshot." + format;
                Rectangle captureRect = new Rectangle(abs(newPanel.getX()), abs(newPanel.getY()), abs(newPanel.getX2() - newPanel.getX()), abs(newPanel.getY2() - newPanel.getY()));
                BufferedImage screenFullImage = robot.createScreenCapture(captureRect);
                ImageIO.write(screenFullImage, format, new File(fileName));
                System.out.println("A partial screenshot saved!");

            }
        } catch (AWTException | IOException ex) {
            System.err.println(ex);
        }
    }


    /**
     * Create an overlay for of area where the screenshot can be taken
     * Lets draw the rectangle using mouse adpater
     */
    private void overlay() {
        // Get the size of the monitor
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        // JFrame information
        JFrame frame = new JFrame("Example");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setBackground(new Color(0, 0, 0, 1));
        frame.setLayout(new GridBagLayout());

        // TODO: I think we don't need (?) needs more test
        //getRootPane(frame).setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.DARK_GRAY));
        //frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        // JButton

        // Save button
        JButton buttons = new JButton();
        buttons.setSize(100, 50);
        buttons.setText("Save");
        buttons.addActionListener(this);
        test1.add(buttons);

        // Upload button
        buttons = new JButton();
        buttons.setSize(100, 50);
        buttons.setText("Upload");
        buttons.addActionListener(this);
        test1.add(buttons);

        // Panel for the rectangle
        newPanel = new test(displayFreezeScreen(), test1);
        newPanel.setOpaque(true);
        newPanel.setBackground(new Color(255, 255, 255, 0));
        frame.setContentPane(newPanel);
        for (JButton aTest1 : test1) {
            frame.add(aTest1);
        }
        frame.setVisible(true);
    }

    public static void main(String arg[]) {
        new Main();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton temp = (JButton) e.getSource();
        // Check which button is pressed
        if(temp == test1.get(0)) {
            CaptureScreenShot();
        }else if(temp == test1.get(1)){
            System.out.println("This has to be implemeneted");
        }
    }
}

