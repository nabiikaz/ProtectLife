package com.pfe.protectlife.Services;

import com.pfe.protectlife.Models.CallModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.POST;


public interface IUserClient {

    @POST("nouveauAppel")
    Call<ResponseBody> initiateCall(@Body CallModel callModel);






}
