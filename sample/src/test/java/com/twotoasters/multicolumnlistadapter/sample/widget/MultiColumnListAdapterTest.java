package com.twotoasters.multicolumnlistadapter.sample.widget;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.twotoasters.multicolumnlistadapter.MultiColumnListAdapter;
import com.twotoasters.multicolumnlistadapter.MultiColumnListAdapter.MultiColumnListViewHolder;
import com.twotoasters.multicolumnlistadapter.MultiColumnListAdapter.RowViewHolder;
import com.twotoasters.multicolumnlistadapter.sample.MCLARobolectricTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MCLARobolectricTestRunner.class)
public class MultiColumnListAdapterTest {

    private static final int NUM_COLUMNS = 3;

    @Mock Cursor cursor;
    TestFakeGridAdapter adapter;
    TestFakeGridAdapter spyAdapter;
    Context context;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        context = Robolectric.application;
        adapter = new TestFakeGridAdapter(context, cursor);
        spyAdapter = spy(adapter);
    }

    @Test
    public void itShouldCalculateAdapterCount() {
        int[] gridItemCounts = {0, 1, 2, 3, 4, 5, 6, 7};
        int[] listRowCounts =  {0, 1, 1, 1, 2, 2, 2, 3};

        int i = 0;
        for (int gridItemCount : gridItemCounts) {
            when(cursor.getCount()).thenReturn(gridItemCount);
            assertThat(adapter.getCount()).isEqualTo(listRowCounts[i++]);
        }
    }

    @Test
    public void itShouldBuildANewRowView() {
        when(cursor.getCount()).thenReturn(NUM_COLUMNS * 3);
        spyAdapter.newView(context, cursor, null);
        verify(spyAdapter, times(NUM_COLUMNS))
                .newGridItemView(any(Context.class), any(Cursor.class), any(ViewGroup.class));
    }

    @Test
    public void itShouldBindDataToAFullRowView() {
        int currentRow = 1;
        when(cursor.getCount()).thenReturn(NUM_COLUMNS * 2);
        when(cursor.getPosition()).thenReturn(currentRow);

        spyAdapter.bindView(newRowView(), context, cursor);
        verify(spyAdapter, times(NUM_COLUMNS))
                .bindGridItemView(any(adapter.getGridItemViewHolderClass()),
                        any(Context.class), any(Cursor.class));
    }

    @Test
    public void itShouldBindDataToAnAlmostFullRowView() {
        int currentRow = 1;
        when(cursor.getCount()).thenReturn(NUM_COLUMNS * 2 - 1);
        when(cursor.getPosition()).thenReturn(currentRow);

        spyAdapter.bindView(newRowView(), context, cursor);
        verify(spyAdapter, times(NUM_COLUMNS - 1))
                .bindGridItemView(any(adapter.getGridItemViewHolderClass()),
                        any(Context.class), any(Cursor.class));
    }

    // test helpers

    LinearLayout newRowView() {
        LinearLayout rowView = new LinearLayout(context);
        MockGridItemViewHolder[] gridItemViewHolders = new MockGridItemViewHolder[NUM_COLUMNS];
        for (MockGridItemViewHolder gridItemViewHolder : gridItemViewHolders) {
            gridItemViewHolder = new MockGridItemViewHolder(new View(context));
            gridItemViewHolder.gridItemView = new View(context);
        }
        rowView.setTag(new RowViewHolder(gridItemViewHolders));
        return rowView;
    }

    static class TestFakeGridAdapter extends MultiColumnListAdapter<MockGridItemViewHolder> {

        private TestFakeGridAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0, 0, 0);
            numColumns = NUM_COLUMNS;
        }

        @Override
        public View newGridItemView(Context context, Cursor cursor, ViewGroup parent) {
            return new android.view.View(context);
        }

        @Override
        public void bindGridItemView(MockGridItemViewHolder holder, final Context context, final Cursor cursor) { }

        @Override
        public Class<MockGridItemViewHolder> getGridItemViewHolderClass() {
            return MockGridItemViewHolder.class;
        }
    }

    static class MockGridItemViewHolder implements MultiColumnListViewHolder {
        private View gridItemView;

        public MockGridItemViewHolder(View convertView) {
            gridItemView = convertView;
        }

        public View getGridItemView() {
            return gridItemView;
        }
    }
}
