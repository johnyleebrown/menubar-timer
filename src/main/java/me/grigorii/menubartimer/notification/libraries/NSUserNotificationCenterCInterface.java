package me.grigorii.menubartimer.notification.libraries;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Pointer;

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
public interface NSUserNotificationCenterCInterface extends Library {
	void NSUserNotificationCenterScheduleNotification(Pointer paramPointer);

	int NSUserNotificationCenterGetDeliveredNotificationsCount();

	Pointer NSUserNotificationCenterGetDeliveredNotification(int paramInt);

	void NSUserNotificationCenterRemoveScheduledNotification(Pointer paramPointer);

	void NSUserNotificationCenterDeliverNotification(Pointer paramPointer);

	int NSUserNotificationCenterGetScheduledNotificationsCount();

	Pointer NSUserNotificationCenterGetScheduledNotification(int paramInt);

	void NSUserNotificationCenterRemoveDeliveredNotification(Pointer paramPointer);

	void NSUserNotificationCenterRemoveAllDeliveredNotifications();

	void NSUserNotificationCenterSetDefaultDelegate();

	void NSUserNotificationCenterSetDelegate(Callback paramCallback1, Callback paramCallback2, Callback paramCallback3);
}