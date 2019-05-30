package com.minghao;


import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@SuppressWarnings("WeakerAccess")
class AuthenticationFrame extends JFrame {
    static String token;

    AuthenticationFrame() {
        super("Login");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(500, 350);
        setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
        setResizable(false);

        // JPanel
        add(new AuthenticationPanel(this));
        setVisible(true);
    }

    private static class AuthenticationPanel extends JPanel {
        JButton submit;
        JTextField userName, password;


        AuthenticationPanel(final JFrame parent) {
            // Panel information
            this.setLayout(null);
            this.setBackground(Color.lightGray);

            // Username JTextField
            userName = new JTextField("myles");
            userName.setBounds(150, 145, 200, 35);
            userName.setBorder(BorderFactory.createLineBorder(Color.blue));
            userName.setBackground(Color.lightGray);
            this.add(userName);

            // Password JTextField
            password = new JTextField("myles");
            password.setBounds(150, 185, 200, 35);
            password.setBorder(BorderFactory.createLineBorder(Color.blue));
            password.setBackground(Color.lightGray);
            this.add(password);

            // JButton
            submit = new JButton("Submit");
            submit.setBounds(200, 225, 100, 35);
            submit.addActionListener(e -> {
                System.out.println("submitted");
                try {
                    String userName = getUserNameText();
                    String password = getPasswordText();
                    JSONObject login = new JSONObject();
                    login.put("username", userName);
                    login.put("password", password);
                    HttpRequests con = new HttpRequests();
                    token = String.valueOf(new JSONObject(con.sendJson("login", login)).get("token"));
                    writeToken();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                System.out.println(token);
                parent.dispose();
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
                e.printStackTrace();
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