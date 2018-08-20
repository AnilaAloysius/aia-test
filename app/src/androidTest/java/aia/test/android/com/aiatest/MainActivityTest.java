package aia.test.android.com.aiatest;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import aia.test.android.com.aiatest.concrete.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);
    @Test
    public void testSearch_EmptyItem() {
        String searchItem = "";
        typeTextAndClickSearchButton(searchItem);
        onView(withId(R.id.inputlayout_edit_search))
                .check(matches(withError("Please enter a search query")));
    }
    @Test
    public void testSearch_Item() {
        String searchItem = "cat";
        typeTextAndClickSearchButton(searchItem);
        onView(withText(any(String.class))).inRoot(RootMatchers
                .withDecorView(not(is(mActivityRule.getActivity()
                        .getWindow()
                        .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    private static TypeSafeMatcher<View> withError(final String expected) {
        return new TypeSafeMatcher<View>() {

            @Override
            protected boolean matchesSafely(View item) {
                return item instanceof TextInputLayout && Objects.equals(((TextInputLayout) item).getError(), expected);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Not found error message [" + expected + "]");
            }
        };
    }

    private void typeTextAndClickSearchButton(String searchQuery) {
        onView(withId(R.id.edit_search))
                .perform(typeText(searchQuery));
        onView(withId(R.id.icon_search))
                .perform(click());


    }
}
