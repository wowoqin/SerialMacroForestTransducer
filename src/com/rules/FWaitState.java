package com.rules;

import java.util.List;
import java.util.Stack;

/**
 * Created by qin on 2015/11/24.
 * 匹配失败时 q3-qfwait，检查singlepred 和 predstack
 */
public class FWaitState extends State{

    protected Stack _predstack;
    protected Stack _singlestack;
    public FWaitState(){
        _predstack=new Stack();
        list.add(_predstack);
        _singlestack=new Stack();
        list.add(_singlestack);
    }
    public String getNodeTest() {//得到当前 XPath 的测试节点
        return "";
    }


    @Override
    public List startElementDo(String tag, int layer, Stack currstack) {//waitQ  在currstack的栈顶
        return  list;
    }
    @Override
    public List endElementDo(String tag,int layer,Stack currstack){
        if(layer == getLevel()-1){
            if(this._singlestack.peek() instanceof TState){//singlestack 是检查成功的
                State currQ=(State)this._predstack.peek();
                if(currQ instanceof TState){//predstack 是检查成功的
                    this._predstack.pop();
                    this._singlestack.pop();
                    TState trueQ=new TState(true);
                    trueQ.setLevel(this.getLevel());
                    currstack.pop();
                    currstack.push(trueQ);
                    ((State)currstack.peek()).endElementDo(tag, layer, currstack);
                }
                else if(!this._predstack.empty()) {//predstack 是 TWaitState || FWaitState
                    if (currQ instanceof TWaitState || currQ instanceof FWaitState
                        ||currQ instanceof  StateT3_2 || currQ instanceof  StateT3_4) {
                        currQ.endElementDo(tag, layer, this._predstack);
                        return ((State) currstack.peek()).endElementDo(tag, layer, currstack);
                    }
                    else{
                        this._predstack.pop();
                        this._singlestack.pop();
                        currstack.pop();
                        currQ=(State)currstack.peek();
                        Object object=outlist.get(outlist.size()-1);
                        if(object instanceof  List){
                            if(currQ instanceof StateT1_2)
                                ((List) object).remove(((List) object).size() - 1);
                            else if(currQ instanceof StateT1_6)
                                ((List)object).clear();
                        }
                        else{
                            if(currQ instanceof StateT1_2)
                                outlist.remove(outlist.size() - 1);
                            else if(currQ instanceof StateT1_6)
                                outlist.clear();
                        }
                    }
                }

            }
            else{// singlestack 检查失败
                this._predstack.pop();
                this._singlestack.pop();
                currstack.pop();
                State currQ=(State)currstack.peek();
                Object object=outlist.get(outlist.size()-1);
                if(object instanceof  List){
                    if(currQ instanceof StateT1_2)
                        ((List) object).remove(((List) object).size() - 1);
                    else if(currQ instanceof StateT1_6)
                        ((List)object).clear();
                }
                else{
                    if(currQ instanceof StateT1_2)
                        outlist.remove(outlist.size() - 1);
                    else if(currQ instanceof StateT1_6)
                        outlist.clear();
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
        return new FWaitState();
    }
}
