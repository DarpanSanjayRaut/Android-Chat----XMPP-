package com.pleximus.pet_app.core.api;

import com.pleximus.pet_app.core.api.response.APIResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by pleximus on 21/04/17.
 */

public interface APIInteface {

    @GET("/feeds/flowers.json")
    Observable<List<APIResponse>> getUserResponse();
}
