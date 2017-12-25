import java.awt.SystemTray;

/**
 * Created by Greg on 12/25/17.
 */
public class App {

    public static void main(String[] args) {
        System.setProperty("apple.awt.UIElement", "true");
        if (SystemTray.isSupported()) {
            SystemTrayIcon initApp = new SystemTrayIcon();
            initApp.initSystemTray();
        }
    }
}
