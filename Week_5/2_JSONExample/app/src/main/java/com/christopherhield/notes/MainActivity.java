package com.christopherhield.notes;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText name;
    private EditText description;
    private EditText price;
    private EditText weight;
    private EditText manufacturer;
    private Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.prodName);
        description = findViewById(R.id.prodDesc);
        description.setMovementMethod(new ScrollingMovementMethod());
        price = findViewById(R.id.prodPrice);
        weight = findViewById(R.id.prodWeight);
        manufacturer = findViewById(R.id.prodManu);

    }

    @Override
    protected void onResume() { // After Pause or Stop
        product = loadFile();

        if (product != null) {
            name.setText(product.getName());
            description.setText(product.getDescription());
            price.setText(String.format(Locale.getDefault(), "%.2f", product.getPrice()));
            weight.setText(String.format(Locale.getDefault(),"%.2f", product.getWeight()));
            manufacturer.setText(product.getManufacturer());
        }
        super.onResume();
    }

    private Product loadFile() {

        Log.d(TAG, "loadFile: Loading JSON File");
        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            String name = jsonObject.getString("name");
            String desc = jsonObject.getString("description");
            double price = jsonObject.getDouble("price");
            double weight = jsonObject.getDouble("weight");
            String manufacturer = jsonObject.getString("manufacturer");
            return new Product(name, desc, price, weight, manufacturer);

        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPause() { // Going to be partially or fully hidden
        String nameStr = name.getText().toString();
        String descStr = description.getText().toString();
        String priceStr = price.getText().toString();
        String weightStr = weight.getText().toString();
        String manuStr = manufacturer.getText().toString();


        if (!nameStr.isEmpty() && !descStr.isEmpty() &&
                !priceStr.isEmpty() && !weightStr.isEmpty() && !manuStr.isEmpty()) {
            product = new Product(
                    nameStr, descStr, Double.parseDouble(priceStr),
                    Double.parseDouble(weightStr), manuStr);
        }

        saveProduct();
        super.onPause();
    }

    private void saveProduct() {

        Log.d(TAG, "saveProduct: Saving JSON File");

        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(product.toJSON());
            printWriter.close();
            fos.close();

            Log.d(TAG, "saveProduct:\n" + product.toJSON());
            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
