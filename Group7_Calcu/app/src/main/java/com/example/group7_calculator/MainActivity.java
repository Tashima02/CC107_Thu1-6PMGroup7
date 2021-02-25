package com.example.group7_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView Screen;
    private Button AC,Back,Div,Svn,Eyt,Nyn,Mult,Ffr,Fve,Siks,Min,Wan,Too,Tri,Add,Siro,Point,Equal;
    private String input,Answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Screen = findViewById(R.id.screen);
        AC = findViewById(R.id.AC);

        Back = findViewById(R.id.Bck);
        Div = findViewById(R.id.Div);
        Svn = findViewById(R.id.Svn);
        Eyt = findViewById(R.id.Eyt);
        Nyn = findViewById(R.id.Nyn);
        Mult = findViewById(R.id.Mult);
        Ffr = findViewById(R.id.Ffr);
        Fve = findViewById(R.id.Fve);
        Siks = findViewById(R.id.Siks);
        Min = findViewById(R.id.Min);
        Wan = findViewById(R.id.Wan);
        Too = findViewById(R.id.Too);
        Tri = findViewById(R.id.Tri);
        Add = findViewById(R.id.add);
        Siro = findViewById(R.id.Siro);
        Point = findViewById(R.id.Point);

        Equal = findViewById(R.id.Equal);

    }
    public void ButtonClick(View view){
        Button button =(Button) view;
        String data = button.getText().toString();
        switch (data){
            case"AC":
                input="";
                break;
            case "ANS":
                input+=Answer;
                break;
            case "x":
                Solve();
                input+="*";
                break;
            case "^":
                Solve();
                input+="^";
                break;
            case "=":
                Solve();
                Answer=input;
                break;
            case "⬅":
                String newText =input.substring(0,input.length()-1);
                input =newText;
                break;
            default:
                if (input==null){
                    input="";
                }
                if (data.equals("+") || data.equals("-") || data.equals("/")){
                    Solve();
                }
                input+=data;
        }
        Screen.setText(input);

    }
    private void Solve(){
        if(input.split("\\*").length==2){
            String number[]=input.split("\\*");
            try {


                double mul = Double.parseDouble(number[0]) * Double.parseDouble(number[1]);
                input = mul+"";
            }
            catch (Exception e){

            }

        }

        else if(input.split("/").length==2){
            String number[]=input.split("/");
            try {


                double div = Double.parseDouble(number[0]) / Double.parseDouble(number[1]);
                input = div+"";
            }
            catch (Exception e){

            }

        }
        else if(input.split("\\+").length==2){
            String number[]=input.split("\\+");
            try {


                double sum = Double.parseDouble(number[0]) + Double.parseDouble(number[1]);
                input = sum+"";
            }
            catch (Exception e){

            }

        }

        else if(input.split("-").length==2){
            String number[]=input.split("-");
            if(number[0]=="" && number.length==2){
                number[0]=0+"";
            }
            try {
                double sum = Double.parseDouble(number[0]) - Double.parseDouble(number[1]);
                input = sum+"";
            }
            catch (Exception e){

            }

        }

        String n[]=input.split("\\.");
        if(n.length>1){
            if (n[1].equals("0")){
                input=n[0];
            }
        }
    }
}