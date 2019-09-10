package com.gogroup.app.gogroupapp.HelperClasses;

import android.util.Log;

import com.gogroup.app.gogroupapp.User.UserDashboard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zabius on 9/8/17.
 */

public class RestClient {


    //    public static String BASE_URL = "http://34.209.56.255/newwebapi/";
//    public static String BASE_URL = "http://52.37.22.174/gogroup/";
//    public static String BASE_URL = "http://52.37.22.174/gogroup_pro/";
//    public static String BASE_URL = "http://52.37.22.174/gogroup_new/";
//    public static String BASE_URL = "http://52.12.184.64/gogroup_new/";
    public static String BASE_URL = "http://40.83.217.129/";

    private static Retrofit retrofit = null;
    private static final String TAG = "RestClient";
    private static AppServices REST_CLIENT = null;

    public static AppServices get() {
        REST_CLIENT = getClient().create(AppServices.class);
        return REST_CLIENT;
    }

    static Gson gson = new GsonBuilder()
            .setLenient()
            .serializeNulls()
            .disableHtmlEscaping()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public static Retrofit getClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.MINUTES).readTimeout(60,TimeUnit.MINUTES).writeTimeout(60,TimeUnit.MINUTES);
        httpClient.addInterceptor(logging);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header("token",""+UserPreferences.getInstance().getToken());
                Log.d("token user",""+ UserPreferences.getInstance().getToken());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });


        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }
}
