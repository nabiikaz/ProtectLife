package com.pfe.protectlife.Repositories;

import android.util.Log;

import com.pfe.protectlife.Models.CallModel;
import com.pfe.protectlife.Services.IUserClient;
import com.pfe.protectlife.Services.retrofitClient;



import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CallRepository {

    private static CallRepository instance;
    private static final String TAG = "CallRepository";

    private  Retrofit retrofit;





    public static CallRepository getInstance(){
        if(instance == null){
            instance = new CallRepository();
        }
        return instance;

    }

    private CallRepository(){
        //this.authToken = authToken;
        //get retrofit instance with an authorization header added to it holding the authToken value
        retrofit = new retrofitClient().getRetrofit();


    }

    public void initiateCall(CallModel callModel){
        Log.d(TAG, "initiateCall: ");

        IUserClient userClient = retrofit.create(IUserClient.class);

        Call<ResponseBody> call = userClient.initiateCall(callModel);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()){

                    Log.d(TAG, "initiateCall > onResponse > successful ");

                }else{
                    Log.d(TAG, "initiateCall > onResponse > failed to initiateCAll ");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.d(TAG, "initiateCall > onResponse > failed to initiateCAll 2 ");
                t.printStackTrace();

            }
        });




    }


}
