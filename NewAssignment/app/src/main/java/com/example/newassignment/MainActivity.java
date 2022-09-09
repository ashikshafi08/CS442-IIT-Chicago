package com.example.newassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RadioGroup radioGroupButton;
    private EditText billAmtWithTax;
    private RadioButton radioTipButton;
    private TextView tipAmtId;
    private TextView totalWithTipId;
    private Button goButtonId;

    public int giveInteger(String tipPercentString){

        // Slicing the string
        String tipNumString = tipPercentString.substring(0 , 2);
        // Converting into Integer
        return Integer.parseInt(tipNumString);
    }


    public void radioButtonToggler(boolean state, RadioGroup radioGroupButton){
        for (int button = 0; button < this.radioGroupButton.getChildCount(); button++){
            this.radioGroupButton.getChildAt(button).setEnabled(state);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroupButton = findViewById(R.id.tipPercentageButton);
        billAmtWithTax = findViewById(R.id.billTotalWithAmount);
        tipAmtId = findViewById(R.id.tipAmount);
        totalWithTipId = findViewById(R.id.totalAmtWithTip);


        //radioButtonToggler(false , radioGroupButton);
        billAmtWithTax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(charSequence.toString().trim().matches("")){
//                    Log.d(TAG, "Its Empty");
//
//                    // Looping and disabling all child
//                    radioButtonToggler(false , radioGroupButton);
//                }else{
//                    radioButtonToggler(true , radioGroupButton);
//
//                    // Getting the RadioID from the RadioGroup
//                    int radioId = radioGroupButton.getCheckedRadioButtonId();
//                    radioTipButton = findViewById(radioId);
//
//
//                    // Getting the percentage in String
//                    String tipPercent = radioTipButton.getText().toString();
//                    int tipPercentInt  = giveInteger(tipPercent);
//                    Log.d(TAG , "Percentage: " + String.valueOf(tipPercentInt));
//
//                    billAmtWithTax = findViewById(R.id.billTotalWithAmount);
//                    String billAmtWithTaxString =  billAmtWithTax.getText().toString();
//                    double tipPercentDouble = Double.parseDouble(billAmtWithTaxString);
//                    Log.d(TAG ,"Amount is: " + String.valueOf(tipPercentDouble));
                    // double billAmt = Double.parseDouble(charSequence.toString());

                    // double tipAmtpercent = (billAmt * 100) / tipPercentDouble;

                    // TextView tipAmt = findViewById(R.id.tipAmount);
                    // tipAmt.setText(String.valueOf(billAmt));



                }

            @Override
            public void afterTextChanged(Editable editable) {
                String billAmt = billAmtWithTax.getText().toString();

                if (billAmt.trim().matches("")){
                    radioGroupButton.clearCheck();
                    tipAmtId.setText("");
                    totalWithTipId.setText("");


                }else{
                        radioTipButton = findViewById(radioGroupButton.getCheckedRadioButtonId());
                        String tipPercentString = radioTipButton.getText().toString();
                        double billAmtwithTax = Double.parseDouble(billAmt);
                        int tipPercentInt = giveInteger(tipPercentString);

                        double tipAmt = (billAmtwithTax / 100) * tipPercentInt;
                        tipAmtId.setText(String.valueOf(tipAmt));

                        // Calculating the total with the Tip Amount
                        double totalBillWithTip = billAmtwithTax + tipAmt;
                        totalWithTipId.setText(String.valueOf(totalBillWithTip));

//                        goButtonId = findViewById(R.id.goButton);

                        // Log.d(TAG , "Tip amount: " + String.valueOf(tipAmt));

                }
            }
        });





    }
}


//        radioGroupButton = findViewById(R.id.tipPercentageButton);

