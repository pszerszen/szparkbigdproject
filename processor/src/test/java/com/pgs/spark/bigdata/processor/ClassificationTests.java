package com.pgs.spark.bigdata.processor;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.pgs.spark.bigdata.processor.domain.Classification;

public class ClassificationTests {

    private static final double UNDER_RANGE = Classification.getMinimum().getSparkLabel() - 1.0;

    private static final double ABOVE_RANGE = Classification.getMaximum().getSparkLabel() + 1.0;

    @Test
    public void testLinearClassification() {
        // test negativ
        for (double d = 0.00; d <= 0.50; d += 0.00001) {
            assertEquals("Wrong classification for label: " + d, Classification.NEGATIVE, Classification.fromLabelLinear(d));
        }
        // test neutral
        for (double d = 0.51; d <= 1.5; d += 0.00001) {
            assertEquals("Wrong classification for label: " + d, Classification.NEUTRAL, Classification.fromLabelLinear(d));
        }
        // test positiv
        for (double d = 1.51; d <= 2.0; d += 0.00001) {
            assertEquals("Wrong classification for label: " + d, Classification.POSITIVE, Classification.fromLabelLinear(d));
        }

        assertEquals("Wrong classification for label: " + UNDER_RANGE, Classification.NEGATIVE, Classification.fromLabelLinear(UNDER_RANGE));

        assertEquals("Wrong classification for label: " + ABOVE_RANGE, Classification.POSITIVE, Classification.fromLabelLinear(ABOVE_RANGE));
    }
}
