package ru.myitschool.mte;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WikiService {

    @GET("/w/rest.php/v1/search/page")
    Call<SearchRes> find(@Query("q") String toString,@Query("limit") int i);
}
