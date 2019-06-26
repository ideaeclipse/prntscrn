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

/**
 * This class is used to display the freezed image and allow the user to select a sub-region to capture and upload
 * <p>
 * This class is called from {@link Main#createFrame()}
 *
 * @author {CompanyName}
 */
class PrntscrnFrame extends JFrame {

    /**
     * This value stores the web token which allows for web requests,
     * This value is passed from {@link Main#createFrame()}
     */
    private final String token;

    PrntscrnFrame(BufferedImage image, final String token, final ErrorFrame errorFrame) {
        this.token = token;
        setUndecorated(true);
        setContentPane(new ImagePanel(image));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        int minimumX = 0, minimumY = 0;

        // This block of code is used to calculate where the lowest point points on your displays are to adjust the image display accordingly
        {
            for (GraphicsDevice g : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
                if (g.getDefaultConfiguration().getBounds().getX() < minimumX)
                    minimumX = (int) g.getDefaultConfiguration().getBounds().getX();
                if (g.getDefaultConfiguration().getBounds().getY() < minimumY)
                    minimumY = (int) g.getDefaultConfiguration().getBounds().getY();
            }
            errorFrame.writeMessage("Minimum x value is: " + minimumX);
            errorFrame.writeMessage("Minimum y value is: " + minimumY);
        }

        setLocation(minimumX, minimumY);
        add(new DrawPanel(this, image, errorFrame));
        pack();
        setVisible(true);
        int state = super.getExtendedState();
        state &= ~JFrame.ICONIFIED;
        setExtendedState(state);
        setAlwaysOnTop(true);
        toFront();
        requestFocus();
        setAlwaysOnTop(false);
    }

    /**
     * This method allows the background of the foreground frame to be whatever the background frame is displaying.
     * Which in this case will be the DrawPanel displaying the ImagePanel
     *
     * @param g      this value is generated form {@link DrawPanel#paintComponent(Graphics)}
     * @param width  width of the image drawn in {@link ImagePanel#getPreferredSize()}
     * @param height height of the image drawn in {@link ImagePanel#getPreferredSize()}
     */
    private static void transparent(final Graphics g, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        Composite old = g2d.getComposite();
        g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
        g2d.fillRect(0, 0, width, height);
        g2d.setComposite(old);
    }

    /**
     * This class is used to draw the generated freezed image from {@link Main#createFrame()}
     * Then a {@link DrawPanel} will be drawn on top of this and will be transparent and the image drawn here will be seen
     *
     * @author {CompanyName}
     */
    private static final class ImagePanel extends JComponent {
        /**
         * This is the image stored as a variable
         */
        private final BufferedImage image;

        /**
         * @param image passed background image to display
         */
        ImagePanel(final BufferedImage image) {
            setLayout(new BorderLayout());
            this.image = image;
        }

        /**
         * @return sets the size of the frame to be the size of the
         */
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(image.getWidth(), image.getHeight());
        }

        /**
         * draws the image to the screen
         *
         * @param g passed from parent call
         */
        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }

    /**
     * This class allows for drawing of the sub-region rectangle
     * <p>
     * allows for uploading/capturing of the sub-region rectangle
     *
     * @author {CompanyName}
     */
    private final class DrawPanel extends JPanel {

        /**
         * A global storage of the upload button.
         */
        private final JButton upload = new JButton();

        /**
         * Point a is the point of initial click.
         * This variable is only updated on mouse click
         */
        private final Point a = new Point();

        /**
         * Point b is the point where the user is dragging
         * This point is updated everytime mouseDragged is called
         */
        private final Point b = new Point();

        /**
         * Sets up an action listener for the click of the upload button
         * Sets up a mouse listener for when the mouse is dragged/pressed
         *
         * @param frame      the parent frame
         * @param image      the image that is drawn behind it
         * @param errorFrame an errorFrame instance
         */
        DrawPanel(final JFrame frame, final BufferedImage image, final ErrorFrame errorFrame) {
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

            //Called once the upload button is clicked
            Timer timer = new Timer(250, e -> {
                if (!isVisible()) {
                    try {
                        File tempFile = File.createTempFile("image-", ".png");
                        ImageIO.write(image.getSubimage(Math.min(a.x, b.x), Math.min(a.y, b.y), abs(a.x - b.x), abs(a.y - b.y)), "png", tempFile);
                        String url = String.valueOf(new JSONObject(new HttpRequests().postImage(tempFile, "image", token)).get("uuid"));
                        tempFile.deleteOnExit();
                        Desktop.getDesktop().browse(new URI(url));
                    } catch (URISyntaxException e1) {
                        errorFrame.writeError("URLSyntax error, Please contact the one and only mayo!", e1, this.getClass());
                    } catch (IOException e2) {
                        errorFrame.writeError("One or both of the two following error(s) has occurred: Unable open connection or unable to upload image. Please contact the one and only mayo!", e2, this.getClass());
                    }
                    frame.dispose();
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