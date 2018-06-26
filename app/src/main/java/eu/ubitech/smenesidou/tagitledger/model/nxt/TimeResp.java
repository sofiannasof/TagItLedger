package eu.ubitech.smenesidou.tagitledger.model.nxt;

import com.google.gson.annotations.SerializedName;

public class TimeResp {
    @SerializedName("time")
    public String time;
    @SerializedName("requestProcessingTime")
    public int requestProcessingTime;
}

