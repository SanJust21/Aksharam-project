package com.example.MuseumTicketing.Guide.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OTPService {
    @Value("${2factor.api.key}")
    private String apiKey;

    private static final  String BASE_URL = "https://2factor.in/API/V1/";

    public String sendOtp(String phoneNumber,String otpNumber){
        OkHttpClient client = new OkHttpClient();
        String templateName = "AksharamMuseum_OTP";

        String url = BASE_URL + apiKey +"/SMS" +phoneNumber+"/"+otpNumber+"/"+templateName;

        Request request = new Request.Builder()
                .url(url).get().build();

        try (Response response=client.newCall(request).execute()){
            if (response.isSuccessful()){
                return response.body().string();
            }else {
                return "Failed: "+response.code()+" _ "+response.message();
            }
        }catch (Exception e){
            e.printStackTrace();
            return "Error : "+e.getMessage();
        }
    }
}
