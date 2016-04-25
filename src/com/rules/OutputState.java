package com.rules;

import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by qin on 2015/10/15.
 */
public class OutputState extends State{
    protected String _output;
    public OutputState(String output){
        _output=output;
    }

    public String getNodeTest() {//�õ���ǰ XPath �Ĳ��Խڵ�
        return "";
    }

    @Override
    public List startElementDo(String tag, int layer,Stack currstack) {
        System.out.println(_output);
        currstack.pop();//���� OutputState
        if(currstack.empty())//���� OutputState ֮������ǰջΪ�գ����������ջ
            list.remove(currstack);
        return list;
    }


    public State copy(){
        return new OutputState(_output);
    }

    public List endElementDo(String tag,int layer,Stack currstack){
        currstack.pop();
        return list;
    }

    @Override
    public List checkOthers(String tag, int layer, Stack currstack) {
        return list;
    }
}
