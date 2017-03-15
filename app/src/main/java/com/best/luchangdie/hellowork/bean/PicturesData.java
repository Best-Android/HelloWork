package com.best.luchangdie.hellowork.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 */

public class PicturesData {
    private boolean error;
    private List<Results> results;

    public PicturesData() {
    }

    public PicturesData(boolean error, List<Results> results) {
        this.error = error;
        this.results = results;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "PicturesData{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
