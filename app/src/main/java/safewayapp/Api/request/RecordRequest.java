package safewayapp.Api.request;

import com.google.gson.annotations.SerializedName;

public class RecordRequest {
    private String user;
    @SerializedName("lat")
    private String latitude;
    @SerializedName("long")
    private String longitude;
    private String severity;
    private String description;
    private String date;

    public RecordRequest(String user, String latitude, String longitude, String severity, String description, String date) {
        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
        this.severity = severity;
        this.description = description;
        this.date = date;
    }

    public RecordRequest(){}
}
