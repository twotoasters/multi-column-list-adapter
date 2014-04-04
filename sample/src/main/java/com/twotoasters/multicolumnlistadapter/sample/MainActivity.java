package main.sample;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.twotoasters.multicolumnlistadapter.sample.R;

import main.sample.ToasterData.Toaster;

import java.util.List;

public class MainActivity extends Activity {

    private ListView multiColumnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadToastersIntoDatabase();
        setupGrid();
    }

    private void loadToastersIntoDatabase() {
        List<ToasterModel> toasterModels = new Select().from(ToasterModel.class).execute();
        if (toasterModels == null || toasterModels.size() == 0) {
            ActiveAndroid.beginTransaction();
            try {
                Toaster[] toasters = ToasterData.getToasters();
                for (Toaster toaster : toasters) {
                    ToasterModel toasterModel = new ToasterModel();
                    toasterModel.name = toaster.name;
                    toasterModel.imageResId = toaster.imageResId;
                    toasterModel.save();
                }
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }
        }
    }

    private void setupGrid() {
        SQLiteDatabase db = ActiveAndroid.getDatabase();
        Cursor cursor = db.query(ToasterModel.TABLE_NAME, null, null, null, null, null, null);

        ToasterAdapter adapter = new ToasterAdapter(this, cursor,
                R.layout.grid_item, R.integer.num_columns, R.dimen.grid_spacing);

        View headerView = LayoutInflater.from(this).inflate(R.layout.view_header, null, false);
        View footerView = LayoutInflater.from(this).inflate(R.layout.view_footer, null, false);

        multiColumnList = (ListView) findViewById(R.id.multiColumnList);
        multiColumnList.addHeaderView(headerView);
        multiColumnList.addFooterView(footerView);
        multiColumnList.setAdapter(adapter);
    }
}
