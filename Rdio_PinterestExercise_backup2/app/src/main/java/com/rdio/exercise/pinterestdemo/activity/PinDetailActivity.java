package com.rdio.exercise.pinterestdemo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.rdio.exercise.pinterestdemo.Data.PinData;
import com.rdio.exercise.pinterestdemo.Data.UserData;
import com.rdio.exercise.pinterestdemo.R;

/**
 * @author mausam
 *This class displays Pin details
 * Pin image displayed in a same size coming from the Pinterest API
 */
public class PinDetailActivity extends ActionBarActivity {

    private UserData userData=null;
    private PinData pinData =null;
    private ImageView imgPinLarge=null;
    private TextView txtDesc =null;
    private DisplayImageOptions options = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pin_detail);
        userData=(UserData) getIntent().getSerializableExtra("userData");
        pinData=(PinData)getIntent().getSerializableExtra("pinData");

        imgPinLarge=(ImageView)findViewById(R.id.imgPinLarge);
        txtDesc =(TextView)findViewById(R.id.txtDescription);

        //Initiating instance of Universal Image Loader library
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(PinDetailActivity.this));

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.img_default)
                .showImageForEmptyUri(R.drawable.img_default)
                .showImageOnFail(R.drawable.img_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        //Loading custom action bar
        initActionBar();

        if(pinData.getDescription()!=null && !pinData.getDescription().equals(""))
            txtDesc.setText(pinData.getDescription());

        // Download and display user profile image using Universal Image Loader library
        ImageLoader.getInstance().displayImage(pinData.getImgURL(),imgPinLarge);

    }

    //Customized Action bar to display user name and profile image
    private void initActionBar() {


        ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle(getString());
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.view_actionbar);
        View view = actionBar.getCustomView();

        ImageView imgUser = (ImageView) view.findViewById(R.id.imgUser);

        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);

       txtTitle.setText(userData.getUsername());
        if(userData.getProfileImageURL()!=null && !userData.getProfileImageURL().equals("")){

            // Download and display user profile image using Universal Image Loader library
            ImageLoader.getInstance().displayImage(userData.getProfileImageURL(),imgUser);
        }


    }
}
