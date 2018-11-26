package safewayapp.Persistence;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "record")
public class Record {

    @NonNull
    @PrimaryKey
    private String id;

    private String Data;
    private String Descricao;
    private String Latitute;
    private String Longitude;
    private String Severity;
    private String User;

    public Record() {}

    @Ignore
    public Record(String id,String Data, String Descricao, String Latitute, String Longitude, String Severity, String User) {
        this.id = id;
        this.Data = Data;
        this.Descricao = Descricao;
        this.Latitute = Latitute;
        this.Longitude = Longitude;
        this.Severity = Severity;
        this.User = User;
    }


    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getLatitute() {
        return Latitute;
    }

    public void setLatitute(String latitute) {
        Latitute = latitute;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getSeverity() {
        return Severity;
    }

    public void setSeverity(String severity) {
        Severity = severity;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }
}
