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
    private static String token;

    public static void main(String[] arg) {
        try {
            new Main();
        } catch (NativeHookException e) {
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
                e.printStackTrace();
            }
        }
        return 401;
    }

    /**
     * Create the PrntscrnFrame
     */
    private void createFrame() throws AWTException {
        try {
            if (new HttpRequests().testToken("auth_test", token) == 200) {
                Rectangle screenRect = new Rectangle(0, 0, 0, 0);
                for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
                    screenRect = screenRect.union(gd.getDefaultConfiguration().getBounds());
                }
                frame = new PrntscrnFrame(new Robot().createScreenCapture(screenRect), token);
            }else{
                token = null;
                new AuthenticationFrame();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                        System.out.println("Take screenshot");
                        try {
                            if (token == null)
                                token = AuthenticationFrame.getToken();
                            //System.out.println(token);
                            createFrame();

                        } catch (AWTException e) {
                            System.out.println("Couldn't take the screenshot error");
                            System.exit(-1);
                        }
                    }
                } else if (nativeKeyEvent.getKeyCode() == 1) {
                    if (frame != null)
                        frame.dispose();
                }
            }

            @Override
            public void nativeKeyReleased(final NativeKeyEvent nativeKeyEvent) {

            }
        });
        if (checkToken() != 200)
            new AuthenticationFrame();

    }
}
