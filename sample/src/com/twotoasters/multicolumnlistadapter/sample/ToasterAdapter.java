package com.twotoasters.multicolumnlistadapter.sample;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.twotoasters.multicolumnlistadapter.MultiColumnListAdapter;

public class ToasterAdapter extends MultiColumnListAdapter<ToasterGridItemViewHolder> {

    private static final String TAG = ToasterAdapter.class.getSimpleName();

    protected LayoutInflater inflater;
    protected int gridItemLayoutResId;

    public ToasterAdapter(Context context, Cursor cursor,
                          int gridItemLayoutResId, int numColumnsResId, int horizontalSpacingResId) {
        super(context, cursor, numColumnsResId, horizontalSpacingResId);
        this.gridItemLayoutResId = gridItemLayoutResId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newGridItemView(Context context, Cursor cursor, ViewGroup parent) {
        View convertView = inflater.inflate(gridItemLayoutResId, parent, false);
        ToasterGridItemViewHolder holder = new ToasterGridItemViewHolder(convertView);
        convertView.setTag(holder);
        return convertView;
    }

    @Override
    public void bindGridItemView(ToasterGridItemViewHolder holder, final Context context, final Cursor cursor) {
        Resources res = context.getResources();

        final ToasterModel toasterModel = new ToasterModel();
        toasterModel.loadFromCursor(cursor);

        holder.gridItemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, toasterModel.name, Toast.LENGTH_SHORT).show();
            }
        });

        holder.name.setText(toasterModel.name);
        Picasso.with(context)
                .load(toasterModel.imageResId)
                .fit()
                .centerCrop()
                .placeholder(android.R.color.transparent)
                .into(holder.image);
    }

    @Override
    public Class<ToasterGridItemViewHolder> getGridItemViewHolderClass() {
        return ToasterGridItemViewHolder.class;
    }
}
