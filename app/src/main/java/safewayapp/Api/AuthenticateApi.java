package safewayapp.Api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import safewayapp.Api.request.LoginRequest;
import safewayapp.Api.response.LoginResponse;

public interface AuthenticateApi {

    @POST("/user/login")
    Call<LoginResponse> postLogin(@Body LoginRequest body);

    @POST("/user/create")
    Call<LoginResponse> postCreate(@Body LoginRequest body);
}
