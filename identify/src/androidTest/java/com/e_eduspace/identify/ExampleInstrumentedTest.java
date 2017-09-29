package com.e_eduspace.identify;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.e_eduspace.identify.test", appContext.getPackageName());

        /*File databasePath = appContext.getDatabasePath(Constants.POINT_DB_NAME);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        FileInputStream fis = new FileInputStream(databasePath);

        byte[] bytes = new byte[1024];

        int len = 0;
        while ((len = fis.read(bytes)) > -1) {
            baos.write(bytes,0,len);
            baos.flush();
        }

        baos.close();
        byte[] bytes1 = baos.toByteArray();

        LogUtils.e("kevin", bytes1.length);*/
       /* IDentifyMulti iDentify = new IDentifyMulti.Builder().build(appContext);
        for (int i = 0; i < 102; i++) {
            PointEntity pointEntity = new PointEntity();
            pointEntity.x = 0;
            pointEntity.y = 1;
            iDentify.addPoint(pointEntity,true);
        }*/

    }
}
