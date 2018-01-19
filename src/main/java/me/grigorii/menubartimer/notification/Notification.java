package me.grigorii.menubartimer.notification;

import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import me.grigorii.menubartimer.notification.core.NSUserNotification;
import me.grigorii.menubartimer.notification.core.NSUserNotificationCenter;

/**
 * Created by github.com/johnyleebrown
 */
public class Notification implements BasicNotification<NSUserNotification> {

	private NSUserNotification notification;

	public Notification() {
		notification = new NSUserNotification();
	}

	@Override
	public void setDefaultSoundName() { notification.setDefaultSoundName(); }

	@Override
	public void setSoundName(String name) { notification.setSoundName(name); }

	@Override
	public void enableActionButton() { notification.enableActionButton(); }

	@Override
	public String getTitle() {
		return notification.getTitle();
	}

	@Override
	public void setTitle(String title) { notification.setTitle(title); }

	@Override
	public String getMessage() {
		return notification.getText();
	}

	@Override
	public void setMessage(String message) {
		notification.setText(message);
	}

	@Override
	public void show(long duration, TimeUnit unit) {
		Platform.runLater(() -> {
			try {
				NSUserNotificationCenter.getInstance().deliverNotification(notification);
				Thread.sleep(unit.toMillis(duration));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				notification.close();
			}
		});
	}

	@Override
	public NSUserNotification getRoot() {
		return notification;
	}
}
