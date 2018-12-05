package safewayapp.Api.request;

import com.google.gson.annotations.SerializedName;


public class ContactRequest {
    @SerializedName("_id")
    private String id;
    @SerializedName("user")
    private String user;
    @SerializedName("name")
    private String nome;
    @SerializedName("phonenumber")
    private String telefone;

    public ContactRequest(String user, String nome, String telefone) {
        this.user = user;
        this.nome = nome;
        this.telefone = telefone;
    }

}
