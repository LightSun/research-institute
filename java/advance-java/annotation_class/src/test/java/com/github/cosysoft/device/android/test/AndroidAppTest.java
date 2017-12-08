package com.github.cosysoft.device.android.test;

import static org.junit.Assert.*;

import java.io.File;

import com.github.cosysoft.device.android.AndroidApp;
import com.github.cosysoft.device.android.impl.DefaultAndroidApp;
import org.junit.Test;

public class AndroidAppTest {

	@Test
	public void testExtract() {
		//XiaoXiang_1.0.1_2017081790_class100
		AndroidApp app = new DefaultAndroidApp(new File("E:\\work\\XiaoXiang_1.0.1_2017081790_class100.apk"));

		assertEquals("com.classroom100.android", app.getBasePackage());

	}

}
