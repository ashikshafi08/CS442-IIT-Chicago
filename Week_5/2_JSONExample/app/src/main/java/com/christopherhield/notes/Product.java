package com.christopherhield.notes;

import android.util.JsonWriter;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.StringWriter;

public class Product {

    private final String name;
    private final String description;
    private final double price;
    private final double weight;
    private final String manufacturer;

    public Product(String name, String description, double price,
                   double weight, String manufacturer) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.manufacturer = manufacturer;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public double getWeight() {
        return weight;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    @NonNull
    public String toJSON() {

        try {
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();

            jsonWriter.name("name").value(getName());
            jsonWriter.name("description").value(getDescription());
            jsonWriter.name("price").value(getPrice());
            jsonWriter.name("weight").value(getWeight());
            jsonWriter.name("manufacturer").value(getManufacturer());

            jsonWriter.endObject();
            jsonWriter.close();
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", weight=" + weight +
                ", manufacturer='" + manufacturer + '\'' +
                '}';
    }
}
