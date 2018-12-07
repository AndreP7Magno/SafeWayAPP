package safewayapp.Api.response;

import com.google.gson.annotations.SerializedName;

public class Contact {

    @SerializedName("_id")
    public String id;
    @SerializedName("user")
    public String user;
    @SerializedName("name")
    public String nome;
    @SerializedName("phonenumber")
    public String telefone;

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }
}
