package com.example.shosho.loginapp.api;

import com.example.shosho.loginapp.model.CountryResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {
    @GET("/bins/173pr4")
    Call<CountryResponse> getitems();
}
