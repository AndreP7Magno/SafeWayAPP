package safewayapp.Api.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private String name;
    private String email;
    @SerializedName("CPF")
    private String CPF;
    private String token;
    private String message;
    private String ok;

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCPF() {
        return CPF;
    }

    public String getMessage() {
        return message;
    }

    public String getOk() {
        return ok;
    }
}
