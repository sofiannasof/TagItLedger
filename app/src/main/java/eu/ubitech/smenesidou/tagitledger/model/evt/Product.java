package eu.ubitech.smenesidou.tagitledger.model.evt;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("id")
    public String id;
    @SerializedName("brand")
    public String brand;

    public Product(String id, String brand) {
        this.id = id;
        this.brand = brand;
    }

    public String getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }
}
