package com.twotoasters.multicolumnlistadapter.sample;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = ToasterModel.TABLE_NAME, id = BaseColumns._ID)
public class ToasterModel extends Model {

    @Column(name = NAME)
    public String name;
    @Column(name = IMAGE_RES_ID)
    public int imageResId;

    public static final String TABLE_NAME = "Toasters";
    public static final String NAME = "Name";
    public static final String IMAGE_RES_ID = "ImageResId";
}
