import java.awt.*;

/**
 * Created by Greg on 12/25/17.
 */
public class SystemTrayIcon {
    static public PopupMenu popup = new PopupMenu();
    static TrayIcon trayIcon = null;

    public boolean initSystemTray() {

        SystemTray tray = SystemTray.getSystemTray();
        Image image;
        image = Toolkit.getDefaultToolkit().getImage(SystemTrayIcon.class.getResource("/icon.png"));

        MenuItem aboutItem = new MenuItem("About this app");
        popup.add(aboutItem);

        trayIcon = new TrayIcon(image.getScaledInstance(18, 12, 10), "Timer", popup);
        trayIcon.setToolTip("The best macOS menubar timer!");

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println(e);
        }

        return true;
    }
}
