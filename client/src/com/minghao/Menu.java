package com.minghao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

class Menu extends JFrame {
    private TrayIcon trayIcon;
    private SystemTray tray;
    private final ErrorFrame errorFrame;

    Menu(final ErrorFrame errorFrame) {
        super("Menu System");
        this.errorFrame = errorFrame;
        if (SystemTray.isSupported()) {
            tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage("PrintScreen-Clone.png");
            ActionListener exitListener = (e -> {
                tray.remove(trayIcon);
                System.exit(0);
            });

            // Adding different option when clicked
            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Exit");
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);

            defaultItem = new MenuItem("Option");
            defaultItem.addActionListener(e -> {
                setVisible(true);
                setExtendedState(JFrame.NORMAL);
            });
            popup.add(defaultItem);

            trayIcon = new TrayIcon(image, "PrintScreen-Clone", popup);
            trayIcon.setImageAutoSize(true);

            // Setting the state when in tray and when not in tray
            addWindowStateListener(e -> {
                if (e.getNewState() == ICONIFIED) {
                    try {
                        tray.add(trayIcon);
                        setVisible(false);

                    } catch (AWTException ex) {
                        errorFrame.writeError("Unable to tray the menu system. Blame Ming for this and contact the one and only mayo", ex, this.getClass());
                    }
                }
                if (e.getNewState() == 7) {
                    try {
                        tray.add(trayIcon);
                        setVisible(false);
                    } catch (AWTException ex) {
                        System.out.println("unable to add to system tray");
                    }
                }
                if (e.getNewState() == MAXIMIZED_BOTH) {
                    tray.remove(trayIcon);
                    setVisible(true);
                }
                if (e.getNewState() == NORMAL) {
                    tray.remove(trayIcon);
                    setVisible(true);
                }
            });
            setIconImage(Toolkit.getDefaultToolkit().getImage("PrintScreen-Clone.png"));

            // Setting the in tray mode at starting
            try {
                tray.add(trayIcon);
                setVisible(false);
            } catch (AWTException ex) {
                System.out.println("unable to add to system tray");
            }

            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            setSize(500, 350);
            setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setResizable(false);

            this.add(new MenuPanel());
        }
    }

    private class MenuPanel extends JPanel{
        MenuPanel(){

        }

    }
}
