package com.minghao;

import com.minghao.AuthenticationPanel;
import com.minghao.HttpRequests;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.abs;
import static org.jnativehook.keyboard.NativeKeyEvent.VC_ESCAPE;
import static org.jnativehook.keyboard.NativeKeyEvent.VC_PRINTSCREEN;

public class Main implements ActionListener, NativeKeyListener {
    // Declaring variables
    private Frame frame;
    private JFrame authFrame;
    private AuthenticationPanel panel;
    private String token;
    private boolean overlay = false;


    private Main() {
        int status_code = checkToken();
        if (status_code != 200)
            getInfo();
        else
            startListener();
    }

    private int checkToken() {
        if (new File("tokenText.txt").exists()) {
            try {
                token = new String(Files.readAllBytes(Paths.get("tokenText.txt")));
                HttpRequests con = new HttpRequests();
                return con.testToken("auth_test", token);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 401;
    }

    private void startListener() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        // Get the logger for "org.jnativehook" and set the level to warning.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);

        // Don't forget to disable the parent handlers.
        logger.setUseParentHandlers(false);

        GlobalScreen.addNativeKeyListener(this);
    }

    private void getInfo() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        authFrame = new JFrame("Login");
        authFrame.setSize(500, 350);
        authFrame.setLocation(dim.width / 2 - authFrame.getSize().width / 2, dim.height / 2 - authFrame.getSize().height / 2);
        authFrame.setResizable(false);
        authFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // JPanel
        panel = new AuthenticationPanel();
        authFrame.add(panel);
        panel.getButton().addActionListener(this);
        authFrame.setVisible(true);

    }

    private void authentication() throws IOException {
        String userName = panel.getUserNameText();
        String password = panel.getPasswordText();
        JSONObject login = new JSONObject();
        login.put("username", userName);
        login.put("password", password);
        HttpRequests con = new HttpRequests();
        token = con.sendJson("login", login);
        authFrame.dispose();
        saveToken();
        startListener();
    }

    private void saveToken() {

    }

    /**
     * Description: Take a complete screenshot of the the main monitor
     *
     * @return : Return the capture screenshot as a buffered-image
     */
    private BufferedImage displayFreezeScreen() {
        // Getting user width and height of the monitor
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;

        try {
            Robot robot = new Robot();
            Rectangle captureRect = new Rectangle(screenWidth, screenHeight);
            return robot.createScreenCapture(captureRect);

        } catch (AWTException e) {
            System.out.println("Couldn't take the screenshot error");
        }
        return null;
    }

    private void uploadPicture() {
        try {
            if ((frame.getX2() > 0 || frame.getY2() > 0)) {
                Robot robot = new Robot();
                String fileName = "file" + ".png";
                Rectangle captureRect = new Rectangle(abs(frame.getX1()), abs(frame.getY1()), abs(frame.getX2() - frame.getX1()), abs(frame.getY2() - frame.getY1()));
                BufferedImage screenFullImage = robot.createScreenCapture(captureRect);
                ImageIO.write(screenFullImage, "png", new File(fileName));
                HttpRequests con = new HttpRequests();
                String url = con.postImage(fileName, "image", token);
                System.out.println(url);
                frame.dispose();
            }
        } catch (AWTException | IOException ex) {
            System.err.println(ex);
        }
    }

    /**
     * Description: Capture a screenshot based on a rectangle the user draws
     * (current only save to file)
     */
    private void CaptureScreenShot(String nameOfFile) {
        try {
            if ((frame.getX2() > 0 || frame.getY2() > 0)) {
                Robot robot = new Robot();
                String fileName = nameOfFile + ".png";
                Rectangle captureRect = new Rectangle(abs(frame.getX1()), abs(frame.getY1()), abs(frame.getX2() - frame.getX1()), abs(frame.getY2() - frame.getY1()));
                BufferedImage screenFullImage = robot.createScreenCapture(captureRect);
                ImageIO.write(screenFullImage, "png", new File(fileName));
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
        // JFrame
        frame = new Frame(displayFreezeScreen());

        // Setting listener for the buttons
        for (int i = 0; i < frame.getButtonSize(); i++) {
            frame.getButton(i).addActionListener(this);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        // Getting the source of the button that is pressed
        JButton temp = (JButton) e.getSource();
        // Comparing the button pressed with all the buttons
        if (temp == panel.getButton()) {
            try {
                authentication();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else if (temp == frame.getButton(0)) {
            frame.setTextFieldLocation();
        } else if (temp == frame.getButton(1)) {
            uploadPicture();
        } else if (temp == frame.getButton(2)) {
            CaptureScreenShot(frame.getTextField());
        }
    }


    public static void main(String arg[]) {
        new Main();
    }


    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {

        if (nativeKeyEvent.getKeyCode() == VC_PRINTSCREEN) {
            overlay = true;
            overlay();
        }
        if (nativeKeyEvent.getKeyCode() == VC_ESCAPE && overlay) {
            frame.dispose();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }
}
