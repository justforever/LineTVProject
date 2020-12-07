package tw.com.linetvproject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    //https://static.linetv.tw/interview/dramas-sample.json

    @GET("interview/dramas-sample.json")
    Call<DramaList> getSampleData();
}
