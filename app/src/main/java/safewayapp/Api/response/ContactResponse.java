package safewayapp.Api.response;

import com.google.gson.annotations.SerializedName;

public class ContactResponse {

    private String message;
    private String ok;

    @SerializedName("_id")
    private String id;

    @SerializedName("user")
    private User user;

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

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

}
