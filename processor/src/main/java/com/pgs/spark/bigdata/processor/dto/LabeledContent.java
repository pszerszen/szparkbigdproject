package com.pgs.spark.bigdata.processor.dto;

public class LabeledContent extends Content {
    private static final long serialVersionUID = -1318982927614247167L;

    private double label;

    public LabeledContent() {
    }

    public LabeledContent(final long id, final String text, final double label) {
        super(id, text);
        this.label = label;
    }

    public double getLabel() {
        return label;
    }

    public void setLabel(final double label) {
        this.label = label;
    }
}
