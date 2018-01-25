package me.grigorii.menubartimer.notification;

import java.util.concurrent.TimeUnit;

/**
 * This file is part of Menu bar Timer.
 *
 * Menu bar Timer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Menu bar Timer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Menu bar Timer. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Created by github.com/johnyleebrown
 *
 */
public class NotificationFactory {

	public static void showNotification(String subtitle) {
		BasicNotification notification = getNotification("Timer", subtitle, "");
		notification.show(1000, TimeUnit.MILLISECONDS);
	}

	public static void showNotification(String title, String subtitle, String message, long duration) {
		BasicNotification notification = getNotification(title, subtitle, message);
		notification.show(duration, TimeUnit.MILLISECONDS);
	}

	public static BasicNotification getNotification(String title, String subtitle, String message) {
		BasicNotification notification = new Notification();
		notification.setTitle(title);
		notification.setMessage(message);
		notification.setSubtitle(subtitle);
		notification.setSoundName("Glass");
		return notification;
	}
}
