package safewayapp.Api.request;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    private String name;
    private String email;
    @SerializedName("CPF")
    private String CPF;
    private String password;

    public LoginRequest(String name, String email, String CPF, String password) {
        this.name = name;
        this.email = email;
        this.CPF = CPF;
        this.password = password;
    }

    public LoginRequest(String CPF, String password){
        this.CPF = CPF;
        this.password = password;
    }
}
