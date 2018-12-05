package safewayapp.Api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import safewayapp.Api.request.ContactRequest;
import safewayapp.Api.response.ContactResponse;

public interface ContactApi {

    @POST("/contact/")
    Call<ContactResponse> postContact(@Body ContactRequest body);

    @GET("/contact/{id}")
    Call<ContactResponse> getContact(@Path("id") String id);

    @DELETE("/contact/")
    Call<ContactResponse> deleteContact(@Body ContactRequest body);
}
