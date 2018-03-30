package com.pgs.spark.bigdata.processor.dto;

import java.io.Serializable;

public class Content implements Serializable {

    private static final long serialVersionUID = 6301485118661321762L;

    private long id;

    private String text;

    public Content() {
    }

    public Content(final long id, final String text) {
        this.id = id;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

}
