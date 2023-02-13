package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_DISPLAY = "display";
    private static final String KEY_HISTORY = "history";
    private static final int REQUEST_CODE = 1;

    TextView result;
    TextView cal;
    Button bracket_left; Button bracket_right; Button percent; Button clear;
    Button num7; Button num8; Button num9; Button divide;
    Button num4; Button num5; Button num6; Button multiply;
    Button num1; Button num2; Button num3; Button subtract;
    Button num0; Button comma; Button equal; Button plus;
    TextView history;
    Button clearHistory;

    String stringCal = "";
    String textHis = "";

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    private View.OnClickListener display = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button b = (Button)view;
            String text = b.getText().toString();
            if(stringCal != "")
            {
                char endletter = stringCal.charAt(stringCal.length()-1);
                if(isNumeric(String.valueOf(endletter)))
                {
                    if(stringCal.contains("(") && text.equals(")"))
                    {
                        stringCal += text;
                        cal.setText(stringCal);
                    }
                    else if(!text.equals("(") && !text.equals(")")) {
                        stringCal += text;
                        cal.setText(stringCal);
                    }
                }
                else
                {
                    if(endletter == '%' || endletter == ')')
                    {
                        if(text.equals("+") || text.equals("*") || text.equals("/") || text.equals(")"))
                        {
                            stringCal += text;
                            cal.setText(stringCal);
                        }
                    }
                    if(isNumeric(text) || text.equals("(") || text.equals("-"))
                    {
                        stringCal += text;
                        cal.setText(stringCal);
                    }
                }
            }
            else{
                if(!text.equals("*") && !text.equals("/") && !text.equals(")") && !text.equals("%")) {
                    stringCal += text;
                    cal.setText(stringCal);
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Khai báo biến
        cal = findViewById(R.id.cal);
        result = findViewById(R.id.result);
        bracket_left = findViewById(R.id.bracket_left);
        bracket_right = findViewById(R.id.bracket_right);
        percent = findViewById(R.id.percent);
        clear = findViewById(R.id.clear);
        num7 = findViewById(R.id.num7);
        num8 = findViewById(R.id.num8);
        num9 = findViewById(R.id.num9);
        divide = findViewById(R.id.divide);
        num4 = findViewById(R.id.num4);
        num5 = findViewById(R.id.num5);
        num6 = findViewById(R.id.num6);
        multiply = findViewById(R.id.multiply);
        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        num3 = findViewById(R.id.num3);
        subtract = findViewById(R.id.subtract);
        num0 = findViewById(R.id.num0);
        comma = findViewById(R.id.comma);
        equal = findViewById(R.id.equal);
        plus = findViewById(R.id.plus);
        history = findViewById(R.id.history);
        clearHistory = findViewById(R.id.clearHistory);

        if(savedInstanceState != null)
        {
            Bundle bundle = savedInstanceState.getBundle("bundle");
            stringCal = bundle.getString(KEY_DISPLAY);
            textHis = bundle.getString(KEY_HISTORY);
            history.setText(textHis);
        }

        //EventHandler
        num1.setOnClickListener(display);
        num2.setOnClickListener(display);
        num3.setOnClickListener(display);
        num4.setOnClickListener(display);
        num5.setOnClickListener(display);
        num6.setOnClickListener(display);
        num7.setOnClickListener(display);
        num8.setOnClickListener(display);
        num9.setOnClickListener(display);
        num0.setOnClickListener(display);
        comma.setOnClickListener(display);
        percent.setOnClickListener(display);
        plus.setOnClickListener(display);
        subtract.setOnClickListener(display);
        multiply.setOnClickListener(display);
        divide.setOnClickListener(display);
        bracket_left.setOnClickListener(display);
        bracket_right.setOnClickListener(display);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cal.setText("");
                stringCal = "";
                result.setText("");
            }
        });

        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = cal.getText() + " = " + Result(cal.getText().toString());
                result.setText(text);
                cal.setText("");
                stringCal = "";

                //Hiển thị History
                textHis += text + "\n";
                history.setText(textHis);
            }
        });

        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textHis = "";
                history.setText(textHis);
            }
        });

        Button btnWatchAll = findViewById(R.id.watchAll);
        btnWatchAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(KEY_HISTORY, textHis);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }
    private String Result(String s)
    {
        ArrayList<Integer> indexOperands = new ArrayList<>();
        ArrayList<Double> Operators = new ArrayList<>();
        ArrayList<String> Operands = new ArrayList<>();

        //Nếu dấu '+', '-' ngay đầu thì thêm 0 vào đầu để code có thể chạy
        if(s.charAt(0) == '-' || s.charAt(0) == '+')
        {
            s = "0" + s;
        }

        //Tính trong ngoặc trước
        while(s.contains("("))
        {
            if(!s.contains(")")) return "ERROR";
            else
            {
                int indexBraketLeft = s.indexOf("(");
                int indexBracketright = s.lastIndexOf(")");
                String bracketReplace = s.substring(indexBraketLeft, indexBracketright + 1);
                String bracket = s.substring(indexBraketLeft + 1, indexBracketright);
                s = s.replace(bracketReplace, Result(bracket));
                if(s.equals("ERROR")) return "ERROR";
            }
        }

        //Nếu s chỉ là 1 số
        if(isNumeric(s)) return s;

        //Tìm index các operands và add nó vào ArrayList Operands
        for(int i = 0; i < s.length(); i++)
        {
            if(!isNumeric(String.valueOf(s.charAt(i))) && !String.valueOf(s.charAt(i)).equals("."))
            {
                indexOperands.add(i);
                Operands.add(String.valueOf(s.charAt(i)));
            }
        }

        //Tính '%' trước
        if(s.contains("%"))
        {
            if(Operands.size() > 1)
            {
                int indexPercent = Operands.indexOf("%");
                String stringPercent;
                try {
                    stringPercent = s.substring(indexOperands.get(indexPercent - 1) + 1, indexOperands.get(indexPercent));
                }
                catch(ArrayIndexOutOfBoundsException e)
                {
                    stringPercent = s.substring(0, indexOperands.get(indexPercent));
                }
                double numPercent = Double.parseDouble(stringPercent) / 100;
                s = s.replace(stringPercent+"%", String.valueOf(numPercent));
                return Result(s);
            }
            else
            {
                s = s.replace("%", "/100");
                return Result(s);
            }
        }

        //Add các operators vào ArrayList
        int CountIndexOperand = 0;
        ArrayList<Integer> indexSubtractRemoveOperands = new ArrayList<Integer>();
        for(int i = 0; i < s.length(); i++)
        {
            if(CountIndexOperand != indexOperands.size() - 1)
            {
                if(isNumeric(String.valueOf(s.charAt(i))))
                {
                    Operators.add(Double.parseDouble(s.substring(i, indexOperands.get(CountIndexOperand))));
                    i = indexOperands.get(CountIndexOperand);
                    CountIndexOperand++;
                }
                else {
                    CountIndexOperand++;

                }
            }
            else{
                if(isNumeric(s.substring(i, indexOperands.get(CountIndexOperand))))
                {
                    Operators.add(Double.parseDouble(s.substring(i, indexOperands.get(CountIndexOperand))));
                }
                Operators.add(Double.parseDouble(s.substring(indexOperands.get(CountIndexOperand) + 1, s.length())));
                break;
            }
        }


        //Kiểm tra số âm
        int countRemove = 0;
        for(int i = 0; i < indexOperands.size()-1; i++)
        {
            if(indexOperands.get(i+1) - indexOperands.get(i) == 1)
            {
                Operands.remove(i+1 - countRemove);
                double numRemove = Operators.get(i + 1 - countRemove);
                Operators.remove(i+1 - countRemove);
                Operators.add(i+1 - countRemove, -numRemove);
                countRemove++;

            }
        }

        // Tính toán
        for(int i = 0; i < Operands.size(); i++)
        {
            double a;
            double b;
            double c;
            while(Operands.contains("*"))
            {
                int multiplyIndex = Operands.indexOf("*");

                a = Operators.get(multiplyIndex);
                Operators.remove(multiplyIndex);

                b = Operators.get(multiplyIndex);
                Operators.remove(multiplyIndex);

                c = a * b;
                Operators.add(multiplyIndex, c);
                Operands.remove(multiplyIndex);
            }
            while(Operands.contains("/"))
            {
                int divideIndex = Operands.indexOf("/");

                a = Operators.get(divideIndex);
                Operators.remove(divideIndex);

                b = Operators.get(divideIndex);
                Operators.remove(divideIndex);

                if(b == 0) return "ERROR";
                c = a / b;
                Operators.add(divideIndex, c);
                Operands.remove(divideIndex);
            }
            if(!Operands.isEmpty() && Operands.get(i).equals("+"))
            {
                a = Operators.get(0);
                Operators.remove(0);

                b = Operators.get(0);
                Operators.remove(0);

                c = a + b;
                Operators.add(0, c);
            }
            if(!Operands.isEmpty() && Operands.get(i).equals("-"))
            {
                a = Operators.get(0);
                Operators.remove(0);

                b = Operators.get(0);
                Operators.remove(0);

                c = a - b;
                Operators.add(0, c);
            }
        }
        return Operators.get(0).toString();



    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_HISTORY, textHis);
        bundle.putString(KEY_DISPLAY, stringCal);
        outState.putBundle("bundle", bundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String msg = data.getStringExtra("result");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}