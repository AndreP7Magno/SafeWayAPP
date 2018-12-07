package safewayapp.Api.response;

import com.google.gson.annotations.SerializedName;

public class Record {

    @SerializedName("_id")
    public String id;
    @SerializedName("date")
    public String data;
    @SerializedName("description")
    public String descricao;
    @SerializedName("lat")
    public String latitute;
    @SerializedName("long")
    public String longitude;
    @SerializedName("severity")
    public String severidade;
    @SerializedName("user")
    public String user;

    public String getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getLatitute() {
        return latitute;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getSeveridade() {
        return severidade;
    }

    public String getUser() {
        return user;
    }
}
