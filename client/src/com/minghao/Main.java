package com.minghao;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static PrntscrnFrame frame = null;
    private static String token = null;
    private static final ErrorFrame errorFrame = new ErrorFrame();

    public static void main(String[] arg) {

        try {
            new Main();
        } catch (NativeHookException e) {
            errorFrame.writeError("Couldn't register native hook, please contact the one and only mayo", e, Main.class);
            System.out.println("Couldn't register native hook");
        }
    }

    /**
     * Check if existing token(if there is one stored) is valid
     *
     * @return status code
     */
    private int checkToken() {
        if (new File("tokenText.txt").exists()) {
            try {
                token = new String(Files.readAllBytes(Paths.get("tokenText.txt")));
                HttpRequests con = new HttpRequests();
                return con.testToken("auth_test", token);
            } catch (IOException e) {
                errorFrame.writeError("One of two following error has occurred: You don't have permission to open the tokenText.txt file, or the connection to the REST services has failed please contact the one and only mayo", e, this.getClass());
            }
        }
        return 401;
    }

    /**
     * Create the PrntscrnFrame
     */
    private void createFrame() throws IOException, AWTException {

        if (new HttpRequests().testToken("auth_test", token) == 200) {
            Rectangle screenRect = new Rectangle(0, 0, 0, 0);
            for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
                screenRect = screenRect.union(gd.getDefaultConfiguration().getBounds());
            }
            frame = new PrntscrnFrame(new Robot().createScreenCapture(screenRect), token, errorFrame);
        } else {
            token = null;
            new AuthenticationFrame(errorFrame);
        }

    }

    /**
     * This class starts the keyboard listener.
     * Allows the user to hit the prntscrn button on their keyboard
     * to take an image of their screen
     */
    private Main() throws NativeHookException {
        GlobalScreen.registerNativeHook();
        Logger.getLogger(GlobalScreen.class.getPackage().getName()).setLevel(Level.OFF);
        GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
            @Override
            public void nativeKeyTyped(final NativeKeyEvent nativeKeyEvent) {

            }

            /**
             * If key is prntscrn take a screenshot of the screen and create the frame to display this frame
             * If the key is esc dispose the frame
             *
             * @param nativeKeyEvent passed from {@link NativeKeyListener}
             */
            @Override
            public void nativeKeyPressed(final NativeKeyEvent nativeKeyEvent) {
                if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_PRINTSCREEN) {
                    if (frame == null || !frame.isVisible()) {
                        try {
                            if (token == null)
                                token = AuthenticationFrame.getToken();
                            createFrame();
                        } catch (AWTException e) {
                            errorFrame.writeError("The ROBOT has failed to capture a screenshot, please contact the one and only mayo", e, this.getClass());
                            System.exit(-1);
                        } catch (IOException e) {
                            errorFrame.writeError("The connection to REST service has failed.  Please contact the one and only mayo", e, this.getClass());
                        }
                    }
                } else if (nativeKeyEvent.getKeyCode() == 1) {
                    if (frame != null)
                        frame.dispose();
                } else if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_PAUSE) {
                    if (errorFrame.isVisible())
                        errorFrame.setVisible(false);
                    else
                        errorFrame.setVisible(true);
                }
            }

            @Override
            public void nativeKeyReleased(final NativeKeyEvent nativeKeyEvent) {

            }
        });
        if (checkToken() != 200)
            new AuthenticationFrame(errorFrame);

    }
}
