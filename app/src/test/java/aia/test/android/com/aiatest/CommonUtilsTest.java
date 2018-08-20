package aia.test.android.com.aiatest;

import org.junit.Test;

import util.CommonUtils;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CommonUtilsTest {

    CommonUtils commonUtils = new CommonUtils();
    @Test()
    public void isEvenOrNot() {

        int score = 2;
        int points = 3;
        int topic_id = 5;
        boolean result = commonUtils.isEvenOrNot(score, points, topic_id);
        assertEquals(result, true);
    }
}