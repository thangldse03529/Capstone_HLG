package com.example.deathdevilt_t.googlemaps_testv1.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.deathdevilt_t.googlemaps_testv1.ObjectClass.NodeObject;
import com.example.deathdevilt_t.googlemaps_testv1.ObjectClass.UserObject;
import com.example.deathdevilt_t.googlemaps_testv1.R;
import com.example.deathdevilt_t.googlemaps_testv1.Retrofit.BusProvider;
import com.example.deathdevilt_t.googlemaps_testv1.Retrofit.Communicator;
import com.example.deathdevilt_t.googlemaps_testv1.Retrofit.ErrorEvent;
import com.example.deathdevilt_t.googlemaps_testv1.Retrofit.ServerEvent;
import com.example.deathdevilt_t.googlemaps_testv1.SendGetDataFromWebApi.SendPostRequestWebServer;
import com.example.deathdevilt_t.googlemaps_testv1.fragment.FragmentDialogAccessError;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * Created by DeathDevil.T_T on 15-May-17.
 */

public class SigninActivity extends Activity implements View.OnClickListener{
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInAccount acct;
    private SignInButton btnSignIn;
    private ArrayList<NodeObject> listNode;
    private UserObject userObject = new UserObject();
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    public static String email,UriImage;
    public static String IdToken="";
    private int checkKey=0;
    private Communicator communicator;
    private Boolean check_Status;

    private SendPostRequestWebServer mSendPostRequestWebServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_google_account);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(SigninActivity.this).addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();
      // AddNodeTest();
        init();

    }
    private void AddNodeTest(){
        listNode = new ArrayList<NodeObject>() ;
        NodeObject nodeObject = new NodeObject();
        //new LatLng(21.013377, 105.526684);
        nodeObject.setPosition(new LatLng(21.013377, 105.526684));
        nodeObject.setDelete(false);
        nodeObject.setDescription("adas");
        nodeObject.setPhone("1234566");
        nodeObject.setVersion("1.0");
        listNode.add(nodeObject);

    }
    private void init(){
        btnSignIn = (SignInButton) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);

    }
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onClick(View v) {
        //  userObject.setIsLogin("3");

        switch (v.getId()) {
            case R.id.btnSignIn:
                if(isNetworkConnected()==false){
                    showAccessError();
                }

                NodeObject nodeObject = new NodeObject();
                if (listNode == null) { //check xem da co node nao chua

                    signIn();


                    //new SendPostRequestWebServer().execute("https://caps-web-khanghn2.c9users.io/api/auth/idtoken");
                    //new ReceiveDataFromUrl().execute("https://caps-web-khanghn2.c9users.io");
//                Log.d("BugDmm",userObject.getIsLogin());

//                    if (userObject.getIsLogin()) {// check xem co quyen dang nhap khong
//                        ArrayList<NodeObject> nObject = new ArrayList<NodeObject>();
//
//                        if(nObject==null){
//
//                        }
//
//                    } else {
//                        showAccessError();
//                    }
//                }else {
//                    Intent intent = new Intent(SigninActivity.this, NaviMapActivity.class);
////                    Bundle bundleUriImage = new Bundle();
////                    bundleUriImage.putString("UriImage",UriImage);
////                    bundleUriImage.putString("Name",acct.getDisplayName().toString());
////                    bundleUriImage.putString("EmailAddress",acct.getEmail().toString());
////                    intent.putExtras(bundleUriImage);
////                    Log.d("bugUI",acct.getPhotoUrl().toString());
//                    startActivity(intent);
//                }

                }else if(listNode !=null) {
                    Intent intent = new Intent(SigninActivity.this, NaviMapActivity.class);
                    startActivity(intent);
                }else {
                    showAccessError();
            }

                    break;

            default:
                showAccessError();
                break;
        }
    }
    private boolean isNetworkConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    private void showAccessError() {

        new FragmentDialogAccessError().show(getFragmentManager(),"Fragment Access Error");

    }
    private void signIn() {
       Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            acct = result.getSignInAccount();
            UriImage = acct.getPhotoUrl().toString();

            email = acct.getEmail();
            IdToken=acct.getIdToken();

            if(!IdToken.equals("")){
                Log.d("email: ",acct.getIdToken());
                usePost(acct.getIdToken());
               // usePost(IdToken);

                checkKey=1;
            }else {
                Log.d("email: ",acct.getEmail());

            }
//            Intent intent = new Intent(SigninActivity.this, NaviMapActivity.class);
//            Bundle bundleUriImage = new Bundle();
//            bundleUriImage.putString("UriImage",UriImage);
//            bundleUriImage.putString("Name",acct.getDisplayName().toString());
//            bundleUriImage.putString("EmailAddress",acct.getEmail().toString());
//            intent.putExtras(bundleUriImage);
//            Log.d("bugUI",acct.getPhotoUrl().toString());
//            startActivity(intent);
            //.
            // IdToken =  acct.getIdToken().toString();

            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.btnSignIn).setVisibility(View.GONE);
        } else {
            findViewById(R.id.btnSignIn).setVisibility(View.VISIBLE);
        }
    }
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        // updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    @Override
    public void onResume(){
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }
    private void usePost(String id_token){
        Log.d("fuck", id_token);
        communicator = new Communicator();
        communicator.loginPost(id_token,"","");

    }
    @Subscribe
    public void onServerEvent(ServerEvent serverEvent){
        try {
            Toast.makeText(this, ""+serverEvent.getServerResponse().getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("fuck123", serverEvent.getServerResponse().getStatus().toString());
            check_Status = serverEvent.getServerResponse().getStatus();


            if (check_Status==true){
                Intent intent = new Intent(SigninActivity.this, NaviMapActivity.class);
                Bundle bundleUriImage = new Bundle();
                bundleUriImage.putString("UriImage",UriImage);
                bundleUriImage.putString("Name",acct.getDisplayName().toString());
                bundleUriImage.putString("EmailAddress",acct.getEmail().toString());
                intent.putExtras(bundleUriImage);
                Log.d("bugUI",acct.getPhotoUrl().toString());
                startActivity(intent);

            }else {
                showAccessError();
            }

        }catch (Exception e){
           // showAccessError();
        }



    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        Toast.makeText(this,""+errorEvent.getErrorMsg(),Toast.LENGTH_SHORT).show();
        showAccessError();
    }
}
