package safewayapp.Api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoricResponse {

    @SerializedName("records")
    private List<Record> records;

    @SerializedName("emergencyCalls")
    private List<EmergencyCall> emergencyCalls;

    public List<Record> getRecords() {
        return records;
    }

    public List<EmergencyCall> getEmergencyCalls() {
        return emergencyCalls;
    }

}
