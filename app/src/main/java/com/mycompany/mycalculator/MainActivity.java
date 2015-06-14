package com.mycompany.mycalculator;

import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.OutputStream;



public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private EditText scr;
    //private char infixArray[]=new char[20];
    private String infixArray[];
    private String postFix;
    private String output[];
    private String scrInfix = "";
    private int count = 0;
    int stackSize;
    int numbf=0;

    //scr=(EditText)findViewById(R.id.edit_Text);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scr = (EditText) findViewById(R.id.edit_Text);
        infixArray=new String[20];
        output=new String[20];
        int idList[] = {R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9
                , R.id.buttonadder, R.id.buttonsub, R.id.buttonmul, R.id.buttondot, R.id.buttoneq, R.id.buttondiv, R.id.buttonclr};

        for (int id : idList) {

            View v = (View) findViewById(id);
            v.setOnClickListener(this);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getKeyBoard(String str) {
        String ScrCurrent = scr.getText().toString();
        if (ScrCurrent.equals("0"))
            ScrCurrent = "";
        ScrCurrent += str;
        scr.setText(ScrCurrent);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonclr:
                scr.setText("0");
                count = 0;
                numbf=0;
                break;
            case R.id.buttoneq:
                scrInfix = ((EditText) findViewById(R.id.edit_Text)).getText().toString();
                count++;
                InToPost(infixArray);
                scr.setText("");
                PostToEvaluate(output);
                count=0;
                scr.setText("");
                break;
            case R.id.buttonadder:
            case R.id.buttonsub:
            case R.id.buttonmul:
            case R.id.buttondiv:
                numbf=0;
                String oper = ((Button) v).getText().toString();
                getKeyBoard(oper);
                infixArray[++count]=oper;
                count++;
                break;
            default:
                String numb=((Button) v).getText().toString();
                getKeyBoard(numb);
                numbf=numbf*10+(Integer.parseInt(numb));
                infixArray[count]=String.valueOf(numbf);
               /* if (infixArray == null)
                    infixArray = numb;
                else
                    infixArray += numb;
                count++; */

        }
    }



    void InToPost(String str[]) {
        //stackSize = count;
        Stack theStack = new Stack(100);
        String ch;
        int p = 0, i,outcount=0;
        for (i = 0; i<count; i++) {
            ch =str[i];
            if (str[i].equals("(")) {
                theStack.push(str[i]);
            }
            else if (IsOperand(ch)) {
                output[outcount++]= ch;
            }
            else if (operator(ch)) {
                if (theStack.top == 0 || (pre(ch) > pre(theStack.stackArray[theStack.top-1]) || theStack.stackArray[theStack.top-1].equals("(")))
                {
                    theStack.push(ch);
                }
                else if (pre(ch) <= pre(theStack.stackArray[theStack.top-1])) {
                    output[outcount++]= theStack.pop();
                    theStack.push(ch);
                }
            }
            else if (ch.equals("(")) {
                while ((ch = theStack.pop()) != "(") {
                    output[outcount++]= ch;
                }
            }
        }
        while (theStack.top != 0) {
            output[outcount++]= theStack.pop();
        }

    }
    public void PostToEvaluate(String str[]) {
        Stack outputStack = new Stack(100);
        int op1, op2;
        for (int k = 0; k<count; k++) {
            if (IsOperand(str[k]))
                outputStack.push(str[k]);
            else {
                op1 = Integer.parseInt(String.valueOf(outputStack.pop()));
                op2 = Integer.parseInt(String.valueOf(outputStack.pop()));
                op1 = evaluate(op1, op2, str[k]);

                outputStack.push(Integer.toString(op1));//String.valueOf(op1));


            }
        }
        scr.setText("");
        scr.setText(outputStack.pop()) ;
    }

    public int evaluate(int op1, int op2, String k) {
        switch (k) {
            case "+":
                return op1 + op2;
            case "-":
                return op1 - op2;
            case "*":
                return op1 * op2;
            case "/":
                return op1 / op2;
        }
        return 999;
    }


    int pre(String ch) {
        switch (ch) {
            case "-":
                return 1;
            case "+":
                return 1;
            case "*":
                return 2;
            case "/":
                return 2;
        }
        return 0;
    }

    boolean operator(String ch) {
        if (ch.equals("/") || ch.equals("*") || ch.equals("+") || ch.equals("-"))
            return true;
        else
            return false;
    }

    public boolean IsOperand(String c) {

        boolean isoperand=false;
        try{
            Integer.parseInt(c);
            isoperand=true;
        }
        catch(java.lang.NumberFormatException e)
        {

        }
            return isoperand;
    }
}
    class Stack {
         int maxSize;
         String[] stackArray;
         int top;
        Stack(int max) {
            maxSize = max;
            stackArray = new String[maxSize];
            top = 0;
        }
        public void push(String j) {
            stackArray[top] = j;
            top++;
        }
        public String pop() {
            return stackArray[--top];
        }
        public String peek() {
            return stackArray[top];
        }
        public boolean isEmpty() {
            return (top == -1);
        }
    }


 /* public void mMath(String str)
    {
       Numberbf=Float.parseFloat(scr.getText().toString());
        operation=str;
        scr.setText("0");

    }*/ /*public class Stack
    {
        char stackArray[]=new char[20];
        int top;
        void push(char ch)
        {
            top++;
            stack1[top]=ch;
        }
        char pop()
        {
            char ch;
            ch=stack1[top];
            top--;
            return ch;
        }*/
/*public void InToPost(String infixArray) {

            for (int j = 0; j<count; j++) {
                char ch = infixArray.charAt(j);
                switch (ch) {
                    case '+':
                    case '-':
                        gotOper(ch, 1);
                        break;
                    case '*':
                    case '/':
                        gotOper(ch, 2);
                        break;
                    case '(':
                        theStack.push(ch);
                        break;
                    case ')':
                        gotParen(ch);
                        break;
                    default:
                        output = output + ch;

                }
            }
            while (!theStack.isEmpty()) {
                output = output + theStack.pop();
            }


        }
        public void gotOper(char opThis, int prec1) {
            while (!theStack.isEmpty()) {
                char opTop = theStack.pop();
                if (opTop == '(') {
                    theStack.push(opTop);
                    break;
                }
                else {
                    int prec2;
                    if (opTop == '+' || opTop == '-')
                        prec2 = 1;
                    else
                        prec2 = 2;
                    if (prec2 < prec1) {
                        theStack.push(opTop);
                        break;
                    }
                    else
                        output = output + opTop;
                }
            }
            theStack.push(opThis);
        }
        public  void gotParen(char ch){
            while (!theStack.isEmpty()) {
                char chx = theStack.pop();
                if (chx == '(')
                    break;
                else
                    output = output + chx;
            }

        }
     */
/* case R.id.buttonclr:
                scr.setText("0");
                Numberbf=0;
                operation="";
                break;
            case R.id.buttonadder:
                mMath("+");
                break;
            case R.id.buttonsub:
                mMath("-");
                break;
            case R.id.buttonmul:
                mMath("*");
                break;
            case R.id.buttondiv:
                mMath("/");
                break;
            case R.id.buttoneq:
                mResult();
                break;
            default:
                   break;*/
 /* public void mResult()
    {
        float NumAf=Float.parseFloat(scr.getText().toString());
        float result=0;
        if(operation.equals("+"))
            result=Numberbf+NumAf;
        if(operation.equals("-"))
            result=Numberbf-NumAf;
        if(operation.equals("*"))
            result=Numberbf*NumAf;
        if(operation.equals("/"))
            result=Numberbf/NumAf;
        scr.setText(String.valueOf(result));

    }*/