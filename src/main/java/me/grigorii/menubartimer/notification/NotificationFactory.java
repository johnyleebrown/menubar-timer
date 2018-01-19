package me.grigorii.menubartimer.notification;

import java.util.concurrent.TimeUnit;

/**
 * Created by github.com/johnyleebrown
 */
public class NotificationFactory {

	public static void showNotification(String title, String message, long duration) {
		BasicNotification notification = getNotification(title, message);
		notification.show(duration, TimeUnit.MILLISECONDS);
	}

	public static BasicNotification getNotification(String title, String message) {
		BasicNotification notification = new Notification();
		notification.setTitle(title);
		notification.setMessage(message);
		notification.setSoundName("Glass");
		return notification;
	}
}
