package com.git.onedayrex.picocr.ui;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * created by onedayrex
 * 2018/2/24
 **/
public class MainUI extends JFrame{
    private static final long serialVersionUID = -4696251672407909524L;

    public static final int SCREEN_KEY = 1;

    public static final Logger logger = Logger.getLogger(MainUI.class);

    public MainUI() throws HeadlessException {
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("screenshot24.png"));
        final TrayIcon trayIcon = new TrayIcon(image);
        trayIcon.setPopupMenu(createMenu(trayIcon));
        try {
            SystemTray.getSystemTray().add(trayIcon);
            trayIcon.displayMessage("picocr","截图ocr已打开", TrayIcon.MessageType.INFO);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        this.setVisible(false);
    }

    private PopupMenu createMenu(final TrayIcon trayIcon) {
        PopupMenu popupMenu = new PopupMenu();
        MenuItem screenshot = new MenuItem("screenshot");
        MenuItem exit = new MenuItem("close");
        screenshot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScreenShotUI screenShotUI = new ScreenShotUI(trayIcon);
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        popupMenu.add(screenshot);
        popupMenu.add(exit);
        return popupMenu;
    }
}
