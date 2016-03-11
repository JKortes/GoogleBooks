package net.josecortes.com.googlebooksapp;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.After;
import org.junit.Before;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static net.josecortes.com.googlebooksapp.TestUtils.withRecyclerView;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class SearchFeatureTest extends ActivityInstrumentationTestCase2<BookListActivity> {


    private BookListActivity mActivity;
    private IdlingResource mIdlingResource;

    public SearchFeatureTest() {
        super(BookListActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        mIdlingResource = new SearchIdlingResource(getActivity());
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @After
    public void unregisterIntentServiceIdlingResource() {
        Espresso.unregisterIdlingResources(mIdlingResource);
    }


    public void testSearch_booksFound() {
        onView(withId(R.id.edit_query))
                .perform(typeText("lord of the rings"), closeSoftKeyboard());
        onView(withId(R.id.btn_search)).perform(click());
        onView(withRecyclerView(R.id.book_list)
                .atPositionOnView(0, R.id.tvAuthor))
                .check(matches(withText("J. R. R. Tolkien")));
    }


    public void testSearch_booksNotFound() {
        onView(withId(R.id.edit_query))
                .perform(typeText("test123451234512345"), closeSoftKeyboard());
        onView(withId(R.id.btn_search)).perform(click());

        RecyclerView list = (RecyclerView) mActivity.findViewById(R.id.book_list);
        assertEquals(0, list.getAdapter().getItemCount());
    }

    // Add more useful test cases

}



