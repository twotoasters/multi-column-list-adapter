package main.sample;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.twotoasters.multicolumnlistadapter.MultiColumnListAdapter.MultiColumnListViewHolder;
import com.twotoasters.multicolumnlistadapter.sample.R;

/**
 * Serves as the view holder for the toaster adapter. This must be a top level
 * class so that it can be used by the type capture in the adapter declaration.
 */
public final class ToasterGridItemViewHolder implements MultiColumnListViewHolder {
    public View gridItemView;
    public ImageView image;
    public TextView name;

    public ToasterGridItemViewHolder(View view) {
        gridItemView = view;
        image = (ImageView) view.findViewById(R.id.image);
        name = (TextView) view.findViewById(R.id.name);
    }

    public View getGridItemView() {
        return gridItemView;
    }
}
