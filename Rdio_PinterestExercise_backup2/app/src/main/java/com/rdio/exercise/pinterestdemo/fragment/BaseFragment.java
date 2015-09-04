package com.rdio.exercise.pinterestdemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.rdio.exercise.pinterestdemo.views.MyProgressDialog;

/**
 * @author mausam
 *         Base Class for Pin Fragment which contains utils method to be inherrited
 */

public class BaseFragment extends Fragment {


    protected MyProgressDialog progressDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    //Display custom progress bar
    protected void showProgressDialog() {
        progressDialog = new MyProgressDialog(getActivity());
    }


    //Dismiss custom progress bar
    protected void dismissProgressDialog() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    //To Display Message in Toast
    public void showMessage(final String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

    }


}
