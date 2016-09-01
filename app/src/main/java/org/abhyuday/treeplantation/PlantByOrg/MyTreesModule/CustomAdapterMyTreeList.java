package org.abhyuday.treeplantation.PlantByOrg.MyTreesModule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.abhyuday.treeplantation.R;
import org.abhyuday.treeplantation.PlantByOrg.models.PlantedTreeModel;

import java.util.ArrayList;

public class CustomAdapterMyTreeList extends ArrayAdapter<PlantedTreeModel> {

    CustomAdapterMyTreeList(Context context, ArrayList<PlantedTreeModel> plantedTreeList) {
        super(context, R.layout.custom_view_my_tree_rows,plantedTreeList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater customTreeListInflater = LayoutInflater.from(getContext());
        View customTreeListView = customTreeListInflater.inflate(R.layout.custom_view_my_tree_rows,parent, false);

        PlantedTreeModel singlePlantedTree = getItem(position);

        TextView plantedTreeName = (TextView) customTreeListView.findViewById(R.id.plantedTreeName);
        TextView treeName = (TextView) customTreeListView.findViewById(R.id.treeName);
        TextView treeGenus = (TextView) customTreeListView.findViewById(R.id.treeGenus);
        ImageView treeImageCustomView = (ImageView) customTreeListView.findViewById(R.id.treeImageCustomView);

        plantedTreeName.setText(singlePlantedTree.getPT_Name());
        treeName.setText(singlePlantedTree.getT_Name());
        treeGenus.setText(singlePlantedTree.getT_source());
        treeImageCustomView.setImageResource(R.mipmap.icon_tree);

        return customTreeListView;

    }
}
