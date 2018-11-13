package safewayapp.Persistence;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "contato")
public class Contato {

    @NonNull
    @PrimaryKey
    private String Nome;

    private String Telefone;

    public Contato() {}

    public Contato(String nome, String telefone) {
        this.Nome = nome;
        this.Telefone = telefone;
    }

    @NonNull
    public String getNome() {
        return Nome;
    }

    public void setNome(@NonNull String nome) {
        Nome = nome;
    }

    public String getTelefone() {
        return Telefone;
    }

    public void setTelefone(String telefone) {
        Telefone = telefone;
    }
}
