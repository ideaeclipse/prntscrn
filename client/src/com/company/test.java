package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

@SuppressWarnings("WeakerAccess")
public class test extends JPanel {

    int x, y, x2, y2;
    private BufferedImage screenShot;
    private ArrayList <JButton> button = new ArrayList<>();


    public test(BufferedImage image, ArrayList<JButton> button1) {
        x = y = x2 = y2 = 0;
        // Paint the freeze image
        this.screenShot = image;
        repaint();

        // Mouse Listener
        MyMouseListener listener = new MyMouseListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);

        // JButton
        this.button = button1;
    }

    private void setStartPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }


    private void setEndPoint(int x, int y) {
        x2 = (x);
        y2 = (y);
    }


    public int getX(){
        return x;
    }


    public int getY(){
        return y;
    }

    public int getX2(){
        return x2;
    }

    public int getY2(){
        return y2;
    }




    class MyMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {

            setStartPoint(e.getX(), e.getY());
            System.out.println(x+","+y);
        }


        public void mouseReleased(MouseEvent e) {
            setEndPoint(e.getX(), e.getY());
            repaint();
            for(int i = 0; i < button.size(); i++) {
                if (y2 < 1000) {
                    button.get(i).setLocation(x2 - i * 75 - 60, y2);
                } else {
                    button.get(i).setLocation(x2 - i * 75 - 60, y2 - 25);
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.drawImage(screenShot, 0, 0, this);
        drawPerfectRect(g, x, y, x2, y2);
    }

    private void drawPerfectRect(Graphics g, int x, int y, int x2, int y2) {
        int px = Math.min(x,x2);
        int py = Math.min(y,y2);
        int pw=Math.abs(x-x2);
        int ph=Math.abs(y-y2);
        g.drawRect(px, py, pw, ph);
    }

}