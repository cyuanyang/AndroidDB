package com.example.cyy.firedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyy.firedemo.model.Person;
import com.fire.table.DBBase;
import com.fire.table.selector.WhereInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected Button btnInsert;
    protected EditText ttInsert;
    protected Button searchBtn;
    protected Button deletehBtn;
    protected TextView searchTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btnInsert = (Button) findViewById(R.id.btn_insert);
        btnInsert.setOnClickListener(MainActivity.this);
        ttInsert = (EditText) findViewById(R.id.tt_insert);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(MainActivity.this);
        deletehBtn = (Button) findViewById(R.id.deleteBtn);
        deletehBtn.setOnClickListener(this);
        searchTextView = (TextView) findViewById(R.id.searchTextView);
    }

    boolean ishot = false;
    int age = 10;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_insert) {
            Person person = new Person();
            person.setName(ttInsert.getText().toString());
            person.setBirth(new Date());
            person.setHot(ishot);
            ishot = !ishot;
            person.setAge(age);
            age++;
            person.setMoney(1012131231);
            person.setHour(10.25f);
            DBBase.newDBHelper().save(person);
            Toast.makeText(this, "insert Ok", Toast.LENGTH_LONG).show();
        } else if (view.getId() == R.id.searchBtn) {

            List<Person> result = DBBase.newDBHelper().find(Person.class);
            String string = "";
            for (Person p : result) {
                string += "id=" + p.getId() + " | name=" + p.getName() + " | age="+p.getAge()+ " | birth=" + p.getBirth() + " | hot=" + p.isHot() + " | money=" + p.getMoney() + " | hour=" + p.getHour() + "\n\n";
            }
            searchTextView.setText(string);

        }else if (view.getId() == R.id.deleteBtn)  {
            List<Person> objects = new ArrayList<>();
            Person p1 = new Person();
            p1.setId(2);
            objects.add(p1);

            Person p2 = new Person();
            p2.setId(3);
            objects.add(p2);
            DBBase.newDBHelper().deleteById(objects);
//            DBBase.newDBHelper().deleteById(Person.class , "1"); ///根据ID删除
//            DBBase.newDBHelper().delete(Person.class, WhereInfo.Builder.buildWhere("name", "=", "jack").and("age", ">", "12").build());
        }
    }

}
