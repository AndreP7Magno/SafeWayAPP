package safewayapp.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import safewayapp.Api.response.HistoricResponse;

public interface HistoricApi {
    @GET("/historic/{id}")
    Call<List<HistoricResponse>> getHistoric(@Path("id") String id);
}
