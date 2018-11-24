package safewayapp.Persistence;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "usuario")
public class Usuario {

    @NonNull
    @PrimaryKey
    private String CPF;

    private String Nome;
    private String Email;
    private String Foto;
    private String id;

    public Usuario() {}

    @Ignore
    public Usuario(String CPF,String Nome,String Email, String id) {
        this.Nome = Nome;
        this.CPF = CPF;
        this.Email = Email;
        Foto = "";
        this.id = id;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFoto() {
        return Foto == null ? "" : Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
