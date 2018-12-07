package safewayapp.Api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmergencyCall {

    @SerializedName("contacts")
    public List<Contact> contatos;
    @SerializedName("_id")
    public String id;
    @SerializedName("lat")
    public String latitude;
    @SerializedName("long")
    public String longitude;
    @SerializedName("user")
    public String user;
    @SerializedName("date")
    public String data;

    public List<Contact> getContatos() {
        return contatos;
    }

    public String getId() {
        return id;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getUser() {
        return user;
    }

    public String getData() {
        return data;
    }
}
