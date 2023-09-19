package com.example.memotion.home.get.emotions;

import static com.example.memotion.RetrofitClient.errorParsing;
import static com.example.memotion.RetrofitClient.getClient;

import android.util.Log;

import com.example.memotion.RetrofitClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetEmotionsService {

    private GetEmotionsResult emotionsResult;

    public void setGetEmotionsResult(GetEmotionsResult getEmotionsResult) {
        this.emotionsResult = getEmotionsResult;
    }

    // 여기서 date -> 연/월
    public void getEmotions(String date) {
        GetEmotionsRetrofitInterface getEmotionsService = getClient().create(GetEmotionsRetrofitInterface.class);
        getEmotionsService.getEmotions(date).enqueue(new Callback<GetEmotionsResponse>() {
            @Override
            public void onResponse(Call<GetEmotionsResponse> call, Response<GetEmotionsResponse> response) {
                Log.d("GET-EMOTIONS-SUCCESS", response.toString());

                if(response.isSuccessful()) {
                    if(response.body().getCode() == 1000) {
                        emotionsResult.getEmotionsSuccess(response.body().getCode(), response.body().getResult());
                    }
                } else {
                    //400이상 에러시 response.body가 null로 처리됨. 따라서 errorBody로 받아야함.
                    try {
                        String errorBody = response.errorBody().string();
                        RetrofitClient.ErrorResponse errorResponse = errorParsing(errorBody);

                        Log.d("ErrorBody : ", errorBody);
                        Log.d("errorCode: ", String.valueOf(errorResponse.getCode()));
                        Log.d("errorMessage: ", errorResponse.getMessage()!=null && !errorResponse.getMessage().isEmpty() ? errorResponse.getMessage() : " ");

                        switch (errorResponse.getCode()) {
                            case 500:
                            case 2001:
                                // 어진이한테 에러처리 물어보고 추가하기
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetEmotionsResponse> call, Throwable t) {
                Log.d("GET-EMOTIONS-FAILURE", t.getMessage());
            }
        });
    }
}
