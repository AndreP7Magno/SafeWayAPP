package safewayapp.Api.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmergencyCallRequest {
    @SerializedName("lat")
    private String latitute;
    @SerializedName("long")
    private String longitude;
    @SerializedName("user")
    private String user;
    @SerializedName("contacts")
    private List<String> contacts;
    @SerializedName("date")
    private String date;

    public EmergencyCallRequest(String latitute, String longitude, String user, List<String> contacts, String date) {
        this.latitute = latitute;
        this.longitude = longitude;
        this.user = user;
        this.contacts = contacts;
        this.date = date;
    }
}
