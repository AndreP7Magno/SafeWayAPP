package safewayapp.Api.response;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("CPF")
    private String CPF;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;

    public String getCPF() {
        return CPF;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
