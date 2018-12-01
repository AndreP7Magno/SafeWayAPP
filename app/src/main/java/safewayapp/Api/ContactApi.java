package safewayapp.Api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import safewayapp.Api.request.ContactRequest;
import safewayapp.Api.response.ContactResponse;

public interface ContactApi {

    @POST("/contact/")
    Call<ContactResponse> postCreate(@Body ContactRequest body);

}
