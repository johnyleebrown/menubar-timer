package me.grigorii.menubartimer.notification.libraries;

import me.grigorii.menubartimer.notification.core.NSUserNotification;

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
public interface NSUserNotificationCenterDelegate {

	/**
	 * Sent to the delegate when a user clicks on a notification in the
	 * notification center. This would be a good time to take action in
	 * response to user interacting with a specific notification.
	 *
	 * @param paramNSUserNotification A notification that can be scheduled
	 *                                  for display in the notification center.
	 */
	void didActivateNotification(NSUserNotification paramNSUserNotification);

	/**
	 * Sent to the delegate when a notification delivery date has arrived.
	 * At this time, the notification has either been presented to the user
	 * or the notification center has decided not to present it because your
	 * application was already frontmost.
	 *
	 * @param paramNSUserNotification A notification that can be scheduled
	 *                                  for display in the notification center.
	 */
	void didDeliverNotification(NSUserNotification paramNSUserNotification);


	/**
	 * Sent to the delegate when the Notification Center has decided not to
	 * present your notification, for example when your application is front most.
	 * If you want the notification to be displayed anyway, return YES.
	 *
	 * @param paramNSUserNotification A notification that can be scheduled
	 *                                  for display in the notification center.
	 */
	byte shouldPresentNotification(NSUserNotification paramNSUserNotification);
}
