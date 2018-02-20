package com.aaronmeaney.busstop;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 */
public class GoogleApiTest {

    /**
     * Tests to ensure that the API key is correct.
     * @throws Exception Any exception.
     */
    @Test
    public void testMapsApiKey() throws Exception {
        Context thisContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("AIzaSyCEYm-BtibsPt6uA7UNLtjcQDJYgDncs4I", thisContext.getResources().getString(R.string.google_maps_key));
    }
}
