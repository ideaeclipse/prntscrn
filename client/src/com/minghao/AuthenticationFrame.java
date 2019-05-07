package com.minghao;

import javax.swing.*;
import java.awt.*;

public class AuthenticationFrame extends JPanel {

    JButton submit;
    JTextField userName, password;
    AuthenticationFrame() {
        this.setLayout(null);
        this.setBackground(Color.lightGray);

        // JTextField
        userName = new JTextField("Username");
        userName.setBounds(150, 145, 200, 35);
        userName.setBorder(BorderFactory.createLineBorder(Color.blue));
        userName.setBackground(Color.lightGray);
        this.add(userName);

        password = new JTextField("Password");
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
