package com.rdio.exercise.pinterestdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.rdio.exercise.pinterestdemo.Data.PinData;
import com.rdio.exercise.pinterestdemo.Data.UserData;
import com.rdio.exercise.pinterestdemo.R;
import com.rdio.exercise.pinterestdemo.Utils.ConnectivityTools;
import com.rdio.exercise.pinterestdemo.activity.PinDetailActivity;
import com.rdio.exercise.pinterestdemo.adapter.PinGridAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author mausam
 * This class performs network operation to get pin data from Pinterest API
 * and loads Pin data in a grid.
 *
 */
public class PinGridFragment extends BaseFragment {


    public static final String TAG = "PinLoad";
    private LinearLayout lnrPin = null;
    private GridView pinGrid = null;
    private TextView txtUser = null, txtNoPin = null;
    private ArrayList<PinData> pinList = null;
    private JsonObjectRequest jsonRequest = null;
    private RequestQueue mRequestQueue = null;
    private String URL = null;
    private String username = "";
    private UserData userData = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        username = getArguments().getString("username");
        URL = getActivity().getString(R.string.url).replace("@username", username);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getContext()));

        // Instantiate the RequestQueue.
        mRequestQueue = Volley.newRequestQueue(getActivity());


        username = getArguments().getString("username");
        URL = getActivity().getString(R.string.url).replace("@username", username);

        // Request a json response from the provided URL using volley library
        jsonRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            if (response.getInt("code") != 0) {
                                showMessage(response.getString("message"));
                            } else {
                                loadPins(response.getJSONObject("data"));
                            }

                            dismissProgressDialog();
                        } catch (JSONException e) {
                            dismissProgressDialog();
                            showMessage(getString(R.string.json_parsing_error));

                        } catch (Exception e) {
                            dismissProgressDialog();
                            showMessage(e.getMessage());
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissProgressDialog();

                        String message;

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            message = getString(R.string.error_network_timeout);
                        } else if (error instanceof NetworkError) {
                            message = getString(R.string.network_error);
                        } else if (error.networkResponse.statusCode == 404) {
                            message = getString(R.string.user_not_found);
                        } else {
                            message = getString(R.string.server_error);
                        }


                        showMessage(message);


                    }
                });

        // Set the tag on the request.
        jsonRequest.setTag(TAG);


        View view = inflater.inflate(R.layout.fragment_pingrid, container, false);
        lnrPin = (LinearLayout) view.findViewById(R.id.lnrPin);
        txtUser = (TextView) view.findViewById(R.id.txtUser);
        pinGrid = (GridView) view.findViewById(R.id.gridView);
        txtNoPin = (TextView) view.findViewById(R.id.txtNoPin);

        pinList = new ArrayList<PinData>();
        pinGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), PinDetailActivity.class);
                intent.putExtra("userData", userData);
                intent.putExtra("pinData", pinList.get(position));
                startActivity(intent);
            }
        });

        //Checks internet connection availability
        if (ConnectivityTools.isNetworkAvailable(getActivity())) {
            initLoading();
        } else {
            showMessage(getString(R.string.no_internet));

        }

        return view;
    }

    //Initiate Data loading process
    private void initLoading() {

        pinList = new ArrayList<PinData>();
        pinGrid.setAdapter(new PinGridAdapter(getActivity(), pinList));
        txtNoPin.setVisibility(View.GONE);
        lnrPin.setVisibility(View.GONE);
        showProgressDialog();
        //Add the request to the RequestQueue.
        mRequestQueue.add(jsonRequest);


    }

    //parse json data and set grid adapter
    private void loadPins(JSONObject jsonObj) throws Exception {

        try {

            JSONObject userObj = jsonObj.getJSONObject("user");
            JSONArray pinArray = jsonObj.getJSONArray("pins");

            userData = new UserData();
            userData.setUserId(userObj.getString("id"));
            userData.setUsername(userObj.getString("full_name"));
            userData.setPinCount(userObj.getInt("pin_count"));
            userData.setProfileImageURL(userObj.getString("image_small_url"));

            if (userObj.getInt("pin_count") <= 0) {
                txtNoPin.setVisibility(View.VISIBLE);
                lnrPin.setVisibility(View.GONE);
            } else {
                txtUser.setText(userData.getUsername()+" ("+userData.getPinCount()+" Pins)");
                txtNoPin.setVisibility(View.GONE);
                lnrPin.setVisibility(View.VISIBLE);
                for (int i = 0; i < pinArray.length(); i++) {

                    JSONObject pinObj = pinArray.getJSONObject(i);
                    PinData pindata = new PinData();
                    pindata.setDescription(pinObj.getString("description"));
                    pindata.setPinId(pinObj.getString("id"));
                    JSONObject imgObj = pinObj.getJSONObject("images").getJSONObject("237x");
                    pindata.setImgURL(imgObj.getString("url"));
                    pindata.setHeight(imgObj.getInt("height"));
                    pindata.setWidth(imgObj.getInt("width"));

                    pinList.add(pindata);
                }

                pinGrid.setAdapter(new PinGridAdapter(getActivity(), pinList));
            }

        } catch (JSONException e) {
            throw new Exception(getString(R.string.json_parsing_error));

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }


    }



    @Override
    public void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            //Cancel all the requests that have this tag.
            mRequestQueue.cancelAll(TAG);
        }
    }


}
