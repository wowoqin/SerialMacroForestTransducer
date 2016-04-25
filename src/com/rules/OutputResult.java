package com.rules;

import java.util.*;

/**
 * Created by toy on 15-9-12.
 */
public class OutputResult {
    protected int _layer;
    protected String _qName;
    protected String _content;

    protected OutputResult(int layer, String qName, String content){
        _layer = layer;
        _qName = qName;
        _content = content;
    }

    protected OutputResult(){
        _layer = -1;
        _qName = null;
        _content = null;
    }

    protected OutputResult(int layer, String qName){
        _layer = layer;
        _qName = qName;
    }

    public int getLayer(){
        return _layer;
    }

    public String getqName(){
        return _qName;
    }

    public void setLayer(int layer){
        _layer = layer;
    }

    public void setqName(String qName){
        _qName = qName;
    }

    public void setContent(String content){
        _content = content;
    }

    public String getContent(){
        return _content;
    }

    public String toString(){
        return String.format("Layer: %d,\nqName: %s,\ncontent: %s\n", _layer, _qName, _content);
    }

    public static Stack createResultList(){
        return new Stack();
    }

    public static OutputResult createResult(){
        return new OutputResult();
    }

    public void initialize(int layer, String qName, String content){
        this.setLayer(layer);
        this.setqName(qName);
        this.setContent(content);
    }

    public static OutputResult createResult(int layer, String qName){
        return new OutputResult(layer, qName);
    }

    public static void setInfo(OutputResult result, String content){
        result.setContent(content);
    }
}
