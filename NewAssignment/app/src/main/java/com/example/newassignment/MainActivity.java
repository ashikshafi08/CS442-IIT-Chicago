package com.example.newassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.view.View;
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
    private TextView totalAmtPerPersonId;
    private Button goButtonId;

    // Variables
    public double tipAmt;




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
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}


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

                        tipAmt = (billAmtwithTax / 100) * tipPercentInt;
                        tipAmtId.setText(String.valueOf(tipAmt));

                        // Calculating the total with the Tip Amount
                        double totalBillWithTip = billAmtwithTax + tipAmt;
                        totalWithTipId.setText(String.valueOf(totalBillWithTip));
                        goButtonId = findViewById(R.id.goButton);

                        goButtonId.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EditText numPeopleInt = findViewById(R.id.editTextNumPeople);
                                int numberOfPeople = Integer.parseInt(numPeopleInt.getText().toString());

                                // Divide by the amount
                                double totalPerPerson =  totalBillWithTip / numberOfPeople;
                                totalAmtPerPersonId = findViewById(R.id.totalAmtPerPerson);
                                totalAmtPerPersonId.setText(String.valueOf(totalPerPerson));
                            }
                        });

//                        goButtonId = findViewById(R.id.goButton);

                        // Log.d(TAG , "Tip amount: " + String.valueOf(tipAmt));

                }
            }
        });





    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("BILLAMTWITHTAX" , billAmtWithTax.getText().toString());
        outState.putDouble("TIPAMOUNT" , tipAmt);


        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState){

        // Calling the super here first
        super.onRestoreInstanceState(savedInstanceState);

        // The variables we wanna store will come under here
    }

}






//        radioGroupButton = findViewById(R.id.tipPercentageButton);

