package com.rdio.exercise.pinterestdemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rdio.exercise.pinterestdemo.Data.PinData;
import com.rdio.exercise.pinterestdemo.R;

import java.util.ArrayList;


/**
 * @author mausam
 * Custom Adapter class for loading image in Pin Grid
 *
 */
public class PinGridAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	private ArrayList<PinData> pinList = null;

	private DisplayImageOptions options = null;


	public PinGridAdapter(Context context, ArrayList<PinData> list) {

		this.pinList = list;
		inflater = LayoutInflater.from(context);



        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.img_default)
                .showImageForEmptyUri(R.drawable.img_default)
                .showImageOnFail(R.drawable.img_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
	}

	@Override
	public int getCount() {
		return pinList.size();
	}

	@Override
	public Object getItem(int position) {
		return pinList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder mHolder;

		if (convertView == null) {

			convertView = inflater.inflate(R.layout.row_grid_image, parent, false);
			mHolder = new ViewHolder();

			mHolder.imgPin = (ImageView) convertView.findViewById(R.id.imgPin);


			convertView.setTag(mHolder);

		} else {

			mHolder = (ViewHolder) convertView.getTag();
		}


		PinData data = pinList.get(position);

        // Download and display user profile image using Universal Image Loader library
        ImageLoader.getInstance().displayImage(data.getImgURL(),mHolder.imgPin,options);




		return convertView;
	}
	private static class ViewHolder {

		ImageView imgPin;

	}
}
