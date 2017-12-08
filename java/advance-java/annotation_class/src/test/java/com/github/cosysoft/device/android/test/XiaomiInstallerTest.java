package com.github.cosysoft.device.android.test;

import java.io.File;

import com.github.cosysoft.device.android.AndroidApp;
import com.github.cosysoft.device.android.AndroidDevice;
import com.github.cosysoft.device.android.impl.DefaultAndroidApp;
import com.github.cosysoft.device.android.xiaomi.MIDeviceUtility;
import org.junit.Test;

/**
 * 
 * @author ltyao
 *
 */
public class XiaomiInstallerTest extends AndroidDeviceTest {

	@Test
	public void testXiaomiInstall() {

		File ctrip = new File("E:\\work\\XiaoXiang_1.0.1_2017081790_class100.apk");
		AndroidApp app = new DefaultAndroidApp(ctrip);

		logger.info(app.getBasePackage());
		logger.info(app.getMainActivity());
		String activity = app.getMainActivity().replace(app.getBasePackage(),
				"");

		logger.info(activity);
		for (AndroidDevice device : getDevices()) {
			MIDeviceUtility.testInstall(device);
			MIDeviceUtility.install(device, app);
			// device.install(app);
		}
	}

	@Test
	public void testImageSub() {
		for (AndroidDevice device : getDevices()) {
			logger.info(device.currentActivity());
		}
	}
}
