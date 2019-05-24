package com.minghao;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("WeakerAccess")
public class AuthenticationPanel extends JPanel {


    JButton submit;
    JTextField userName, password;

    AuthenticationPanel() {
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
        this.add(submit);

    }

    public JButton getButton() {
        return submit;
    }

    public String getUserNameText(){
        return userName.getText();
    }

    public String getPasswordText(){
        return password.getText();
    }

}