package com.rules;

import com.XPath.PathParser.ASTPreds;

import java.util.List;
import java.util.Stack;

/**
 * Created by qin on 2015/10/10.
 */
public class StateT3_1 extends StateT3{
    protected  State _q3;//检查preds'
    protected  State _q2;//检查【child::test preds】

    protected  StateT3_1(ASTPreds preds,State q3,State q2){
        super(preds);
        _q3=q3;
        _q2=q2;
    }

    public static StateT3 TranslateState(ASTPreds preds){//重新创建T3-1
        State q3=StateT3.TranslateStateT3(preds.getRemainderPreds());
        State q2=StateT2.TranslateStateT2(StateT3.getSinglePred(preds));
        return new StateT3_1(preds,q3,q2);
    }
    public List startElementDo(String tag,int layer,Stack currstack) throws CloneNotSupportedException {
        //进到 q3-1 中就应该对 preds' 同时进行检查
        if(getLevel() == layer) {//应该匹配的层数 getLayer（）和 当前标签 tag 的层数相等
            _q2.setLevel(getLevel() );//q2 检查【child::test preds】，应该匹配的标签的层数 不变
            _q3.setLevel(getLevel());// q3 检查preds'，应该匹配的标签的层数与当前 [test] 同一层
            if (tag .equals(_test)){
                TWaitState twaitQ=new TWaitState();
                twaitQ.setLevel(this.getLevel());
                currstack.pop();
                currstack.push(twaitQ);
                twaitQ._predstack.push(_q3);
                ((State)twaitQ._predstack.peek()).startElementDo(tag, layer, twaitQ._predstack);
            }
            else {
                FWaitState  fwaitQ=new FWaitState();
                fwaitQ.setLevel(this.getLevel());
                currstack.pop();
                currstack.push(fwaitQ);

                fwaitQ._singlestack.push(_q2);
                fwaitQ._predstack.push(_q3);
                ((State) fwaitQ._predstack.peek()).startElementDo(tag, layer, fwaitQ._predstack);
            }
        }
        return list;
    }

    //无论成败，q3-1 都被替换，所以 q3-1 不会遇到上层结束标签


    public State copy() throws CloneNotSupportedException {
        return (State)this.clone();
    }
}

