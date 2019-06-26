package com.minghao;


import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@SuppressWarnings("WeakerAccess")
class AuthenticationFrame extends JFrame {
    private static String token;
    private final ErrorFrame errorFrame;

    AuthenticationFrame(final ErrorFrame errorFrame) {
        super("Login");
        this.errorFrame = errorFrame;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(500, 350);
        setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
        setResizable(false);

        // JPanel
        add(new AuthenticationPanel(this));
        setVisible(true);
    }

    static String getToken(){
        return token;
    }


    private class AuthenticationPanel extends JPanel {
        JButton submit;
        JTextField userName, password;

        AuthenticationPanel(final JFrame parent) {
            // Panel information
            this.setLayout(null);
            this.setBackground(Color.lightGray);

            // Username JTextField
            userName = new JTextField("Username");
            userName.setBounds(150, 145, 200, 35);
            userName.setBorder(BorderFactory.createLineBorder(Color.blue));
            userName.setBackground(Color.lightGray);
            userName.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                   if(userName.getText().equals("Username"))
                        userName.setText("");
                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });
            this.add(userName);

            // Password JTextField
            password = new JTextField("Password");
            password.setBounds(150, 185, 200, 35);
            password.setBorder(BorderFactory.createLineBorder(Color.blue));
            password.setBackground(Color.lightGray);
            password.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {}

                @Override
                public void keyPressed(KeyEvent e) {
                    if(password.getText().equals("Password"))
                        password.setText("");
                }

                @Override
                public void keyReleased(KeyEvent e) {}

            });
            this.add(password);

            // JButton
            submit = new JButton("Submit");
            submit.setBounds(200, 225, 100, 35);
            submit.addActionListener(e -> {
                try {
                    String userName = getUserNameText();
                    String password = getPasswordText();
                    JSONObject login = new JSONObject();
                    login.put("username", userName);
                    login.put("password", password);
                    HttpRequests con = new HttpRequests();
                    token = String.valueOf(new JSONObject(con.sendJson("login", login)).get("token"));
                    writeToken();
                    parent.dispose();
                    new Menu(errorFrame);
                } catch (IOException e1) {
                    JLabel invalid = new JLabel("Invalid username and password", JLabel.CENTER);
                    if (!invalid.isVisible()) {
                        invalid.setBounds(150, 300, 200, 150);
                        invalid.setForeground(Color.RED);
                        add(invalid);
                        this.repaint();
                        errorFrame.writeError("The user has enter an invalid password, if you do not have an username or password; please contact the one and only mayo", e1, this.getClass());
                    }
                }
            });
            this.add(submit);
        }

        /**
         * Write the token to file
         */
        private void writeToken(){
            try {
                PrintWriter printWriter = new PrintWriter(new FileWriter("tokenText.txt"));
                printWriter.print(token);
                printWriter.close();
            } catch (IOException e) {
               errorFrame.writeError("Unable to write token to file, user does not have permission to write to that directory", e, this.getClass());
            }
        }

        /**
         * Get string from username textfield
         * @return Input from username textfield
         */
        private String getUserNameText() {
            return userName.getText();
        }

        /**
         * Get string from password textfield
         * @return Input from password textfield
         */
        private String getPasswordText() {
            return password.getText();
        }
    }
}