package safewayapp.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import safewayapp.Api.request.RecordRequest;
import safewayapp.Api.response.RecordResponse;

public interface RecordApi {

    @POST("/record/")
    Call<RecordResponse> postCreate(@Body RecordRequest body);

    @GET("/record/")
    Call<List<RecordResponse>> getAll();
}
