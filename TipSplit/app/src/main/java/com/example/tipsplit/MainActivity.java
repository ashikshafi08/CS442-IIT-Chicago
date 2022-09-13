package com.example.tipsplit;

import java.text.DecimalFormat;
import java.math.RoundingMode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.Toast;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.View;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RadioGroup radioGroupButton;
    private RadioButton radioTipButton;
    private Button goButtonId;
    private Button clearButtonId;

    private EditText billAmtWithTax;
    private EditText numPeopleId;


    private TextView tipAmtId;
    private TextView totalWithTipId;
    private TextView totalAmtPerPersonId;


    // Define some variables
    private double tipAmountVar;
    private double totalBillWithTipVar;
    private double totalPerPerson;
    private int tipPercentInt;
    private int numPeopleInt;

    // Defining the Decimal Format
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");






    /// Making Helper Functions ///
    ///////////////////////////////

    public int giveIntegerPercentage(String tipPercentString){

        String tipNumString = tipPercentString.substring(0 ,2);
        return Integer.parseInt(tipNumString);
    }

    public void radioButtonToggler(boolean state , RadioGroup radioGroupButton){

        for (int button = 0; button < this.radioGroupButton.getChildCount(); button++){
            this.radioGroupButton.getChildAt(button).setEnabled(state);
        }
    }

    public void radioButtonGroupFunction(RadioGroup radioGroupButton , String billAmtTaxString){
        radioGroupButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int iD) {

                // Getting the text of the percentage
                radioTipButton = findViewById(iD);
                String  tipPercentString = radioTipButton.getText().toString();
                radioTipButton.setActivated(false);
                tipPercentInt = giveIntegerPercentage(tipPercentString);

                // Log.d(TAG , String.valueOf(tipPercentInt));

                // Converting to Double
                double billAmtWithTaxVar = Double.parseDouble(billAmtTaxString);

                // Calculate the Tip Amount
                tipAmountVar = (billAmtWithTaxVar / 100) * tipPercentInt;
                tipAmtId.setText(String.format("%.2f" , tipAmountVar));


                // Calculate the bill Total with the Tip Amount
                totalBillWithTipVar = billAmtWithTaxVar + tipAmountVar;
                totalWithTipId.setText(String.format("%.2f", totalBillWithTipVar));


            }
        });

    }

    protected void userClicksGo(Button buttonId){
        buttonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numPeopleInt = Integer.parseInt(numPeopleId.getText().toString());

                // Divide by the amount
                totalPerPerson = totalBillWithTipVar / numPeopleInt;
                totalAmtPerPersonId.setText(String.format("%.2f" , totalPerPerson));

            }
        });
    }

    protected void clearEverythingButton(Button clearButtonId){
        clearButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billAmtWithTax.setText("");
                tipAmtId.setText("");
                totalWithTipId.setText("");
                numPeopleId.setText("");
                totalAmtPerPersonId.setText("");

                radioButtonToggler(false , radioGroupButton);
            }
        });
    }

// TODO: 1. Finish up the Formating of the Double variable and rounding up of the values
// TODO: 2. Configure the Clear button for the whole app, by clicking so the entire values stored should be gone.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
            Toast.makeText(this, "savedInstanceState is NULL", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "savedInstanceState is NOT NULL", Toast.LENGTH_LONG).show();


        // Defining the views and getting their IDs
        radioGroupButton = findViewById(R.id.tipPercentageButton);
        billAmtWithTax = findViewById(R.id.billTotalWithAmount);
        tipAmtId = findViewById(R.id.tipAmount);
        totalWithTipId = findViewById(R.id.totalAmtWithTip);
        numPeopleId = findViewById(R.id.editTextNumPeople);
        goButtonId = findViewById(R.id.goButton);
        clearButtonId = findViewById(R.id.clearButton);
        totalAmtPerPersonId = findViewById(R.id.totalPerPerson);





        // Checking if the Bill with Total Tax Field is Empty or Not
//        String billAmtTaxString = billAmtWithTax.getText().toString();

        billAmtWithTax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String billAmtTaxString = billAmtWithTax.getText().toString();

                if (billAmtTaxString.trim().matches("")) {
                    if(radioGroupButton.getCheckedRadioButtonId() !=-1){
                        radioTipButton = findViewById(radioGroupButton.getCheckedRadioButtonId());
                        radioTipButton.setChecked(false);
                        radioButtonToggler(false , radioGroupButton);
                        tipAmtId.setText("");
                        totalWithTipId.setText("");


                    }else{
                        radioButtonToggler(false , radioGroupButton);
                        tipAmtId.setText("");
                        totalWithTipId.setText("");
                    }




                } else{


                    if(radioGroupButton.getCheckedRadioButtonId() == -1){
                        // radioButtonToggler(false , radioGroupButton);
                        radioButtonToggler(true , radioGroupButton);
                        radioButtonGroupFunction(radioGroupButton , billAmtTaxString);
                        userClicksGo(goButtonId);

                    }
                    else{

                        radioTipButton = findViewById(radioGroupButton.getCheckedRadioButtonId());
                        radioButtonToggler(true , radioGroupButton);
                        radioButtonGroupFunction(radioGroupButton , billAmtTaxString);
                        //radioTipButton.setChecked(false);
                        //radioTipButton.setActivated(false);
                        userClicksGo(goButtonId);

                    }


                }
            }
        });

        clearEverythingButton(clearButtonId);





    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("TotalPerPersonString" , String.format("%.2f" , totalPerPerson));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        totalAmtPerPersonId.setText(savedInstanceState.getString("TotalPerPersonString"));


    }
}