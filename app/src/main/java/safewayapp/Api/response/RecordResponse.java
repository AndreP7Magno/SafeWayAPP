package safewayapp.Api.response;

import com.google.gson.annotations.SerializedName;

public class RecordResponse {
    //para o create
    private String message;
    private String ok;

    //para o get
    private String date;
    @SerializedName("_id")
    private String _id;
    private String user;
    @SerializedName("lat")
    private String latitude;
    @SerializedName("long")
    private String longitude;
    private String severity;
    private String description;

    public String getOk() {
        return ok;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String get_id() {
        return _id;
    }

    public String getUser() {
        return user;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getSeverity() {
        return severity;
    }

    public String getDescription() {
        return description;
    }
}
