package com.fire;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by cyy on 2016/11/8.
 * 说明：
 */

public interface DBCallback {

    void dbCreate();

    void dbUpdate(SQLiteDatabase db, int oldVersion, int newVersion);
}
