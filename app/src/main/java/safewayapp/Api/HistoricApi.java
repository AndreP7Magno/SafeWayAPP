package safewayapp.Api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import safewayapp.Api.response.HistoricResponse;

public interface HistoricApi {
    @GET("/historic/{id}")
    Call<HistoricResponse> getHistoric(@Path("id") String id);
}
