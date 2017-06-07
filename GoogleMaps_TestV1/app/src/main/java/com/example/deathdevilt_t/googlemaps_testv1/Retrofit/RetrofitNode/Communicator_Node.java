package com.example.deathdevilt_t.googlemaps_testv1.Retrofit.RetrofitNode;

import android.util.Log;

import com.example.deathdevilt_t.googlemaps_testv1.ObjectClass.UserObject;
import com.example.deathdevilt_t.googlemaps_testv1.Retrofit.RetrofitNode.Model.NodeInformation;
import com.squareup.otto.Produce;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DeathDevil.T_T on 31-May-17.
 */

public class Communicator_Node {
    private static  final String TAG = "Communicator_Node";
    private static final String SERVER_URL = "http://capstoneweb.herokuapp.com/";
    //https://caps-web-khanghn2.c9users.io/
    private UserObject userObject;

    public Communicator_Node() {
    }

    public void loginPostNode(String id_token,String version){
        //Here a logging interceptor is created
        Log.d("fuck1","testtttt");
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
       logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //The logging interceptor will be added to the http client
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        //The Retrofit builder will have the client attached, in order to get connection logs
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SERVER_URL)
                .build(); Interface_node service = retrofit.create(Interface_node.class);

        Call<NodeInformation> call = service.post(id_token,version);

        call.enqueue(new Callback<NodeInformation>() {
            @Override
            public void onResponse(Call<NodeInformation> call, Response<NodeInformation> response) {
                BusProvider_Node.getInstance().post(new ServerEvent_Node(response.body()));
                NodeInformation nodeInformation = response.body();

               // serverResponse.getStatus();
                Log.d("fuck1","Success");
                Log.d("fuck1",response.body().getStatus().toString());

//                Log.d("fuck",response.body().toString());
//                Log.d("fuck",serverResponse.getStatus().toString());
//                Log.d("fuck",serverResponse.getMessage().toString());
//                 userObject = new UserObject();
//                userObject.setIsLogin(serverResponse.getStatus());
//                userObject.setMessage(serverResponse.getMessage());
                Log.d("fuck1","testtttt");

            }

            @Override
            public void onFailure(Call<NodeInformation> call, Throwable t) {
                // handle execution failures like no internet connectivity
                BusProvider_Node.getInstance().post(new ErrorEvent_Node(-2,t.getMessage()));
            }
        });
    }
    @Produce
    public ServerEvent_Node produceServerEvent(NodeInformation serverResponse) {
        return new ServerEvent_Node(serverResponse);
    }

    @Produce
    public ErrorEvent_Node produceErrorEvent(int errorCode, String errorMsg) {
        return new ErrorEvent_Node(errorCode, errorMsg);
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
