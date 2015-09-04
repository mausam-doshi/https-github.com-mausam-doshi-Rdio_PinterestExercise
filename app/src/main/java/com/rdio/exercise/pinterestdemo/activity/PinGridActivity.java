package com.rdio.exercise.pinterestdemo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rdio.exercise.pinterestdemo.R;
import com.rdio.exercise.pinterestdemo.fragment.PinGridFragment;

/**
 * @author mausam
 *This class loads Pins of user using Pinterest public api
 * Pin image displayed in a same size coming from the Pinterest API
 */
public class PinGridActivity extends ActionBarActivity {

    private EditText edtUserName=null;
    private Button btnLoadPin=null;
    private boolean  isAdded=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pingrid);



        edtUserName = (EditText)findViewById(R.id.edtUserName);
        btnLoadPin=(Button)findViewById(R.id.btnLoadPin);



        btnLoadPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtUserName.getText()== null || edtUserName.getText().toString().equals("")){
                    Toast.makeText(PinGridActivity.this,getString(R.string.error_enter_username),Toast.LENGTH_LONG).show();
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putString("username", edtUserName.getText().toString().trim());

                    Fragment gridFragment = new PinGridFragment();

                    gridFragment.setArguments(bundle);
                    addFragment(gridFragment);
                }
            }
        });




    }

    //Adding Pin Grid fragment in the activity
    private void addFragment(Fragment fragment) {

        FragmentManager fm = getSupportFragmentManager();
        if(!isAdded){

            fm.beginTransaction().add(R.id.fgmGridLayout, fragment).commit();
            isAdded=true;
        }else{
            fm.beginTransaction().replace(R.id.fgmGridLayout, fragment).commit();
        }

    }




}
