package com.example.cyy.firedemo;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.cyy.firedemo.model.Person;
import com.fire.DBCallback;
import com.fire.table.DBBase;

/**
 * Created by study on 16/11/24.
 */

public class App extends Application {
    @Override
    public void onCreate() {

        super.onCreate();

        DBBase.Config config = DBBase.builcConfig()
                .setName("dbName.db")
                .setVersion(1)
                .setCallback(new DBCallback() {
                    @Override
                    public void dbCreate() {

                    }

                    @Override
                    public void dbUpdate(SQLiteDatabase db, int oldVersion, int newVersion) {

                    }
                });

        DBBase.getBase().init(this,config).creatTables(new Class[]{Person.class});
    }
}
