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
    private ArrayList<JButton> button;


    public Frame(BufferedImage image, ArrayList<JButton> button1) {
        // Set the point to (0, 0)
        a.x = a.y = b.x = b.y = 0;

        // Paint the freeze image
        this.screenShot = image;

        // Mouse Listener
        MyMouseListener listener = new MyMouseListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);

        // JButton
        this.button = button1;
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


    // Mouse Listener and mouse motion listener
    class MyMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            // Get the mouse (x, y) and set it to point a
            setStartPoint(e.getX(), e.getY());
        }

        public void mouseReleased(MouseEvent e) {
            // Get the mouse (x, y) and set it to point b
            setEndPoint(e.getX(), e.getY());
            repaint();
            // Reposition the button based on the location of the rectangle
            for (int i = 0; i < button.size(); i++) {
                // Setting the buttons under the rectangle
                if (Math.max(a.y, b.y) < 1000) {
                    add(button.get(i));
                    button.get(i).setLocation(Math.max(a.x, b.x) - i * 75 - 60, Math.max(a.y, b.y));
                }
                // Setting the buttons inside the rectangle
                else {
                    add(button.get(i));
                    button.get(i).setLocation(Math.max(a.x, b.x) - i * 75 - 60, Math.max(a.y, b.y) - 25);
                }
            }
        }
    }


}