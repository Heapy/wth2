package by.heap.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TODO: CommentMe!
 *
 * @author Ibragimov Ruslan
 */
public class GeoUtilsTest {

    @Test
    public void testDistanceBetweenPointsLessThen() throws Exception {
        assertFalse(GeoUtils.distanceBetweenPointsLessThen("53.9012", "27.5650", "53.9025", "27.5709", 0.01));
        assertTrue(GeoUtils.distanceBetweenPointsLessThen("53.9012", "27.5650", "53.9025", "27.5709", 0.5));
    }
}