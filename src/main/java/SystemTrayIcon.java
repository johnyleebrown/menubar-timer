import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;

import javax.swing.*;

/**
 * Created by Greg on 12/25/17.
 */
public class SystemTrayIcon {
    ActionsManager actions = new ActionsManager();
    static public PopupMenu popup = new PopupMenu();
    static TrayIcon trayIcon = null;

    public boolean initSystemTray() {

        SystemTray tray = SystemTray.getSystemTray();

        Image image = Toolkit.getDefaultToolkit().getImage(SystemTrayIcon.class.getResource("/icon.png"));

        MenuItem startItem = new MenuItem("Start Timer");
        startItem.addActionListener(actions.startListener);
        popup.add(startItem);

        MenuItem cancelItem = new MenuItem("Stop Timer");
        cancelItem.addActionListener(actions.cancelListener);
        popup.add(cancelItem);

        MenuItem exitItem = new MenuItem("Quit");
        exitItem.addActionListener(actions.quitListener);
        popup.add(exitItem);

        trayIcon = new TrayIcon(image.getScaledInstance(18, 12, 10), "Timer", popup);
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println(e);
        }

        return true;
    }

}
