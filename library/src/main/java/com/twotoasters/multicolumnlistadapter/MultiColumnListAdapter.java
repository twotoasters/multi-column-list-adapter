package com.twotoasters.multicolumnlistadapter;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;

import com.twotoasters.multicolumnlistadapter.MultiColumnListAdapter.MultiColumnListViewHolder;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * This class serves as a cursor adapter for a listview that is simulating a gridview. The advantage
 * of using this is that you can add headers and footers to your grid-looking listview.
 *
 * In order to accomplish the desired effect for your horizontal and vertical grid item spacing, be
 * sure to check out the getGridItemLayoutParams(int) method comments.
 */
public abstract class MultiColumnListAdapter<V extends MultiColumnListViewHolder> extends CursorAdapter {

    private static final String TAG = MultiColumnListAdapter.class.getSimpleName();

    protected LayoutInflater inflater;
    protected int numColumns = 1;
    protected int gridItemHorizontalSpacing = 0;
    protected int gridItemVerticalSpacing = 0;

    public MultiColumnListAdapter(Context context, Cursor cursor, int numColumnsResId, int horizontalSpacingResId, int verticalSpacingResId) {
        super(context, cursor, 0);
        inflater = LayoutInflater.from(context);

        Resources res = context.getResources();
        if (numColumnsResId > 0) {
            numColumns = res.getInteger(numColumnsResId);
        }

        if (horizontalSpacingResId > 0) {
            gridItemHorizontalSpacing = (int) res.getDimension(horizontalSpacingResId);
        }

        if (verticalSpacingResId > 0) {
            gridItemVerticalSpacing = (int) res.getDimension(verticalSpacingResId);
        }
    }



    /**
     * Create a new row view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // create a row view
        LinearLayout row = new LinearLayout(context);
        row.setWeightSum(numColumns);

        V[] gridItemViewHolders = newGridItemViewHolderArray(numColumns);

        // inflate a grid item view for each column in the row
        for (int col = 0; col < numColumns; col++) {
            View gridItemView = newGridItemView(context, cursor, parent);
            LinearLayout.LayoutParams lp = newGridItemLayoutParams(col);

            gridItemViewHolders[col] = newGridItemViewHolder(gridItemView);
            row.addView(gridItemView, lp);
        }

        // save the grid item view holders in the row view holder
        RowViewHolder holder = new RowViewHolder(gridItemViewHolders);
        row.setTag(holder);
        return row;
    }

    /**
     * Bind data to an existing row view.
     */
    @Override
    public void bindView(View convertView, final Context context, Cursor cursor) {
        int numGridItems = cursor.getCount();
        int listRowIndex = cursor.getPosition();

        RowViewHolder rowHolder = (RowViewHolder) convertView.getTag();
        for (int col = 0; col < numColumns; col++) {
            V gridItemViewHolder = (V) rowHolder.gridItemHolders[col];
            int gridItemIndex = listRowIndex * numColumns + col;

            if (gridItemIndex < numGridItems) {
                cursor.moveToPosition(gridItemIndex);
                setGridItemVisibility(gridItemViewHolder, true);
                updateGridItemLayoutParams(gridItemViewHolder, listRowIndex);
                bindGridItemView(gridItemViewHolder, context, cursor);
            } else {
                setGridItemVisibility(gridItemViewHolder, false);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private V[] newGridItemViewHolderArray(int numColumns) {
        Class<V> gridItemViewHolderClass = getGridItemViewHolderClass();
        return (V[]) Array.newInstance(gridItemViewHolderClass, numColumns);
    }

    private V newGridItemViewHolder(View gridItemView) {
        try {
            Class<V> gridItemViewHolderClass = getGridItemViewHolderClass();
            Constructor<V> ctor = gridItemViewHolderClass.getConstructor(View.class);
            return ctor.newInstance(gridItemView);
        } catch (InstantiationException e) {
            Log.e(TAG, "Error while instantiating new grid item view holder", e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Grid item view holder must have a public constructor", e);
        } catch (InvocationTargetException e) {
            Log.e(TAG, "Error while invoking grid item view holder constructor", e);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "Grid item view holder constructor must have public constructor with a view argument", e);
        }
        return null;
    }

    /**
     * Generate layout params for grid item view with appropriate horizontal spacing since the view never changes columns.
     */
    private LinearLayout.LayoutParams newGridItemLayoutParams(int column) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        lp.leftMargin = (column == 0 ? gridItemHorizontalSpacing : gridItemHorizontalSpacing / 2);
        lp.rightMargin = (column == numColumns - 1 ? gridItemHorizontalSpacing : gridItemHorizontalSpacing / 2);
        return lp;
    }

    /**
     * Update layout params for grid item view with appropriate vertical spacing since view re-use could change the spacing.
     */
    private void updateGridItemLayoutParams(MultiColumnListViewHolder gridItemViewHolder, int row) {
        if (gridItemViewHolder != null) {
            View gridItemView = gridItemViewHolder.getGridItemView();
            if (gridItemView != null) {
                LayoutParams lp = gridItemView.getLayoutParams();
                if (lp instanceof MarginLayoutParams) {
                    MarginLayoutParams mlp = (MarginLayoutParams) lp;
                    int numRows = getCount();
                    mlp.topMargin = (row == 0 ? gridItemVerticalSpacing : gridItemVerticalSpacing / 2);
                    mlp.bottomMargin = (row == numRows - 1 ? gridItemVerticalSpacing : gridItemVerticalSpacing / 2);
                }
            }
        }
    }

    private void setGridItemVisibility(MultiColumnListViewHolder gridItemViewHolder, boolean visible) {
        if (gridItemViewHolder != null) {
            View gridItemView = gridItemViewHolder.getGridItemView();
            if (gridItemView != null) {
                gridItemView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
            }
        }
    }

    /**
     * Returns the number of rows of grid items.
     */
    @Override
    public int getCount() {
        return (int) Math.ceil(super.getCount() / (float) numColumns);
    }

    public abstract View newGridItemView(Context context, Cursor cursor, ViewGroup parent);

    public abstract void bindGridItemView(V holder, final Context context, final Cursor cursor);

    public abstract Class<V> getGridItemViewHolderClass();

    public interface MultiColumnListViewHolder {
        View getGridItemView();
    }

    public static final class RowViewHolder {
        MultiColumnListViewHolder[] gridItemHolders;

        public RowViewHolder(MultiColumnListViewHolder[] gridItemHolders) {
            this.gridItemHolders = gridItemHolders;
        }
    }
}
