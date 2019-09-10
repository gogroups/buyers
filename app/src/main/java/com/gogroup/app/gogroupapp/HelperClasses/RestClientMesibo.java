package com.gogroup.app.gogroupapp.HelperClasses;

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

public class RestClientMesibo {



//    public static String BASE_URL = "http://52.12.184.64/gogroup_new/";
    public static String BASE_URL = "https://mesibo.com/api/api.php";

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
