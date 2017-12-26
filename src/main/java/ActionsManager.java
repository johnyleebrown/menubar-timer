import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Greg on 12/26/17.
 */
public class ActionsManager {

    SystemTray tray = SystemTray.getSystemTray();
    private Timer timer;

    ActionListener startListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (timer != null) timer.cancel();

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            final String strDate = simpleDateFormat.format(calendar.getTime());
            System.out.println(strDate);

            timer = new Timer();
            MyTimerTask timerTask = new MyTimerTask();
            timer.schedule(timerTask, 5000);
        }
    };

    ActionListener cancelListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (timer != null) {
                timer.cancel();
                timer = null;
                System.out.println("Timer cancelled");
            }
        }
    };

    ActionListener quitListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            tray.remove(SystemTrayIcon.trayIcon);
            System.exit(0);
        }
    };

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            final String strDate = simpleDateFormat.format(calendar.getTime());
            System.out.println(strDate);
            timer = null;
        }
    }
}
