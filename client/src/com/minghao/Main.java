package com.minghao;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * TODO: rename package to com.{CompanyName}.client
 * TODO: rearrange installer to separate dir, and package name to com.{CompanyName}.installer
 * Entry point for the program.
 * This class will start the 2 required listeners, mouse and keyboard, see {@link Main#Main()}
 *
 * @author {CompanyName}
 */
public class Main {
    /**
     * This variable stores the instance of the ErrorFrame class, {@link ErrorFrame}
     * This variable will never be null.
     */
    private static final ErrorFrame errorFrame = new ErrorFrame();

    /**
     * This variable stores the instance of the PrntscrnFrame class, {@link PrntscrnFrame}
     * This variable is initialized inside the createFrame method {@link Main#createFrame()}
     */
    private static PrntscrnFrame frame = null;

    /**
     * This variable stores the users login token.
     * This variable is either initialized by reading from the tokenText.txt file or by making a web request to gain a fresh token see {@link Main#checkToken()}
     */
    private static String token = null;

    /**
     * This interface is used to pass a callback function to methods.
     * <p>
     * Example is in {@link Main#updateToken(CallBack)}
     */
    private interface CallBack {
        /**
         * Where user defined code goes for method callbacks
         *
         * @throws Exception allows for throwing of any exception
         */
        void run() throws Exception;
    }

    /**
     * Calls the main method {@link Main#Main()}
     *
     * @param arg nothing is done with command line arguments. Don't bother passing any
     */
    public static void main(String[] arg) {
        try {
            new Main();
        } catch (NativeHookException e) {
            errorFrame.writeError("Couldn't register native hook, please contact the one and only mayo", e, Main.class);
            System.out.println("Couldn't register native hook");
        }
    }

    /**
     * TODO: fix errors
     * This method will check if the tokenText.txt file. If the file exists it will make a web request to ensure
     * the token is not invalid. If the token is invalid you will be prompted to re login. If the file doesn't exist you will be prompted to re login
     * <p>
     * If everything is ok the program will start up as normal.
     * <p>
     * If you are prompted to relogin the tokenText.txt file will be repopulated with the token received from the login request
     *
     * @return response must be 200 to pass the case in {@link Main#Main()} otherwise you will be prompted to re login
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
     * This method will initialize the prntscrnframe. {@link PrntscrnFrame}
     * This function is called when the user clicks the customized button to take a screenshot
     * The users token will be tested using the authentication test endpoint. If it is valid it will execute the code
     * otherwise the authentication frame will be opened
     *
     * @throws IOException  The webservice is down
     * @throws AWTException The screenshot util has failed
     */
    private void createFrame() throws IOException, AWTException {
        if (new HttpRequests().testToken("auth_test", token) == 200) {
            Rectangle screenRect = new Rectangle(0, 0, 0, 0);
            errorFrame.writeMessage("Printing out monitor specifications");
            for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
                screenRect = screenRect.union(gd.getDefaultConfiguration().getBounds());
                errorFrame.writeMessage("Monitor spec: " + gd.getDefaultConfiguration().getBounds());
            }
            frame = new PrntscrnFrame(new Robot().createScreenCapture(screenRect), token, errorFrame);
        } else {
            token = null;
            updateToken(this::createFrame);
        }

    }

    /**
     * TODO: fix the e1.printStackTrace();
     * This function will prompt the user to relogin because their token has expired. It will then update the global variable token {@link Main#token}
     * Then it will execute the call back function, after waiting 250milliseconds for the authentication frame to disappear from the screen.
     *
     * @param callBack pass lambda function to execute some code once the token has been updated
     */
    private void updateToken(final CallBack callBack) {
        token = new AuthenticationFrame(errorFrame).getToken();
        errorFrame.writeMessage("Token has been updated to: " + token);
        Timer timer = new Timer(250, e -> {
            try {
                callBack.run();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * This method is only called from {@link Main#main(String[])} on program entry
     * This will register the according listeners and prompt the user for login if required.
     *
     * @throws NativeHookException If the program can't access the native keyboard/mouse listener functions
     */
    private Main() throws NativeHookException {
        GlobalScreen.registerNativeHook();
        Logger.getLogger(GlobalScreen.class.getPackage().getName()).setLevel(Level.OFF);
        GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
            @Override
            public void nativeKeyTyped(final NativeKeyEvent nativeKeyEvent) {

            }

            /**
             * This function checks to see if the user presses the correct key to take a screenshot
             * If the user has a valid token the frame will be created else {@link Main#updateToken(CallBack)} is called
             *
             * @param nativeKeyEvent pass from {@link NativeKeyListener}
             */
            @Override
            public void nativeKeyPressed(final NativeKeyEvent nativeKeyEvent) {
                if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_PRINTSCREEN) {
                    if (frame == null || !frame.isVisible()) {
                        try {
                            if (token == null)
                                updateToken(() -> createFrame());
                            else
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
                    errorFrame.setVisible(!errorFrame.isVisible());
                }
            }

            @Override
            public void nativeKeyReleased(final NativeKeyEvent nativeKeyEvent) {

            }
        });


        if (checkToken() != 200)
            token = new AuthenticationFrame(errorFrame).getToken();
        else {
            new Menu(errorFrame);
        }
    }
}
