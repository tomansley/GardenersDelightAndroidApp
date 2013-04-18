package com.gdelight.widget.adapter;

import java.util.Map;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.gdelight.R;
import com.gdelight.domain.item.Item;
import com.gdelight.domain.item.ItemGroup;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class MapWindowAdapter implements InfoWindowAdapter {

	private FragmentActivity activity = null;
	private Map<Marker, ItemGroup> items = null;

	public MapWindowAdapter(FragmentActivity activity, Map<Marker, ItemGroup> items) {
		this.activity = activity;
		this.items = items;
	}

	@Override
	public View getInfoContents(Marker marker) {

		View view = activity.getLayoutInflater().inflate(R.layout.map_info_window, null);

		ItemGroup group = items.get(marker);
		
		//title (name of item)
		TextView textViewTitle = ((TextView) view.findViewById(R.id.mapInfoTitleTextView));
		
		StringBuffer title = new StringBuffer();
		int count = 0;
		for (Item item: group.getItems()) {
			title.append(item.getName());
			title.append(", ");
			count++;
			//only show up to 3 items in list
			if (count > 2) {
				break;
			}
		}

		title.delete(title.lastIndexOf(", "), title.length()); //get rid of the last comma
		if (group.getItems().size() > 2) {
			title.append("... (more)");
		}

		SpannableString titleText = new SpannableString(title);
		titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
		textViewTitle.setText(titleText);

		//is organic
		TextView textViewIsOrganic = ((TextView) view.findViewById(R.id.mapInfoOrganicTextView));
		textViewIsOrganic.setText("Organic: " + items.get(marker).getOrganic());

		//distance 
		TextView textViewDistance = ((TextView) view.findViewById(R.id.mapInfoDistanceTextView));
		textViewDistance.setText("Distance: " + items.get(marker).getReadableDistance() + " mi");

		return view;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		return null;
	}

}
