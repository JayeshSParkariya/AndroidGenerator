package test.jsp.org.testgenerator;


import org.jsp.generator.Generator;

/**
 * Created by ANDROID-10 on 04-08-2016.
 */
public class TestGenerator {


    public static void main(String s[]) {

        try {
            Generator.generateForV4Fragment(MyFragment.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}