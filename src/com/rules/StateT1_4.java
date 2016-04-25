package com.rules;

import com.XPath.PathParser.ASTPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

/**
 * Created by qin on 2015/10/10.
 */
public class StateT1_4 extends StateT1 implements Cloneable {
    protected State _q3;//检查 preds

    protected StateT1_4(ASTPath path, State q3) {
        super(path);
        _q3 = q3;
        this._predstack = new Stack();
    }

    public static State TranslateState(ASTPath path) {//重新创建T1-4
        State q3 = StateT3.TranslateStateT3(path.getFirstStep().getPreds());
        return new StateT1_4(path, q3);
    }

    public List startElementDo(String tag,int layer,Stack currstack) throws CloneNotSupportedException{
        State currQ;
        if ((layer >= getLevel()) && (tag .equals(_test))) {//当前层数大于等于应该匹配的层数 getLayer（）就可以
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

                if(outlist.isEmpty()){
                    outlist.add(_test);
                }
                else{
                    if(outlist.get(outlist.size()-1) instanceof List)
                        ((List)(outlist.get(outlist.size() - 1))).add(_test);
                    else
                        outlist.add(_test);
                }
        }
        return list;
    }

    @Override
    public List endElementDo(String tag,int layer,Stack currstack){
        if(tag.equals(_test)){//遇到自己的结束标签,判断谓词检查的结果，失败则删除，成功则保留
            //这里的结束标签对于谓词来说就是上层结束标签，所以在这里对谓词进行出栈操作
            if(!this._predstack.isEmpty()){
                State currQ=(State)this._predstack.peek();
                Object object=outlist.get(outlist.size()-1);
                if(currQ instanceof TWaitState||currQ instanceof FWaitState
                        ||currQ instanceof  StateT3_2 || currQ instanceof  StateT3_4){
                    currQ.endElementDo(tag, layer, this._predstack);
                    currQ=(State)this._predstack.peek();
                    if(!(currQ instanceof TState)){
                        if(object instanceof List)
                            ((List)(object)).remove(((List)(object)).size() - 1);//删除最后一个list中的最后一个符合的标签
                        else
                            outlist.remove(outlist.size() - 1);//删除最后一个
                        this._predstack.pop();
                    }
                    else  //推导之后谓词栈检查成功
                        this._predstack.pop();
                }
                else if((currQ instanceof TState)){//谓词本来就检查成功
                    this._predstack.pop();
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
                    if(object instanceof List){
                        ((List)(object)).remove(((List)(object)).size() - 1);
                    }
                    else
                        outlist.remove(outlist.size() - 1);//删除最后一个
                    this._predstack.pop();
                }
            }
        }
        else{
            if(layer==getLevel()-1){ //遇到上层结束标签
                currstack.pop();
                if(currstack.isEmpty())
                    list.remove(this._predstack);
            }
        }
        return list;
    }

    public List checkOthers(String tag, int layer,Stack currstack) {
        this.endElementDo(tag, layer, currstack);
        if(!outlist.isEmpty()&&(outlist.size()==(this._predstack.size()+1))){//这里也只能输出最后一个满足的子树
            System.out.println(outlist.get(outlist.size()-1));
            outlist.remove(outlist.size()-1);
        }
        return list;
    }

    public State copy() throws CloneNotSupportedException {
        return (State)this.clone();
    }

}
