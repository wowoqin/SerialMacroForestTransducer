package com.rules;

import com.XPath.PathParser.ASTPreds;

import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by qin on 2015/10/25.
 * 匹配成功时 q3-qtwait，检查 predstack 即可
 */
public class TWaitState extends State{

    protected Stack _predstack;
    public TWaitState(){
        _predstack=new Stack();
        list.add(_predstack);
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
        if(layer == getLevel()-1){//遇到上层结束标签
            State currQ=(State)this._predstack.peek();
            if(currQ instanceof TState){//predstack 是检查成功的
                this._predstack.pop();
                TState trueQ=new TState(true);
                trueQ.setLevel(this.getLevel());
                currstack.pop();
                currstack.push(trueQ);
                ((State)currstack.peek()).endElementDo(tag, layer, currstack);
            }
            else{
                if(!this._predstack.empty()){//predstack 是 TWaitState || FWaitState
                    if(currQ instanceof TWaitState || currQ instanceof FWaitState
                                                    ||currQ instanceof  StateT3_2 || currQ instanceof  StateT3_4){
                        currQ.endElementDo(tag, layer, this._predstack);
                        return ((State)currstack.peek()).endElementDo(tag,layer,currstack);
                    }
                    else{//predstack 是 其它谓词自己--this._predstack 检查失败
                        this._predstack.pop();
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
//                else{//this._predstack.empty()，也就是检查失败
//
//                }
            }
        }
        return list;
    }

    @Override
    public List checkOthers(String tag, int layer, Stack currstack) {
        return list;
    }

    public State copy(){
        return new TWaitState();
    }
}
