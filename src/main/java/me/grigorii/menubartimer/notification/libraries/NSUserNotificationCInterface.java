package me.grigorii.menubartimer.notification.libraries;

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
public interface NSUserNotificationCInterface extends Library {

	Pointer NSUserNotificationAllocInit();

	void NSUserNotificationSetTitle(Pointer paramPointer1, Pointer paramPointer2);

	Pointer NSUserNotificationGetTitle(Pointer paramPointer);

	void NSUserNotificationSetSubtitle(Pointer paramPointer1, Pointer paramPointer2);

	Pointer NSUserNotificationGetSubtitle(Pointer paramPointer);

	void NSUserNotificationSetInformativeText(Pointer paramPointer1, Pointer paramPointer2);

	Pointer NSUserNotificationGetInformativeText(Pointer paramPointer);

	void NSUserNotificationSetHasActionButton(Pointer paramPointer, byte paramByte);

	byte NSUserNotificationHasActionButton(Pointer paramPointer);

	void NSUserNotificationSetActionButtonTitle(Pointer paramPointer1, Pointer paramPointer2);

	Pointer NSUserNotificationGetActionButtonTitle(Pointer paramPointer);

	void NSUserNotificationSetOtherButtonTitle(Pointer paramPointer1, Pointer paramPointer2);

	Pointer NSUserNotificationGetOtherButtonTitle(Pointer paramPointer);

	void NSUserNotificationSetDeliveryDate(Pointer paramPointer1, Pointer paramPointer2);

	Pointer NSUserNotificationGetDeliveryDate(Pointer paramPointer);

	Pointer NSUserNotificationGetActualDeliveryDate(Pointer paramPointer);

	void NSUserNotificationSetDeliveryRepeatInterval(Pointer paramPointer, int paramInt);

	int NSUserNotificationGetDeliveryRepeatInterval(Pointer paramPointer);

	void NSUserNotificationSetDeliveryTimeZone(Pointer paramPointer1, Pointer paramPointer2);

	Pointer NSUserNotificationGetDeliveryTimeZone(Pointer paramPointer);

	byte NSUserNotificationIsPresented(Pointer paramPointer);

	byte NSUserNotificationIsRemote(Pointer paramPointer);

	Pointer NSUserNotificationGetSoundName(Pointer paramPointer);

	void NSUserNotificationSetSoundName(Pointer paramPointer1, Pointer paramPointer2);

	void NSUserNotificationSetDefaultSoundName(Pointer paramPointer);

	byte NSUserNotificationGetActivationType(Pointer paramPointer);

	void NSUserNotificationRelease(Pointer paramPointer);
}
