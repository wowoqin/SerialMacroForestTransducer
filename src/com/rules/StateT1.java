package com.rules;

import com.XPath.PathParser.ASTPath;
import com.XPath.PathParser.AxisType;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by qin on 2015/10/10.
 */
public class StateT1 extends State implements Cloneable {

    protected  ASTPath _path;
    protected  String  _test;

    protected Stack _predstack;
    protected Stack _pathstack;


    protected StateT1(ASTPath path) {
        _path = path;
        _test=_path.getFirstStep().getNodeTest().toString();
    }


    public String getNodeTest() {//�õ���ǰ XPath �Ĳ��Խڵ�
        return _path.getFirstStep().getNodeTest().toString();
    }

    public static State TranslateStateT1(ASTPath path) {
        //���������͡�ʣ��pathѡ���Եĵ�����Ӧ��T1����
        if (path.getFirstStep().getAxisType() == AxisType.PC) {//PC ��
            if (path.getRemainderPath().toString().equals("")){ //�޺���·��
                if (path.getFirstStep().getPreds().toString().equals("")){//��ν��
                    return StateT1_1.TranslateState(path);     //T1_1
                }
                else{
                    return StateT1_2.TranslateState(path);//��ν��
                }
            }
            else {
                if (path.getFirstStep().getPreds().toString().equals("")){//�к���·������ν��
                    return StateT1_5.TranslateState(path);
                }
                else{
                    return StateT1_6.TranslateState(path);//��ν��
                }
            }
        }
        //AD ��
        else{
            if (path.getRemainderPath().toString().equals("")){//�޺���·��
                if (path.getFirstStep().getPreds().toString().equals("")) //��ν��
                    return StateT1_3.TranslateState(path);
                else return StateT1_4.TranslateState(path);//��ν��
            }
            else {
                if (path.getFirstStep().getPreds().toString().equals(""))//�к���·������ν��
                    return StateT1_7.TranslateState(path);
                else return StateT1_8.TranslateState(path);//��ν��
            }
        }
    }


    public State copy() throws CloneNotSupportedException {
        return (State)this.clone();
    }

    @Override
    public List startElementDo(String tag, int layer,Stack currstack) throws CloneNotSupportedException {
        this.setLevel(layer);
        return list;
    }


    @Override
    public List endElementDo(String tag,int layer,Stack currstack){
        if(layer==getLevel()-1)
            currstack.pop();
        return list;
    }

    public List checkOthers(String tag, int layer,Stack currstack) {
        return list;
    }

}
