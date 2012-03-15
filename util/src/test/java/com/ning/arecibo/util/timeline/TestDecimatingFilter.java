package com.ning.arecibo.util.timeline;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestDecimatingFilter {

    @Test(groups="fast")
    public void testBasicFilterOperations() throws Exception {
        final List<Double> outputs = new ArrayList<Double>();
        final long millisStart = System.currentTimeMillis() - 2000 * 100;

        final DecimatingSampleFilter filter = new DecimatingSampleFilter(new DateTime(millisStart), new DateTime(millisStart + 2000 * 100), 25, 100,
                new SampleConsumer() {

                    @Override
                    public void consumeSample(int sampleNumber, SampleOpcode opcode, Object value, DateTime time) {
                        outputs.add((double)((Double)value));
                    }
                });
        for (int i=0; i<100; i++) {
            // Make the value go up for 4 samples; then down for 4 samples, between 10.0 and 40.0
            final int index = (i % 8) + 1;
            double value = 0;
            if (index <= 4) {
                value = 10.0 * index;
            }
            else {
                value = (8 - (index - 1)) * 10;
            }
            //System.out.printf("For i %d, index %d, adding value %f\n", i, index, value);
            filter.processOneSample(new DateTime(millisStart + 2000 * i), i+1, SampleOpcode.DOUBLE, value);
        }
        int index = 0;
        for (Double value : outputs) {
            //System.out.printf("index %d, value %f\n", index++, (double)((Double)value));
            if ((index & 1) == 0) {
                Assert.assertEquals(value, 40.0);
            }
            else {
                Assert.assertEquals(value, 10.0);
            }
            index++;
        }
    }

    /**
     * This test has sample count of 21, and output count of 6, so there are 5.8 samples per output point
     * @throws Exception
     */
    @Test(groups="fast")
    public void testFilterWithNonAlignedSampleCounts() throws Exception {
        final List<Double> outputs = new ArrayList<Double>();
        final long millisStart = System.currentTimeMillis() - 2000 * 21;

        final DecimatingSampleFilter filter = new DecimatingSampleFilter(new DateTime(millisStart), new DateTime(millisStart + 2000 * 21), 6, 21,
                new SampleConsumer() {

                    @Override
                    public void consumeSample(int sampleNumber, SampleOpcode opcode, Object value, DateTime time) {
                        outputs.add((double)((Double)value));
                    }
                });
        for (int i=0; i<21; i++) {
            // Make the value go up for 6 samples; then down for 6 samples, between 10.0 and 60.0
            final int index = (i % 6) + 1;
            double value = 0;
            if (index <= 3) {
                value = 10.0 * index;
            }
            else {
                value = (6 - (index - 1)) * 10;
            }
            System.out.printf("For i %d, index %d, adding value %f\n", i, index, value);
            filter.processOneSample(new DateTime(millisStart + 2000 * i), i+1, SampleOpcode.DOUBLE, value);
        }
        Assert.assertEquals(outputs.size(), 5);
        final double[] expectedValues = new double[] { 30.0, 20.0, 30.0, 30.0, 10.0 };
        for (int i=0; i<5; i++) {
            final double value = outputs.get(i);
            final double expectedValue = expectedValues[i];
            System.out.printf("index %d, value returned %f, value expected %f\n", i, value, expectedValue);
            Assert.assertEquals(value, expectedValue);
        }
    }
}
