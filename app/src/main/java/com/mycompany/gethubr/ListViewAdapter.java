
package com.mycompany.gethubr;


import java.util.ArrayList;
import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import com.squareup.picasso.Picasso;



public class ListViewAdapter extends ArrayAdapter <ListItem> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList <ListItem> mListData = new ArrayList <ListItem>();

    public ListViewAdapter(Context mContext, int layoutResourceId, ArrayList<ListItem> mListData) {
       
		
		super(mContext, layoutResourceId, mListData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mListData = mListData;
    }

    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
	 
    public void setListData(ArrayList<ListItem> mListData) {
        this.mListData = mListData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.list_item_title);
            holder.imageView = (ImageView) row.findViewById(R.id.list_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ListItem item = mListData.get(position);
        holder.titleTextView.setText(item.getName());

        Picasso.with(mContext).load(item.getImage()).transform(new CircleTransform()).into(holder.imageView);
        return row;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }
}
