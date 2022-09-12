package com.example.tipsplit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
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
                tipPercentInt = giveIntegerPercentage(tipPercentString);

                // Log.d(TAG , String.valueOf(tipPercentInt));

                // Converting to Double
                double billAmtWithTaxVar = Double.parseDouble(billAmtTaxString);

                // Calculate the Tip Amount
                tipAmountVar = (billAmtWithTaxVar / 100) * tipPercentInt;
                tipAmtId.setText(String.valueOf(tipAmountVar));

                // Calculate the bill Total with the Tip Amount
                totalBillWithTipVar = billAmtWithTaxVar + tipAmountVar;
                totalWithTipId.setText(String.valueOf(totalBillWithTipVar));

            }
        });

    }

    public void userClicksGo(Button buttonId){
        buttonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numPeopleId = findViewById(R.id.editTextNumPeople);
                numPeopleInt = Integer.parseInt(numPeopleId.getText().toString());

                // Divide by the amount
                totalPerPerson = totalBillWithTipVar / numPeopleInt;
                totalAmtPerPersonId = findViewById(R.id.totalPerPerson);
                totalAmtPerPersonId.setText(String.valueOf(totalPerPerson));

            }
        });
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Defining the views and getting their IDs
        radioGroupButton = findViewById(R.id.tipPercentageButton);
        billAmtWithTax = findViewById(R.id.billTotalWithAmount);
        tipAmtId = findViewById(R.id.tipAmount);
        totalWithTipId = findViewById(R.id.totalAmtWithTip);
        goButtonId = findViewById(R.id.goButton);




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
                    radioButtonToggler(false , radioGroupButton);
                    tipAmtId.setText("");
                    totalWithTipId.setText("");


                } else{

                    if(radioGroupButton.getCheckedRadioButtonId() == -1){
                        radioButtonToggler(true , radioGroupButton);
                        radioButtonGroupFunction(radioGroupButton , billAmtTaxString);
                        userClicksGo(goButtonId);
                    }
                    else{

                        radioTipButton = findViewById(radioGroupButton.getCheckedRadioButtonId());
                        radioTipButton.setChecked(false);
                        radioButtonToggler(true , radioGroupButton);
                        radioButtonGroupFunction(radioGroupButton , billAmtTaxString);
                        userClicksGo(goButtonId);
                    }


                }
            }
        });




    }

}