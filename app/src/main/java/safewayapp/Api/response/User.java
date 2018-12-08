package safewayapp.Api.response;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("_id")
    public String id;
    @SerializedName("CPF")
    public String cpf;
    @SerializedName("email")
    public String email;
    @SerializedName("name")
    public String nome;

    public String getId() {
        return id;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }
}
