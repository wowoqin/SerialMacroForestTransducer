package com.rules;

import java.util.List;
import java.util.Stack;

/**
 * Created by qin on 2015/10/14.
 */
public class TState extends State{
    boolean _flg;
    public TState(boolean flg){
        _flg=flg;
    }


    public String getNodeTest() {//得到当前 XPath 的测试节点
        return "";
    }

    @Override
    public List startElementDo(String tag, int layer,Stack currstack) {
        return list;
    }

    @Override
    public List endElementDo(String tag,int layer,Stack currstack){
        if(layer == getLevel()-1){ //遇到上层结束标签
            if(currstack.size()>1){
                currstack.pop();
                State currQ=(State)currstack.peek();
                if(currQ instanceof StateT2){
                    TState trueQ=new TState(true);
                    trueQ.setLevel(layer);
                    currstack.pop();
                    currstack.push(trueQ);
                }
                else if(currQ instanceof StateT3){//q3-2 .q3-4
                    TWaitState twaitQ=new TWaitState();
                    twaitQ.setLevel(layer);
                    twaitQ._predstack=((StateT3)currQ)._predstack;
                    currstack.pop();
                    currstack.push(twaitQ);
                }
            }
        }
        return list;
    }

    @Override
    public List checkOthers(String tag, int layer, Stack currstack) {
        return list;
    }

    public State copy(){
        return new TState(_flg);
    }
}
