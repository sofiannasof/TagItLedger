package eu.ubitech.smenesidou.tagitledger.model.nxt;

import com.google.gson.annotations.SerializedName;

public class IssueAssetResp {
    //The transaction ID is also the asset ID.
    @SerializedName("transaction")
    public String transaction;

    public String getAssetID() {
        return this.transaction;
    }
}