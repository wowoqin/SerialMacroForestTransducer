package com.rules;

import com.XPath.PathParser.ASTPath;

import java.util.*;

/**
 * Created by qin on 2015/10/10.
 */
public class StateT1_8 extends StateT1{
    protected  State  _q3;//检查 preds
    protected  State  _q1;//检查后续 path


    protected StateT1_8(ASTPath path,State q3,State q1){
        super(path);
        _q3=q3;
        _q1=q1;
        this._predstack = new Stack();
        this._pathstack=new Stack();
    }

    public static State TranslateState(ASTPath path){//重新创建T1-8
        State q3=StateT3.TranslateStateT3(path.getFirstStep().getPreds());
        State q1=StateT1.TranslateStateT1(path.getRemainderPath());
        return new StateT1_8(path,q3,q1);//然后压入栈
    }

    public List startElementDo(String tag,int layer,Stack currstack) throws CloneNotSupportedException {
        if ((layer >= getLevel()) && (tag .equals(_test))) {///当前层数大于等于应该匹配的层数 getLayer（）就可以
            outlist.add(new ArrayList());
            State currQ;

            //pred栈
            if(this._predstack.isEmpty()) {
                _q3.setLevel(layer + 1);
                list.add(this._predstack);
                this._predstack.push(_q3);
            }
            else{
                this._predstack.push(_q3.copy());
                currQ=(State)this._predstack.peek();
                currQ.setLevel(layer+1);
            }

            //path栈
            if(this._pathstack.isEmpty()){
                list.add(this._pathstack);//在队列中添加一个 stack
                _q1.setLevel(layer + 1);
                this._pathstack.push(_q1);// q''(x1)
            }
            else{
                this._pathstack.push(_q1.copy());
                currQ=(State)this._pathstack.peek();
                currQ.setLevel(layer+1);
            }
        }
        return list;
    }

    public List endElementDo(String tag,int layer,Stack currstack){
        if(tag.equals(_test)){//收到自己的结束标签
         //判断 preds 检查的结果，然后相应的该删除的删除，该保留的保留
            State currQ=(State)this._predstack.peek();
            Object object=outlist.get(outlist.size()-1);
            if(currQ instanceof TWaitState||currQ instanceof FWaitState
                                            ||currQ instanceof  StateT3_2 || currQ instanceof  StateT3_4){
                currQ.endElementDo(tag, layer, this._predstack);
                currQ=(State)this._predstack.peek();
                if(!(currQ instanceof TState)){//推导之后谓词栈检查失败
                    outlist.remove(object);//删除最后一个 list
                    this._predstack.pop();
                }
                else{//推导之后谓词栈检查成功
                    this._predstack.pop();
                    if(((List)object).isEmpty())
                        outlist.remove(object);//删除最后一个 list
                }
            }
            else if((currQ instanceof TState)){//谓词本来就检查成功
                this._predstack.pop();
                this._pathstack.pop();
                if(((List)object).isEmpty())
                    outlist.remove(outlist.size() - 1);//删除最后一个 list
                if((!this._predstack.empty())){
                    currQ=(State)this._predstack.peek();
                    if(( currQ instanceof StateT2_3||currQ instanceof StateT2_4
                                    || currQ instanceof StateT3_3||currQ instanceof StateT3_4)){
                        TState trueQ=new TState(true);
                        trueQ.setLevel(currQ.getLevel());
                        this._predstack.pop();
                        this._predstack.push(trueQ);
                    }
                }
            }
            else{//谓词本来就检查失败
                outlist.remove(object);//删除最后一个 list
                this._predstack.pop();
                this._pathstack.pop();
            }
        }
        else  if(layer==getLevel()-1){//遇到上层结束标签
            currstack.pop();
            if(currstack.isEmpty())
                list.remove(this._pathstack);
        }
        return list;
    }

    public List checkOthers(String tag, int layer,Stack currstack) {
        this.endElementDo(tag, layer, currstack);
        if (!outlist.isEmpty() && (outlist.size() == (this._pathstack.size()+1))) {//这里也只能输出最后一个满足的子树
            Object object=outlist.get(outlist.size()-1);
            System.out.println(object);
            if(!this._pathstack.empty()){
                if( this._pathstack.peek() instanceof StateT1_3|| this._pathstack.peek() instanceof StateT1_4
                            ||this._pathstack.peek() instanceof StateT1_7|| this._pathstack.peek() instanceof StateT1_8){
                    //List curr_list=((List)(outlist.get(outlist.size() - 2)));
                    //将最后一个 list中的符合条件的标签 copy 到之前所有的已经查到上层标签的输出列表中
                    for(int i=0;i<((List)(object)).size();i++)
                        ((List)(outlist.get(outlist.size()-2))).add(((List)(object)).get(i));
                    }
                }
                outlist.remove(outlist.size() - 1);
            }
        return list;
    }

    public State copy() throws CloneNotSupportedException {
        return (State)this.clone();
    }
}
