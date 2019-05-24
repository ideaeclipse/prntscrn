package com.minghao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

@SuppressWarnings("WeakerAccess")
public class Frame extends JFrame {

    // Declaring variables
    Point a = new Point(), b = new Point();
    private BufferedImage screenShot;
    private ArrayList<JButton> buttons;
    JTextField name;

    public Frame(BufferedImage image) {
        // Getting user width and height of the monitor
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;

        // Setting JFrame information
        this.setSize(screenWidth, screenHeight);
        this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.setBackground(new Color(0, 0, 0, 1));
        this.setLayout(new GridBagLayout());
        this.setVisible(true);



        // Set the point to (0, 0)
        a.x = a.y = b.x = b.y = 0;

        // Paint the freeze image
        this.screenShot = image;

        // Mouse Listener
        MyMouseListener listener = new MyMouseListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);

        // JButton
        buttons = new ArrayList<>();

        // Save button
        JButton button = new JButton();
        button.setSize(75, 25);
        button.setText("Save");
        buttons.add(button);

        // Upload button
        button = new JButton();
        button.setSize(75, 25);
        button.setText("Upload");
        buttons.add(button);

        // Submit button
        button = new JButton();
        button.setSize(75, 25);
        button.setText("Submit");
        buttons.add(button);

        // JTextfield
        name = new JTextField();
        this.add(name);
    }

    public int removeButton(){
        buttons.get(0).setLocation(-100, -100);
        return 1;
    }


    // Following setter sets the top left and bottom right corner of the rectangle
    private void setStartPoint(int x, int y) {
        a.setLocation(x, y);
    }


    private void setEndPoint(int x, int y) {
        b.setLocation(x, y);
    }


    // Following getter gets the top left and bottom right corner of the rectangle
    public int getX1() {
        return a.x;
    }

    public int getY1() {
        return a.y;
    }

    public int getX2() {
        return b.x;
    }

    public int getY2() {
        return b.y;
    }

    // Following getter are for the buttons
    public int getButtonSize(){
        return buttons.size();
    }

    public JButton getButton(int i){
        return buttons.get(i);
    }

    /**
     * Description: Draws rectangle and the freeze screen to the JFrame
     *              This function is called by default and is called again when repaint() is called
     * @param g: Graphics
     */
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.RED);
        g.drawImage(screenShot, 0, 0, this);
        drawPerfectRect(g, a, b);
    }

    /**
     * Description: Draw a rectangle onto the screen
     * @param g: Graphics
     * @param a: Top left corner of the rectangle
     * @param b: Bottom right corner of the rectangle
     */
    private void drawPerfectRect(Graphics g, Point a, Point b) {
        // Get the top left corner
        int px = Math.min(a.x, b.x);
        int py = Math.min(a.y, b.y);
        // Get the width and height
        int pw = Math.abs(a.x - b.x);
        int ph = Math.abs(a.y - b.y);
        // Drawing to the screen
        g.drawRect(px, py, pw, ph);
    }

    public void setTextFieldLocation(){
        name.setVisible(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;

        buttons.get(2).setVisible(true);
        if(buttons.get(0).getX() < screenWidth - 250) {
            name.setBounds(buttons.get(0).getX() + 75, buttons.get(0).getY(), 200, 25);
            add(buttons.get(2));
            buttons.get(2).setLocation(buttons.get(0).getX() + 275, name.getY());
        }
        else {
            name.setBounds(buttons.get(1).getX() - 200, buttons.get(0).getY(), 200, 25);
            add(buttons.get(2));
            buttons.get(2).setLocation(buttons.get(1).getX() - 275, name.getY());
        }
    }

    public String getTextField(){
        return name.getText();
    }

    // Mouse Listener and mouse motion listener
    class MyMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            remove(buttons.get(2));

            // Get the mouse (x, y) and set it to point a
            setStartPoint(e.getX(), e.getY());
        }

        public void mouseReleased(MouseEvent e) {
            if(name != null)
                name.setVisible(false);
            // Getting user width and height of the monitor
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenHeight = screenSize.height;
            // Get the mouse (x, y) and set it to point b
            setEndPoint(e.getX(), e.getY());
            repaint();
            // Reposition the button based on the location of the rectangle
            for (int i = 0; i < buttons.size() - 1; i++) {
                // Setting the buttons under the rectangle
                if (Math.max(a.y, b.y) < screenHeight - 200) {
                    add(buttons.get(i));
                    buttons.get(i).setLocation(Math.max(a.x, b.x) - i * 75 - 73, Math.max(a.y, b.y));
                }
                // Setting the buttons inside the rectangle
                else {
                    add(buttons.get(i));
                    buttons.get(i).setLocation(Math.max(a.x, b.x) - i * 75 - 73, Math.max(a.y, b.y) - 25);
                }
            }

        }
    }
}