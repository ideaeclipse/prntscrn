package com.installer;

import com.minghao.ErrorFrame;
import com.minghao.HttpRequests;
import com.sun.jna.platform.win32.Advapi32Util;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import static com.sun.jna.platform.win32.WinReg.HKEY_CURRENT_USER;


class InstallFrame extends JFrame {
    private final ErrorFrame errorFrame;

    InstallFrame(final ErrorFrame errorFrame) {
        super("Print Screen Clone Installer");
        this.errorFrame = errorFrame;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(750, 500);
        setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
        setResizable(false);
        this.add(new InstallPanel());
        this.setVisible(true);
    }

    private class InstallPanel extends JPanel {

        InstallPanel() {
            this.setLayout(null);

            // Getting versions from the api and storing them as a list
            List<String> version = new LinkedList<>();
            List<String> url = new LinkedList<>();
            try {
                JSONArray array = new JSONArray(new HttpRequests().getVersion("executable"));
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonobject = array.getJSONObject(i);
                    version.add(jsonobject.getString("version"));
                    url.add(jsonobject.getString("url"));
                }

            } catch (IOException e) {
                try {
                    Method method = errorFrame.getClass().getDeclaredMethod("writeError", String.class, Exception.class, Class.class);
                    method.setAccessible(true);
                    method.invoke(errorFrame, "Unable to connected to the REST service to get version please contact the one and only mayo. ", e, this.getClass());
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
                    e1.printStackTrace();
                }

            }
            // JButtons f
            JButton[] download = new JButton[version.size()];
            for (int i = 0; i < download.length; i++) {
                download[i] = new JButton(version.get(i));
                download[i].setBounds(325, 225 + i * 50, 100, 35);
                String urlTemp = url.get(i);
                // Setting listener for JButton
                download[i].addActionListener(e -> {
                    try {
                        // Get user inputted directory
                        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                        jfc.setDialogTitle("Choose a directory to save your file: ");
                        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                        int returnValue = jfc.showSaveDialog(null);
                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            if (jfc.getSelectedFile().isDirectory()) {
                                // Get the file from the url
                                BufferedInputStream in = new BufferedInputStream(new URL(urlTemp).openStream());
                                FileOutputStream fileOutputStream = new FileOutputStream(jfc.getSelectedFile() + "\\PrintScreen-Clone.exe.jar");
                                byte[] dataBuffer = new byte[1024];
                                int bytesRead;
                                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                                }
                                // Adding program to registry
                                Advapi32Util.registrySetStringValue(HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "PrintScreen-Clone", jfc.getSelectedFile() + "\\PrintScreen-Clone.exe.jar");
                                System.exit(0);
                            }
                        }

                    } catch (IOException e2) {
                        // Error Label
                        JLabel Error = new JLabel("An error has occurred please contact the one and only mayo", JLabel.CENTER);
                        Error.setFont(new Font("Courier New", Font.BOLD, 25));
                        Error.setBounds(0, 100, 500, 100);
                        Error.setForeground(Color.BLUE);
                        add(Error);
                        this.repaint();
                        try {
                            Method method = errorFrame.getClass().getDeclaredMethod("writeError", String.class, Exception.class, Class.class);
                            method.setAccessible(true);
                            method.invoke(errorFrame, "One of the two error(s) has occurred:  Unable to connect to the REST service; unable to download file.  Please contact the one and only mayo.  ", e2, this.getClass());
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
                            e1.printStackTrace();
                        }
                    }

                });
                this.add(download[i]);
            }

            // Label for the name of program
            JLabel name = new JLabel("Print Screen Clone", JLabel.CENTER);
            name.setFont(new Font("Courier New", Font.BOLD, 25));
            name.setBounds(125, 100, 500, 100);
            name.setForeground(Color.BLUE);
            add(name);
            this.repaint();
        }

    }

}
