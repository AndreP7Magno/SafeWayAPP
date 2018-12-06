package safewayapp.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;
import safewayapp.Api.request.ContactRequest;
import safewayapp.Api.response.ContactResponse;

public interface ContactApi {

    @POST("/contact/create")
    Call<ContactResponse> postContact(@Body ContactRequest body);

    @GET("/contact/{id}")
    Call<List<ContactResponse>> getContact(@Path("id") String id);

    @HTTP(method = "DELETE", path = "/contact/delete", hasBody = true)
    Call<ContactResponse> deleteContact(@Body ContactRequest body);
}
