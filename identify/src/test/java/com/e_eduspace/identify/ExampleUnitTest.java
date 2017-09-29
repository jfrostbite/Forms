package com.e_eduspace.identify;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);
//        digitTest();
//        elementTest();
//        dirtest();

        /*ArrayList<String> strings = new ArrayList<>();
        List<String> strings2 = new ArrayList<>();
        strings.add("132");
        Collections.copy(strings2,strings);
        System.out.println(strings2.size());*/

//        new Zi().fuTest();
        System.out.println(20 % 10);
    }

    private void action(int finalI) {
        for (int i = 0; i < 100; i++) {
            System.out.println(finalI);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void digitTest() {
        System.out.println("2 /6=="+2 /6 );
        System.out.println("2 %6=="+2 %6 );
        System.out.println("6 /6=="+6 /6 );
        System.out.println("6 %6=="+6 %6 );
        System.out.println("7 /6=="+7 /6 );
        System.out.println("7 %6=="+7 %6 );
        System.out.println("8 /6=="+8 /6 );
        System.out.println("8 %6=="+8 %6 );
    }

    private void elementTest() {
        for (int i = 0; i < IDenConstants.AREA_ONE_ELEMENT.length; i++) {
            String[] strs = IDenConstants.AREA_ONE_ELEMENT[i];
            System.out.println((strs.length == IDenConstants.ONE_AREA_SIZE[i]) + "--" + strs.length);
        }
    }

    private void dirtest() throws IOException {
        String dir = System.getProperty("user.dir");

        File file = new File(dir,"child");
        File file1 = new File(file, "db.db");
        boolean mkdirs = file1.createNewFile();
        System.out.println(mkdirs);
        System.out.println(file1.getAbsolutePath());
    }

    class Fu {
        public void test() {
            System.out.println("父类运行");
        }
    }

    class Zi extends Fu{
        public void test() {
            super.test();
            System.out.println("子类运行");
        }

        public void fuTest() {
            super.test();
        }
    }
}