package safewayapp.Api.response;

import com.google.gson.annotations.SerializedName;

public class ContactResponse {

    private String message;
    private String ok;
    @SerializedName("_id")
    private String id;

    private String user;
    private String name;
    private String phonenumber;

    public String getMessage() {
        return message;
    }

    public String getOk() {
        return ok;
    }


    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }
}
