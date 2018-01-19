package me.grigorii.menubartimer.notification;

import java.util.concurrent.TimeUnit;

/**
 * Created by github.com/johnyleebrown
 */
public interface BasicNotification<T> {

	String getTitle();

	void setTitle(String title);

	String getMessage();

	void setMessage(String message);

	void show(long duration, TimeUnit unit);

	void setDefaultSoundName();

	void setSoundName(String name);

	void enableActionButton();

	T getRoot();
}
