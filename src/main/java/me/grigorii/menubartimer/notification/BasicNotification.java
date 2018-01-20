package me.grigorii.menubartimer.notification;

import java.util.concurrent.TimeUnit;

/**
 * Created by github.com/johnyleebrown
 */
public interface BasicNotification<T> {

	void setTitle(String title);

	String getTitle();

	void setMessage(String message);

	String getMessage();

	void setSubtitle(String subtitle);

	void setSoundName(String name);

	void setDefaultSoundName();

	void enableActionButton();

	void show(long duration, TimeUnit unit);

	T getRoot();
}
