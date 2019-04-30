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
// TODO: Fix the rectangle bug - done
// TODO: Better names for variables - Done


public class Main implements ActionListener {
    // Declaring variables
    private Frame frame;
    private ArrayList<JButton> Buttons = new ArrayList<>();
    private Main() {
        overlay();
        CaptureScreenShot();
    }

    /**
     * Description: Take a complete screenshot of the the main monitor
     * @return : Return the capture screenshot as a buffered-image
     */
    private BufferedImage displayFreezeScreen() {
        try {
            Robot robot = new Robot();
            Rectangle captureRect = new Rectangle(1920,1080);
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
            if (frame.getX2() > 0 || frame.getY2() > 0) {
                Robot robot = new Robot();
                String format = "jpg";
                String fileName = "PartialScreenshot." + format;
                Rectangle captureRect = new Rectangle(abs(frame.getX1()), abs(frame.getY1()), abs(frame.getX2() - frame.getX1()), abs(frame.getY2() - frame.getY1()));
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
     * Lets draw the rectangle using mouse adapter
     */
    private void overlay() {
        // Save button
        JButton button = new JButton();
        button.setSize(100, 50);
        button.setText("Save");
        button.addActionListener(this);
        Buttons.add(button);

        // Upload button
        button = new JButton();
        button.setSize(100, 50);
        button.setText("Upload");
        button.addActionListener(this);
        Buttons.add(button);



        // JFrame information
        frame = new Frame(displayFreezeScreen(), Buttons);
        frame.setSize(1920, 100);
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setBackground(new Color(0, 0, 0, 1));
        frame.setLayout(new GridBagLayout());
        frame.setSize(1920, 1080);

        // JButton information
        for (JButton aTest1 : Buttons) {
            frame.add(aTest1);
        }

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton temp = (JButton) e.getSource();
        // Check which button is pressed
        if(temp == Buttons.get(0)) {
            CaptureScreenShot();
        }else if(temp == Buttons.get(1)){
            System.out.println("This has to be implemented");
        }
    }

    public static void main(String arg[]) {
        new Main();
    }



}

