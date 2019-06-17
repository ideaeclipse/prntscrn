package com.installer;

import com.minghao.HttpRequests;
import com.sun.jna.platform.win32.Advapi32Util;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import static com.sun.jna.platform.win32.WinReg.HKEY_CURRENT_USER;


public class InstallFrame extends JFrame {

    InstallFrame() throws IOException {
        super("Print Screen Clone Installer");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(750, 500);
        setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
        setResizable(false);
        this.add(new InstallPanel());
        this.setVisible(true);
    }

    private static class InstallPanel extends JPanel{

        InstallPanel() throws IOException {
            this.setLayout(null);

            // Getting versions from the api and storing them as a list
            List<String> version = new LinkedList<>();
            List<String> url = new LinkedList<>();
            JSONArray array = new JSONArray(new HttpRequests().getVerison("executable"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonobject = array.getJSONObject(i);
                version.add(jsonobject.getString("version"));
                url.add(jsonobject.getString("url"));
            }

            // JButtons
            JButton[] download = new JButton[version.size()];
            for(int i = 0; i < download.length; i++){
                download[i] = new JButton(version.get(i));
                download[i].setBounds(325, 225 + i * 50, 100, 35);
                String urlTemp = url.get(i);
                download[i].addActionListener(e -> {
                    try {
                        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                        jfc.setDialogTitle("Choose a directory to save your file: ");
                        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                        int returnValue = jfc.showSaveDialog(null);
                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            if (jfc.getSelectedFile().isDirectory()) {
                                BufferedInputStream in = new BufferedInputStream(new URL(urlTemp).openStream());
                                FileOutputStream fileOutputStream = new FileOutputStream(jfc.getSelectedFile() + "\\Test.mp3");
                                byte dataBuffer[] = new byte[1024];
                                int bytesRead;
                                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                                }
                                Advapi32Util.registrySetStringValue(HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "mayoIsGay", jfc.getSelectedFile() + "\\Test.mp3");

                            }
                        }

                    } catch (IOException e2){
                        JLabel Error = new JLabel("An error has occurred please contact the one and only mayo");
                        Error.setFont(new Font("Courier New", Font.BOLD, 25));
                        Error.setBounds(125,100, 500,100);
                        Error.setForeground(Color.BLUE);
                        add(Error);
                        this.repaint();
                    }

                });
                this.add(download[i]);
            }

            JLabel name = new JLabel("Print Screen Clone", JLabel.CENTER);
            name.setFont(new Font("Courier New", Font.BOLD, 25));
            name.setBounds(125,100, 500,100);
            name.setForeground(Color.BLUE);
            add(name);
            this.repaint();
        }

    }

}
