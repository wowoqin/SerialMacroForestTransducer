package com.rules;

import com.XPath.PathParser.ASTPath;

import java.util.*;

/**
 * Created by qin on 2015/10/10.
 */
public class StateT1_7 extends StateT1 implements Cloneable{
    protected State _q1;//检查后续 path

    protected StateT1_7(ASTPath path, State q1) {
        super(path);
        _q1 = q1;
        this._pathstack=new Stack();
    }

    public static State TranslateState(ASTPath path) {//重新创建T1-7
        State q1 = StateT1.TranslateStateT1(path.getRemainderPath());
        return new StateT1_7(path, q1);
    }

    public List startElementDo(String tag, int layer,Stack currstack) throws CloneNotSupportedException{
        if ((layer >= getLevel()) && (tag.equals(_test)))  {//当前层数大于等于应该匹配的层数 getLayer（）就可以

            outlist.add(new ArrayList());

            if(this._pathstack.isEmpty()){
                list.add(this._pathstack);//在队列中添加一个 stack
                _q1.setLevel(layer + 1);
                this._pathstack.push(_q1);// q''(x1)
            }
            else{
                this._pathstack.push(_q1.copy());
                State currQ=(State)this._pathstack.peek();
                currQ.setLevel(layer+1);
            }
        }
        return list;
    }

    public List endElementDo(String tag,int layer,Stack currstack){
        if(tag.equals(_test)) {//收到自己的结束标签，也就是 path 的上层结束标签
            // 判断 path 检查的结果，该删除的删除，该保留的保留
            if(!this._pathstack.isEmpty()){
                State currQ=(State)this._pathstack.peek();
                currQ.endElementDo(tag, layer, this._pathstack);
            }
        }
        else if(layer==getLevel()-1) {//遇到上层结束标签
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
            if(((List) object).isEmpty()){
                outlist.remove(object);
                return list;
            }
            else{
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
        }
        return list;
    }

    public State copy() throws CloneNotSupportedException {
        return (State)this.clone();
    }}
