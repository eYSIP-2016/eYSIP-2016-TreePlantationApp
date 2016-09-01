package org.abhyuday.treeplantation.PlantByOrg.TreeListModule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.abhyuday.treeplantation.R;
import org.abhyuday.treeplantation.PlantByOrg.models.TreeModel;

import java.util.ArrayList;


public class CustomAdapterTreeLists extends ArrayAdapter<TreeModel> implements Filterable {

    private Context context;
    private ArrayList<TreeModel> TreeList;
    ViewHolder viewHolder;
    private Filter filter;

    CustomAdapterTreeLists(Context context, ArrayList<TreeModel> TreeList) {
        super(context, R.layout.custom_view_tree_rows, TreeList);
        this.context = context;
        this.TreeList= TreeList;
    }


    private class ViewHolder{
        TextView treeName;
        TextView treeSource;
        TextView treeGenus;
        ImageView treeImageCustomView;
    }

    @Override
    public int getCount() {
        return TreeList.size();
    }

    @Override
    public TreeModel getItem(int position) {
        return TreeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return TreeList.get(position).getId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater customTreeListInflater = LayoutInflater.from(getContext());
        //View customTreeListView = customTreeListInflater.inflate(R.layout.custom_view_tree_rows, parent, false);

        if(convertView==null){
            convertView = customTreeListInflater.inflate(R.layout.custom_view_tree_rows,null);
            viewHolder=new ViewHolder();
            viewHolder.treeName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.treeSource = (TextView) convertView.findViewById(R.id.tvSource);
            viewHolder.treeGenus = (TextView) convertView.findViewById(R.id.tvGenus);
            viewHolder.treeImageCustomView = (ImageView) convertView.findViewById(R.id.treeImageCustomView);

            convertView.setTag(viewHolder);

        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }

        TreeModel singleTree = getItem(position);


        viewHolder.treeName.setText(singleTree.getName());
        viewHolder.treeSource.setText(singleTree.getSource());
        viewHolder.treeGenus.setText(singleTree.getGenus());
        viewHolder.treeImageCustomView.setImageResource(R.mipmap.icon_tree);

        Picasso.with(context).load(singleTree.getImageUrl()).fit().into(viewHolder.treeImageCustomView);
        //return customTreeListView;
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new CustomSearchFilter(TreeList);
        return filter;
    }

    private class CustomSearchFilter extends Filter {
        private ArrayList<TreeModel> sourceObjects;

        public CustomSearchFilter(ArrayList<TreeModel> objects) {
            sourceObjects = new ArrayList<>();
            synchronized (this) {
                sourceObjects.addAll(objects);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence chars) {
            String filterSeq = chars.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (filterSeq != null && filterSeq.length() > 0) {
                ArrayList<TreeModel> filter = new ArrayList<TreeModel>();

                for (TreeModel object : sourceObjects) {
                    // the filtering itself:
                    if (object.getName().toLowerCase().contains(filterSeq))
                        filter.add(object);
                }
                result.count = filter.size();
                result.values = filter;
            } else {
                // add all objects
                synchronized (this) {
                    result.values = sourceObjects;
                    result.count = sourceObjects.size();
                }
            }
            return result;
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            ArrayList<TreeModel> filtered = (ArrayList<TreeModel>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filtered.size(); i < l; i++)
                add(filtered.get(i));
            notifyDataSetInvalidated();
        }
    }
}

