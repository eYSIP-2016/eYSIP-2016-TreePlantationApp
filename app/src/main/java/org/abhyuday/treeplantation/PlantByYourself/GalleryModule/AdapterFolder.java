package org.abhyuday.treeplantation.PlantByYourself.GalleryModule;

/**
 * Adapter for the listview in MyFolder
 * associated layout files- custom_view_my_folder
 *
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.abhyuday.treeplantation.R;

import java.util.ArrayList;

public class AdapterFolder extends ArrayAdapter<String> {
    Context currentContext;
    AdapterFolder(Context context, ArrayList<String> plantedTreeList) {
        super(context, R.layout.custom_view_my_folder,plantedTreeList);
        currentContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater customTreeListInflater = LayoutInflater.from(getContext());
        View customTreeListView = customTreeListInflater.inflate(R.layout.custom_view_my_folder,parent, false);


        String imageUrl = getItem(position);
        ImageView treeImageCustomView = (ImageView) customTreeListView.findViewById(R.id.treeImageCustomView);
        Picasso.with(currentContext).load(imageUrl).fit().into(treeImageCustomView);


        return customTreeListView;

    }
}
