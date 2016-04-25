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
    protected State _q3;//��� preds

    protected StateT1_4(ASTPath path, State q3) {
        super(path);
        _q3 = q3;
        this._predstack = new Stack();
    }

    public static State TranslateState(ASTPath path) {//���´���T1-4
        State q3 = StateT3.TranslateStateT3(path.getFirstStep().getPreds());
        return new StateT1_4(path, q3);
    }

    public List startElementDo(String tag,int layer,Stack currstack) throws CloneNotSupportedException{
        State currQ;
        if ((layer >= getLevel()) && (tag .equals(_test))) {//��ǰ�������ڵ���Ӧ��ƥ��Ĳ��� getLayer�����Ϳ���
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
        if(tag.equals(_test)){//�����Լ��Ľ�����ǩ,�ж�ν�ʼ��Ľ����ʧ����ɾ�����ɹ�����
            //����Ľ�����ǩ����ν����˵�����ϲ������ǩ�������������ν�ʽ��г�ջ����
            if(!this._predstack.isEmpty()){
                State currQ=(State)this._predstack.peek();
                Object object=outlist.get(outlist.size()-1);
                if(currQ instanceof TWaitState||currQ instanceof FWaitState
                        ||currQ instanceof  StateT3_2 || currQ instanceof  StateT3_4){
                    currQ.endElementDo(tag, layer, this._predstack);
                    currQ=(State)this._predstack.peek();
                    if(!(currQ instanceof TState)){
                        if(object instanceof List)
                            ((List)(object)).remove(((List)(object)).size() - 1);//ɾ�����һ��list�е����һ�����ϵı�ǩ
                        else
                            outlist.remove(outlist.size() - 1);//ɾ�����һ��
                        this._predstack.pop();
                    }
                    else  //�Ƶ�֮��ν��ջ���ɹ�
                        this._predstack.pop();
                }
                else if((currQ instanceof TState)){//ν�ʱ����ͼ��ɹ�
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
                else{//ν�ʱ����ͼ��ʧ��
                    if(object instanceof List){
                        ((List)(object)).remove(((List)(object)).size() - 1);
                    }
                    else
                        outlist.remove(outlist.size() - 1);//ɾ�����һ��
                    this._predstack.pop();
                }
            }
        }
        else{
            if(layer==getLevel()-1){ //�����ϲ������ǩ
                currstack.pop();
                if(currstack.isEmpty())
                    list.remove(this._predstack);
            }
        }
        return list;
    }

    public List checkOthers(String tag, int layer,Stack currstack) {
        this.endElementDo(tag, layer, currstack);
        if(!outlist.isEmpty()&&(outlist.size()==(this._predstack.size()+1))){//����Ҳֻ��������һ�����������
            System.out.println(outlist.get(outlist.size()-1));
            outlist.remove(outlist.size()-1);
        }
        return list;
    }

    public State copy() throws CloneNotSupportedException {
        return (State)this.clone();
    }

}
