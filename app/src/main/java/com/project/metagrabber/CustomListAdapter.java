package com.project.metagrabber;

/**
 * Created by AshishKenjale on 23/4/15.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class CustomListAdapter extends BaseAdapter {
    customButtonListener customListner;

    public interface customButtonListener {
        public void onButtonClickListner(int position,String value);
        public void onButtonClickListner2(int position,String value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    private static final String TAG = "CustomListAdaptor";
    private Activity activity;
    private LayoutInflater inflater;
    private List<LinkPreview> linkItems;

    public CustomListAdapter(Activity activity, List<LinkPreview> linkItems) {
        this.activity = activity;
        this.linkItems = linkItems;
    }
    //@Override
    public void add(LinkPreview object) {
        linkItems.add(object);
        //super.add(object);
    }

    @Override
    public int getCount() {
        return linkItems.size();
    }

    @Override
    public Object getItem(int location) {
        return linkItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item_card, null);

        viewHolder = new ViewHolder();

        viewHolder.thumbNail = (ImageView) convertView.findViewById(R.id.imageView);
        viewHolder.title = (TextView) convertView.findViewById(R.id.txtTitle);
        viewHolder.desc = (TextView) convertView.findViewById(R.id.txtDesc);
        viewHolder.btn_viewLink = (Button) convertView.findViewById(R.id.btnViewLink);
        viewHolder.btn_likeLink = (Button) convertView.findViewById(R.id.btnLikeLink);

        // getting link data for the row
        LinkPreview m = linkItems.get(position);

        viewHolder.btn_viewLink.setTag(m.getUrl());
        viewHolder.btn_viewLink.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                if (customListner != null) {
                                                      customListner.onButtonClickListner(position, linkItems.get(position).getUrl());
                                                }
                                            }
                                        }
        );

        viewHolder.btn_likeLink.setOnClickListener(new View.OnClickListener() {
                                                       public void onClick(View v) {
                                                           if (customListner != null) {
                                                               customListner.onButtonClickListner2(position, linkItems.get(position).getTitle());
                                                           }
                                                       }
                                                   }
        );

       // title
        viewHolder.title.setText(m.getTitle());

        // description
        viewHolder.desc.setText(m.getDesc());

        // thumbnail image
        if (m.getImage() != null) {
            viewHolder.thumbNail.setImageBitmap(m.getImage());
        } else {
            // MY DEFAULT IMAGE
            viewHolder.thumbNail.setImageResource(R.drawable.ic_launcher);
        }

        return convertView;
    }

    public class ViewHolder {
        ImageView thumbNail;
        TextView title;
        TextView desc;
        Button btn_viewLink;
        Button btn_likeLink;
    }
}