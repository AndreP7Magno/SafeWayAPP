package safewayapp.Api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import safewayapp.Api.request.EmergencyCallRequest;
import safewayapp.Api.response.EmergencyCallResponse;

public interface EmergencyCallApi {
    @POST("/emergencycall/")
    Call<EmergencyCallResponse> postEmergencyCall(@Body EmergencyCallRequest body);
}
