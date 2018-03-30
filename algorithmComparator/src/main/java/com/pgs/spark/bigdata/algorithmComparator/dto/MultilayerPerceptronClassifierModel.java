package com.pgs.spark.bigdata.algorithmComparator.dto;

public class MultilayerPerceptronClassifierModel {

    private Integer featuresSize;

    private Integer outputClassesSize;

    private int[] hiddenLayers;

    public Integer getFeaturesSize() {
        return featuresSize;
    }

    public void setFeaturesSize(Integer featuresSize) {
        this.featuresSize = featuresSize;
    }

    public void setHiddenLayers(int... hiddenLayers) {
        this.hiddenLayers = hiddenLayers;
    }

    public int[] getHiddenLayers() {
        return hiddenLayers;
    }

    public Integer getOutputClassesSize() {
        return outputClassesSize;
    }

    public void setOutputClassesSize(Integer outputClassesSize) {
        this.outputClassesSize = outputClassesSize;
    }

    public int[] buildLayersArray() {
        final int[] result = new int[hiddenLayers.length + 2];
        //first element set to features size
        result[0] = featuresSize;
        //fill hidden layers
        System.arraycopy(hiddenLayers, 0, result, 1, hiddenLayers.length);
        //last element set to ouput classes size
        result[result.length-1] = outputClassesSize;
        return result;
    }

}
