package com.github.cosysoft.device.android;


public interface AndroidApp {

	/**
	 * get the package name of apk.
	 * @return the package name
	 */
	String getBasePackage();

	String getMainActivity();

	void setMainActivity(String mainActivity);

	String getVersionName();

	void deleteFileFromWithinApk(String file);

	String getAppId();

	/**
	 * For testing only
	 */
	String getAbsolutePath();
}
