package com.gogroup.app.gogroupapp.HelperClasses;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.gogroup.app.gogroupapp.CallBackListeners.CallBackApi;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.CommonResponse;
import com.gogroup.app.gogroupapp.Responses.DetailResponse;
import com.gogroup.app.gogroupapp.Responses.GroupDetailResponse;
import com.gogroup.app.gogroupapp.Responses.MesiboDetailResponse;
import com.gogroup.app.gogroupapp.Responses.PostResponse;

import java.util.Map;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by atinderpalsingh on 2/5/18.
 */

public class ApiCalls<T> {

    Context context;

    public ApiCalls(Context context) {
        this.context = context;

    }

    void retry(String title, String message, final CallBackApi<T> callBackApi) {
        callBackApi.onFailure(message);
        ((BaseActivity) context).showAlert(title, message);
        ((BaseActivity) context).alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) context).alertDialog.dismiss();
                callBackApi.onRetryYes();

            }
        });
        ((BaseActivity) context).alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) context).alertDialog.dismiss();
                callBackApi.onRetryNo();
            }
        });
    }


    public void apiGenerateCouponCode(Map<String, String> postFields, final CallBackApi<T> callBackApi) {
        if (Utils.isInterNetConnected(context)) {
            RestClient.get().generateCouponCode(postFields).
                    enqueue(new Callback<PostResponse>() {
                        @Override
                        public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    callBackApi.onSuccess((Response<T>) response);
                                } else {
                                    retry(response.body().getMessage() != null ? response.body().getMessage() : context.getString(R.string.somethingWentWrong), null, callBackApi);
                                }
                            } else {
                                retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                            }
                        }

                        @Override
                        public void onFailure(Call<PostResponse> call, Throwable t) {
                            retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                        }
                    });
        } else {
             retry(null, null, callBackApi);
        }

    }
    public void apiPurchaseAdd(Map<String, String> postFields, final CallBackApi<T> callBackApi) {
        if (Utils.isInterNetConnected(context)) {
            RestClient.get().purchaseAdd(postFields).
                    enqueue(new Callback<PostResponse>() {
                        @Override
                        public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    callBackApi.onSuccess((Response<T>) response);
                                } else {
                                    retry(response.body().getMessage() != null ? response.body().getMessage() : context.getString(R.string.somethingWentWrong), null, callBackApi);
                                }
                            } else {
                                retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                            }
                        }

                        @Override
                        public void onFailure(Call<PostResponse> call, Throwable t) {
                            retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                        }
                    });
        } else {
             retry(null, null, callBackApi);
        }

    }
    public void apiValidateOffer(Map<String, String> postFields, final CallBackApi<T> callBackApi) {
        if (Utils.isInterNetConnected(context)) {
            RestClient.get().validateOffer(postFields).
                    enqueue(new Callback<PostResponse>() {
                        @Override
                        public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    callBackApi.onSuccess((Response<T>) response);
                                } else {
                                    retry(response.body().getMessage() != null ? response.body().getMessage() : context.getString(R.string.somethingWentWrong), null, callBackApi);
                                }
                            } else {
                                retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                            }
                        }

                        @Override
                        public void onFailure(Call<PostResponse> call, Throwable t) {
                            retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                        }
                    });
        } else {
             retry(null, null, callBackApi);
        }

    }
    public void apiSetLike(Map<String, String> postFields, final CallBackApi<T> callBackApi) {
        if (Utils.isInterNetConnected(context)) {
            RestClient.get().setLike(postFields).
                    enqueue(new Callback<PostResponse>() {
                        @Override
                        public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    callBackApi.onSuccess((Response<T>) response);
                                } else {
                                    retry(response.body().getMessage() != null ? response.body().getMessage() : context.getString(R.string.somethingWentWrong), null, callBackApi);
                                }
                            } else {
                                retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                            }
                        }

                        @Override
                        public void onFailure(Call<PostResponse> call, Throwable t) {
                            retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                        }
                    });
        } else {
             retry(null, null, callBackApi);
        }

    }
    public void apiInsertRating(Map<String, String> postFields, final CallBackApi<T> callBackApi) {
        if (Utils.isInterNetConnected(context)) {
            RestClient.get().insertRating(postFields).
                    enqueue(new Callback<PostResponse>() {
                        @Override
                        public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    callBackApi.onSuccess((Response<T>) response);
                                } else {
                                    retry(response.body().getMessage() != null ? response.body().getMessage() : context.getString(R.string.somethingWentWrong), null, callBackApi);
                                }
                            } else {
                                retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                            }
                        }

                        @Override
                        public void onFailure(Call<PostResponse> call, Throwable t) {
                            retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                        }
                    });
        } else {
             retry(null, null, callBackApi);
        }

    }
    public void apiGetPurchaseList(Map<String, String> postFields, final CallBackApi<T> callBackApi) {
        if (Utils.isInterNetConnected(context)) {
            RestClient.get().getPurchaseList(postFields).
                    enqueue(new Callback<DetailResponse>() {
                        @Override
                        public void onResponse(Call<DetailResponse> call, Response<DetailResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    callBackApi.onSuccess((Response<T>) response);
                                } else {
                                    retry(response.body().getMessage() != null ? response.body().getMessage() : context.getString(R.string.somethingWentWrong), null, callBackApi);
                                }
                            } else {
                                retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                            }
                        }

                        @Override
                        public void onFailure(Call<DetailResponse> call, Throwable t) {
                            retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                        }
                    });
        } else {
             retry(null, null, callBackApi);
        }

    }

    public void apiGetPendingUsers(Map<String, String> postFields, final CallBackApi<T> callBackApi) {
        if (Utils.isInterNetConnected(context)) {
            RestClient.get().getPendingUser(postFields).
                    enqueue(new Callback<DetailResponse>() {
                        @Override
                        public void onResponse(Call<DetailResponse> call, Response<DetailResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    callBackApi.onSuccess((Response<T>) response);
                                } else {
                                    retry(response.body().getMessage() != null ? response.body().getMessage() : context.getString(R.string.somethingWentWrong), null, callBackApi);
                                }
                            } else {
                                retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                            }
                        }

                        @Override
                        public void onFailure(Call<DetailResponse> call, Throwable t) {
                            retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                        }
                    });
        } else {
             retry(null, null, callBackApi);
        }

    }
    public void apiGetPurchasedUsers(Map<String, String> postFields, final CallBackApi<T> callBackApi) {
        if (Utils.isInterNetConnected(context)) {
            RestClient.get().getPurchasedUser(postFields).
                    enqueue(new Callback<DetailResponse>() {
                        @Override
                        public void onResponse(Call<DetailResponse> call, Response<DetailResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    callBackApi.onSuccess((Response<T>) response);
                                } else {
                                    retry(response.body().getMessage() != null ? response.body().getMessage() : context.getString(R.string.somethingWentWrong), null, callBackApi);
                                }
                            } else {
                                retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                            }
                        }

                        @Override
                        public void onFailure(Call<DetailResponse> call, Throwable t) {
                            retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                        }
                    });
        } else {
             retry(null, null, callBackApi);
        }

    }
    public void apiGetSellerFeed(Map<String, String> postFields, final CallBackApi<T> callBackApi) {
        if (Utils.isInterNetConnected(context)) {
            RestClient.get().getSellerFeed(postFields).
                    enqueue(new Callback<DetailResponse>() {
                        @Override
                        public void onResponse(Call<DetailResponse> call, Response<DetailResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    callBackApi.onSuccess((Response<T>) response);
                                } else {
                                    retry(response.body().getMessage() != null ? response.body().getMessage() : context.getString(R.string.somethingWentWrong), null, callBackApi);
                                }
                            } else {
                                retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                            }
                        }

                        @Override
                        public void onFailure(Call<DetailResponse> call, Throwable t) {
                            retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                        }
                    });
        } else {
             retry(null, null, callBackApi);
        }

    }
    public void apiGetGroupDetails(Map<String, String> postFields, final CallBackApi<T> callBackApi) {
        if (Utils.isInterNetConnected(context)) {
            RestClient.get().getGroupDetailsNew(postFields).
                    enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    callBackApi.onSuccess((Response<T>) response);
                                } else {
                                    retry(response.body().getMessage() != null ? response.body().getMessage() : context.getString(R.string.somethingWentWrong), null, callBackApi);
                                }
                            } else {
                                retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                            }
                        }

                        @Override
                        public void onFailure(Call<CommonResponse> call, Throwable t) {
                            retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                        }
                    });
        } else {
             retry(null, null, callBackApi);
        }

    }
    public void apiMesiboUserAdd(Map<String, String> postFields, final CallBackApi<T> callBackApi) {
        if (Utils.isInterNetConnected(context)) {
            RestClient.get().mesiboUserAdd(postFields).
                    enqueue(new Callback<MesiboDetailResponse>() {
                        @Override
                        public void onResponse(Call<MesiboDetailResponse> call, Response<MesiboDetailResponse> response) {
                            if (response.body() != null) {
                                if (response.body().getResult()) {
                                    callBackApi.onSuccess((Response<T>) response);
                                } else {

//                                    retry(response.body().getMessage() != null ? response.body().getMessage() : context.getString(R.string.somethingWentWrong), null, callBackApi);
                                    retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                                }
                            } else {
                                retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                            }
                        }

                        @Override
                        public void onFailure(Call<MesiboDetailResponse> call, Throwable t) {
                            retry(context.getString(R.string.somethingWentWrong), null, callBackApi);
                        }
                    });
        } else {
             retry(null, null, callBackApi);
        }

    }




}
