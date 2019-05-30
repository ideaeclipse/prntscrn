package com.minghao;

import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.Math.abs;

@SuppressWarnings("WeakerAccess")
class PrntscrnFrame extends JFrame {

    private static String token;

    PrntscrnFrame(BufferedImage image, String token) {
        PrntscrnFrame.token = token;
        setUndecorated(true);
        setContentPane(new ImagePanel(image));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(new DrawPanel(this));
        pack();
        setVisible(true);
        int state = super.getExtendedState();
        state &= ~JFrame.ICONIFIED;
        System.out.println(super.getExtendedState());
        setExtendedState(state);
        setAlwaysOnTop(true);
        toFront();
        requestFocus();
        setAlwaysOnTop(false);
    }

    /**
     * This method is to be called inside {@link JPanel#paint(Graphics)} to allow for a transparent panel
     *
     * @param g      graphics value passed from paint
     * @param width  dependent on the frame
     * @param height dependent on the frame
     */
    static void transparent(final Graphics g, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        Composite old = g2d.getComposite();
        g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
        g2d.fillRect(0, 0, width, height);
        g2d.setComposite(old);
    }

    class ImagePanel extends JComponent {
        private final BufferedImage image;

        ImagePanel(final BufferedImage image) {
            setLayout(new BorderLayout());
            this.image = image;
        }

        /**
         * Allows for you to call pack in the frame.
         *
         * @return tells the parent jframe how big the image is
         */

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(image.getWidth(), image.getHeight());
        }


        /**
         * Draws the image to the screen and all parent components
         *
         * @param g passed from {@link JComponent}
         */
        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }

    /**
     * Allows the user to select a region of the screen and upload to the prntscrn-api
     */
    static class DrawPanel extends JPanel {
        private final JButton upload = new JButton();
        private final Point a = new Point();
        private final Point b = new Point();

        /**
         * Border Layout makes the size of this panel the size of the parent.
         *
         * @param frame parent jframe, allows for disposal
         */
        DrawPanel(final JFrame frame) {
            setLayout(new BorderLayout());
            setOpaque(false);

            a.x = a.y = b.x = b.y = 0;

            final MouseAdapter listener = new MouseAdapter() {
                @Override
                public void mousePressed(final MouseEvent e) {
                    a.setLocation(e.getX(), e.getY());
                }

                @Override
                public void mouseDragged(final MouseEvent e) {
                    int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
                    b.setLocation(e.getX(), e.getY());
                    if (Math.max(a.y, b.y) < screenHeight - 200) {
                        upload.setLocation(Math.max(a.x, b.x) - 73, Math.max(a.y, b.y));
                        add(upload);
                    } else {
                        upload.setLocation(Math.max(a.x, b.x) - 73, Math.max(a.y, b.y) - 25);
                        add(upload);
                    }
                    repaint();
                }
            };


            addMouseListener(listener);
            addMouseMotionListener(listener);

            Timer timer = new Timer(250, e -> {
                if (!isVisible()) {
                    try {
                        if ((b.x > 0 || b.y > 0)) {
                            Robot robot = new Robot();
                            String fileName = "file" + ".png";
                            Rectangle captureRect = new Rectangle(abs(a.x), abs(a.y), abs(b.x - a.x), abs(b.y - a.y));
                            BufferedImage screenFullImage = robot.createScreenCapture(captureRect);
                            ImageIO.write(screenFullImage, "png", new File(fileName));
                            frame.dispose();
                            HttpRequests con = new HttpRequests();
                            String url = String.valueOf(new JSONObject(con.postImage(fileName, "image", token)).get("uuid"));
                            Desktop.getDesktop().browse(new URI(url));
                        }
                    } catch (AWTException | IOException ex) {
                        System.err.println(ex);
                    } catch (URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            timer.setRepeats(false);

            this.upload.setSize(75, 25);
            this.upload.setText("Upload");
            this.upload.addActionListener(e -> {
                setVisible(false);
                repaint();
                timer.start();
            });
        }

        /**
         * Draws rectangle based on mouse input
         *
         * @param g from {@link JPanel}
         */
        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);
            transparent(g, getWidth(), getHeight());
            g.setColor(Color.RED);
            g.drawRect(Math.min(a.x, b.x), Math.min(a.y, b.y), abs(a.x - b.x), abs(a.y - b.y));
        }
    }
}