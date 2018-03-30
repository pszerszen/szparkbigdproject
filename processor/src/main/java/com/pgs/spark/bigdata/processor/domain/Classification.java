package com.pgs.spark.bigdata.processor.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Supplier;

import org.apache.commons.lang.math.DoubleRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Classification implements Serializable {
    POSITIVE(2.0),
    NEUTRAL(1.0),
    NEGATIVE(0.0);

    private static final Logger LOGGER = LoggerFactory.getLogger(Classification.class);

    private static Classification MIN;

    private static Classification MAX;

    private double sparkLabel;

    Classification(double sparkLabel) {
        this.sparkLabel = sparkLabel;
    }

    public double getSparkLabel() {
        return this.sparkLabel;
    }

    public static Classification fromLabelLogistic(double label) {
        return Arrays.stream(values())
                .filter(c -> c.sparkLabel == label)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("There is no label with number: " + label));
    }

    public static Classification fromLabelLinear(double label) {
        final Classification classification = Arrays.stream(values())
                .filter(c -> new DoubleRange(c.sparkLabel - 0.5, c.sparkLabel + 0.5).containsDouble(label))
                .findFirst()
                .orElseGet(handleExtremeSituation(label));
        LOGGER.info("The label {} classified as {}({})", String.format("%7.4f", label), classification, classification.getSparkLabel());
        return classification;
    }

    public static Classification getMaximum() {
        if (MAX == null) {
            //noinspection OptionalGetWithoutIsPresent
            MAX = Arrays.stream(values())
                    .max((c1, c2) -> Double.valueOf(c1.getSparkLabel()).compareTo(c2.getSparkLabel()))
                    .get();
        }
        return MAX;
    }

    public static Classification getMinimum() {
        if (MIN == null) {
            //noinspection OptionalGetWithoutIsPresent
            MIN = Arrays.stream(values())
                    .min((c1, c2) -> Double.valueOf(c1.getSparkLabel()).compareTo(c2.getSparkLabel()))
                    .get();
        }
        return MIN;
    }

    private static Supplier<Classification> handleExtremeSituation(double label) {
        return label > getMaximum().getSparkLabel() ? Classification::getMaximum : Classification::getMinimum;
    }

}
