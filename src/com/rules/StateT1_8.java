package com.rules;

import com.XPath.PathParser.ASTPath;

import java.util.*;

/**
 * Created by qin on 2015/10/10.
 */
public class StateT1_8 extends StateT1{
    protected  State  _q3;//��� preds
    protected  State  _q1;//������ path


    protected StateT1_8(ASTPath path,State q3,State q1){
        super(path);
        _q3=q3;
        _q1=q1;
        this._predstack = new Stack();
        this._pathstack=new Stack();
    }

    public static State TranslateState(ASTPath path){//���´���T1-8
        State q3=StateT3.TranslateStateT3(path.getFirstStep().getPreds());
        State q1=StateT1.TranslateStateT1(path.getRemainderPath());
        return new StateT1_8(path,q3,q1);//Ȼ��ѹ��ջ
    }

    public List startElementDo(String tag,int layer,Stack currstack) throws CloneNotSupportedException {
        if ((layer >= getLevel()) && (tag .equals(_test))) {///��ǰ�������ڵ���Ӧ��ƥ��Ĳ��� getLayer�����Ϳ���
            outlist.add(new ArrayList());
            State currQ;

            //predջ
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

            //pathջ
            if(this._pathstack.isEmpty()){
                list.add(this._pathstack);//�ڶ��������һ�� stack
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
        if(tag.equals(_test)){//�յ��Լ��Ľ�����ǩ
         //�ж� preds ���Ľ����Ȼ����Ӧ�ĸ�ɾ����ɾ�����ñ����ı���
            State currQ=(State)this._predstack.peek();
            Object object=outlist.get(outlist.size()-1);
            if(currQ instanceof TWaitState||currQ instanceof FWaitState
                                            ||currQ instanceof  StateT3_2 || currQ instanceof  StateT3_4){
                currQ.endElementDo(tag, layer, this._predstack);
                currQ=(State)this._predstack.peek();
                if(!(currQ instanceof TState)){//�Ƶ�֮��ν��ջ���ʧ��
                    outlist.remove(object);//ɾ�����һ�� list
                    this._predstack.pop();
                }
                else{//�Ƶ�֮��ν��ջ���ɹ�
                    this._predstack.pop();
                    if(((List)object).isEmpty())
                        outlist.remove(object);//ɾ�����һ�� list
                }
            }
            else if((currQ instanceof TState)){//ν�ʱ����ͼ��ɹ�
                this._predstack.pop();
                this._pathstack.pop();
                if(((List)object).isEmpty())
                    outlist.remove(outlist.size() - 1);//ɾ�����һ�� list
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
            else{//ν�ʱ����ͼ��ʧ��
                outlist.remove(object);//ɾ�����һ�� list
                this._predstack.pop();
                this._pathstack.pop();
            }
        }
        else  if(layer==getLevel()-1){//�����ϲ������ǩ
            currstack.pop();
            if(currstack.isEmpty())
                list.remove(this._pathstack);
        }
        return list;
    }

    public List checkOthers(String tag, int layer,Stack currstack) {
        this.endElementDo(tag, layer, currstack);
        if (!outlist.isEmpty() && (outlist.size() == (this._pathstack.size()+1))) {//����Ҳֻ��������һ�����������
            Object object=outlist.get(outlist.size()-1);
            System.out.println(object);
            if(!this._pathstack.empty()){
                if( this._pathstack.peek() instanceof StateT1_3|| this._pathstack.peek() instanceof StateT1_4
                            ||this._pathstack.peek() instanceof StateT1_7|| this._pathstack.peek() instanceof StateT1_8){
                    //List curr_list=((List)(outlist.get(outlist.size() - 2)));
                    //�����һ�� list�еķ��������ı�ǩ copy ��֮ǰ���е��Ѿ��鵽�ϲ��ǩ������б���
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
