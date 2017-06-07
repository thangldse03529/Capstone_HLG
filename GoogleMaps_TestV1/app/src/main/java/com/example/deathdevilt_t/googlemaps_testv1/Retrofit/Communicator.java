package com.example.deathdevilt_t.googlemaps_testv1.Retrofit;

import android.content.Context;
import android.util.Log;

import com.example.deathdevilt_t.googlemaps_testv1.ObjectClass.UserObject;
import com.example.deathdevilt_t.googlemaps_testv1.Retrofit.RetrofitNode.Model.Node;
import com.squareup.otto.Produce;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.internal.JavaNetCookieJar;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DeathDevil.T_T on 31-May-17.
 */

public class Communicator {
    private static  final String TAG = "Communicator_Node";
    private static final String SERVER_URL = "http://capstoneweb.herokuapp.com/";
    //https://caps-web-khanghn2.c9users.io/
    private UserObject userObject;
    private OkHttpClient httpClient;
    private Context ctx;
    public ArrayList<Node> nodeArrayList;

    public Communicator() {
    }

    public void loginPost(final String id_token, String status, String message){
        Log.d("fuck", id_token);

       CookieHandler cookieManager = new CookieManager();
       // CookieHandler.setDefault(cookieManager);

        //Here a logging interceptor is created
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        //The logging interceptor will be added to the http client
        httpClient = new OkHttpClient.Builder().addNetworkInterceptor(logging).cookieJar(new JavaNetCookieJar(cookieManager))
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
                .build();



        //The Retrofit builder will have the client attached, in order to get connection logs
        Retrofit.Builder retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SERVER_URL);
        final Interface service = retrofit.build().create(Interface.class);

        Call<ServerResponse> call = service.post(id_token,status,message);

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                BusProvider.getInstance().post(new ServerEvent(response.body()));

                ServerResponse serverResponse;
                serverResponse = response.body();
               // serverResponse.getStatus();
                Log.e(TAG,"Success");

//                Log.d("fuck",response.body().toString());
                Log.d("fuck",serverResponse.getStatus().toString());
//                if(serverResponse.getStatus()==true){
//                    nodeArrayList = new ArrayList<Node>();
//                    Call<NodeInformation> callNode = service.postNode("1",id_token);
//                    callNode.enqueue(new Callback<NodeInformation>() {
//                        @Override
//                        public void onResponse(Call<NodeInformation> call, Response<NodeInformation> response) {
//                            BusProvider.getInstance().post(new ServerEvent_Node(response.body()));
//                            NodeInformation nodeInformation = response.body();
//                            Log.d("fuck",nodeInformation.getCurrentVersion().toString());
//                            Log.d("fuck11",nodeInformation.toString());
//
//                            for (Node node:nodeInformation.getNodes()
//                                 ) {
//                                    nodeArrayList.add(node);
//                                Log.d("fuck1111111",node.getPhoneNumber());
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<NodeInformation> call, Throwable t) {
//
//                        }
//                    });
//                }
//                Log.d("fuck",serverResponse.getMessage().toString());
//                 userObject = new UserObject();
//                userObject.setIsLogin(serverResponse.getStatus());
//                userObject.setMessage(serverResponse.getMessage());
//                Log.d("fuck1",userObject.getIsLogin().toString());

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                // handle execution failures like no internet connectivity
                BusProvider.getInstance().post(new ErrorEvent(-2,t.getMessage()));
            }
        });
    }
    @Produce
    public ServerEvent produceServerEvent(ServerResponse serverResponse) {
        return new ServerEvent(serverResponse);
    }

    @Produce
    public ErrorEvent produceErrorEvent(int errorCode, String errorMsg) {
        return new ErrorEvent(errorCode, errorMsg);
    }


//    public void loginGet(String username, String password){
//        //Here a logging interceptor is created
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        //The logging interceptor will be added to the http client
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.addInterceptor(logging);
//
//        //The Retrofit builder will have the client attached, in order to get connection logs
//        Retrofit retrofit = new Retrofit.Builder()
//                .client(httpClient.build())
//                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl(SERVER_URL)
//                .build();
//
//        Interface_node service = retrofit.create(Interface_node.class);
//
//        Call<ServerResponse_Node> call = service.get("login",username,password);
//
//        call.enqueue(new Callback<ServerResponse_Node>() {
//            @Override
//            public void onResponse(Call<ServerResponse_Node> call, Response<ServerResponse_Node> response) {
//                BusProvider_Node.getInstance().post(new ServerEvent_Node(response.body()));
//                Log.e(TAG,"Success");
//            }
//
//            @Override
//            public void onFailure(Call<ServerResponse_Node> call, Throwable t) {
//                // handle execution failures like no internet connectivity
//                BusProvider_Node.getInstance().post(new ErrorEvent_Node(-2,t.getMessage()));
//            }
//        });
}
