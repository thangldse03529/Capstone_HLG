package com.example.deathdevilt_t.googlemaps_testv1.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.deathdevilt_t.googlemaps_testv1.ObjectClass.MarkerObject;
import com.example.deathdevilt_t.googlemaps_testv1.R;
import com.example.deathdevilt_t.googlemaps_testv1.Retrofit.ErrorEvent;
import com.example.deathdevilt_t.googlemaps_testv1.Retrofit.RetrofitNode.BusProvider_Node;
import com.example.deathdevilt_t.googlemaps_testv1.Retrofit.RetrofitNode.Communicator_Node;
import com.example.deathdevilt_t.googlemaps_testv1.Retrofit.RetrofitNode.Model.Node;
import com.example.deathdevilt_t.googlemaps_testv1.Retrofit.RetrofitNode.ServerEvent_Node;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;


public class MapFragment extends Fragment implements OnMapReadyCallback{

//    private MapView mapView;
    private Context context;
    private View view;
    private GoogleMap mMap;
    private LatLng near1,near2,specialPoint,nearTest;
    private ArrayList<MarkerObject> markerName;
    private MarkerObject markerObject,markerObject1;
    private Marker marker;
    private MapView mapView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private SupportMapFragment supportMapFragment;
    private FrameLayout mFrameLayout;
    private Communicator_Node communicator;
    private ArrayList<Node> nodes;
    private String id_token;
    private SigninActivity signinActivity;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        context = view.getContext();
//        mapView = (MapView) view.findViewById(R.id.google_map);
//        mapView.getMapAsync(this);
        supportMapFragment = SupportMapFragment.newInstance();
        supportMapFragment.getMapAsync(this);
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.google_map, supportMapFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        usePost("1");
        initGoogleMap();
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.newLatLng(specialPoint));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker arg0) {
                for (MarkerObject mk:markerName
                        ) {
                    if (arg0.getTitle().equals("Near1!")) { // if marker source is clicked
                        Toast.makeText(context, arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                    } else if (arg0.getTitle().equals("Near2!")) {
                        Toast.makeText(context, arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                    } else if (arg0.getTitle().equals(mk.getTittle())) {
                        Toast.makeText(context, "test " + arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                    }
                }

                return true;
            }

        });
    }

    private void initGoogleMap(){
        markerName = new ArrayList<MarkerObject>();


//
//        for (Node node:communicator.nodeArrayList
//             ) {
//            markerObject = new MarkerObject();
//            LatLng pointOnce = new LatLng(Double.parseDouble(node.getLat().toString()),Double.parseDouble(node.getLng().toString()));
//            markerObject.setPosition(pointOnce);
//            markerObject.setTittle(node.getDescription());
//            markerName.add(markerObject);
//        }
//        Log.d("fuck", "fuck");
//        near1 = new LatLng(21.013377, 105.526684);
//        near2 = new LatLng(21.013362, 105.526676);
//        nearTest = new LatLng(21.013390, 105.526680);
        specialPoint = new LatLng(21.013342, 105.525930);
//        String tittleTest = "NodeTEst";
//        String Node1="Node 1";
//        String Node2="Node 2";

//        markerObject.setPosition(specialPoint);
//        markerObject.setTittle(tittleTest);

//        markerName.add(markerObject);
//
//        markerObject1 = new MarkerObject();
//        markerObject1.setPosition(near1);
//        markerObject1.setTittle(Node1);
       // markerName.add(markerObject1);


    }
    @Override
    public void onResume(){
        super.onResume();
        BusProvider_Node.getInstance().register(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        BusProvider_Node.getInstance().unregister(this);
    }
    private void usePost(String version){
        id_token = signinActivity.IdToken;
        Log.d("fuck", version);
        Log.d("fuck", "deo hieu");
        Log.d("fuck id",id_token);

        communicator = new Communicator_Node();
        communicator.loginPostNode(id_token,version);

    }
    @Subscribe
    public void onServerEventNode(ServerEvent_Node serverEvent_node){
        Log.d("fuck1233",serverEvent_node.getServerResponse().getCurrentVersion().toString());
         for (Node node:serverEvent_node.getServerResponse().getNodes()
             ) {
            markerObject = new MarkerObject();
            LatLng pointOnce = new LatLng(Double.parseDouble(node.getLat().toString()),Double.parseDouble(node.getLng().toString()));
             Log.d("fuck Marker",pointOnce.toString());
            markerObject.setPosition(pointOnce);
            markerObject.setTittle(node.getDescription());
            markerName.add(markerObject);
        }
        for (final MarkerObject mk:markerName
                ) {
            MarkerOptions userMarker = new MarkerOptions().position(mk.getPosition()).title(mk.getTittle()).icon(BitmapDescriptorFactory.fromResource(R.drawable.water_pump_icon2));
            mMap.addMarker(userMarker);

        }


    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        Toast.makeText(getContext(),""+errorEvent.getErrorMsg(),Toast.LENGTH_SHORT).show();


    }
}
