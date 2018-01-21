package me.grigorii.menubartimer.notification;

import java.util.concurrent.TimeUnit;

import me.grigorii.menubartimer.notification.core.NSUserNotification;
import me.grigorii.menubartimer.notification.core.NSUserNotificationCenter;

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
public class Notification implements BasicNotification<NSUserNotification> {

	private NSUserNotification notification;

	public Notification() {
		notification = new NSUserNotification();
	}

	@Override
	public void setSubtitle(String subtitle) { notification.setSubtitle(subtitle); }

	@Override
	public void setSoundName(String name) { notification.setSoundName(name); }

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
		try {
			NSUserNotificationCenter.getInstance().deliverNotification(notification);
			Thread.sleep(unit.toMillis(duration));
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			notification.close();
		}
	}

	@Override
	public NSUserNotification getRoot() {
		return notification;
	}
}
