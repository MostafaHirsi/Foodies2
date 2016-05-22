package com.abstractcoders.mostafa.foodies;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.ListView;
import android.widget.SearchView;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by Mustafa
 */
public class SearchActivityTest extends ActivityInstrumentationTestCase2<SearchActivity> {

    public SearchActivityTest() {
        super(SearchActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    //asserts that search textbox query is empty
    @SmallTest
    public void testSearchTextBox()
    {
        SearchView searchView = (SearchView) getActivity().findViewById(R.id.search_query);
        assertEquals(searchView.getQuery(),"");
    }

    // asserts that a specific number of places are retrieved after 3 seconds
    @SmallTest
    public void testSearchFunction() throws InterruptedException {
        SearchView searchView = (SearchView) getActivity().findViewById(R.id.search_query);
        searchView.setQuery("cafe", true);
        Thread.sleep(3000);
        ListView listView = (ListView) getActivity().findViewById(R.id.listView);
        Assert.assertTrue(listView.getAdapter().getCount() > 0);
    }
}
