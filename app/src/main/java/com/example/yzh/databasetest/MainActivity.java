package com.example.yzh.databasetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MyDatabaseHelper(this,"BookStore.db",null,2);

        //创建数据库
        Button btnCreatDatabase = (Button) findViewById(R.id.btnCreateDatabase);
        btnCreatDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase();
            }
        });

        //插入数据
        Button btnInsert = (Button) findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name","The Da Vinci Code");
                values.put("author","Dan Brown");
                values.put("pages",454);
                values.put("price",16.96);
                db.insert("Book",null,values);
                values.clear();
                values.put("name","The Lost Symbol");
                values.put("author","Dan Brown");
                values.put("pages",510);
                values.put("price",19.95);
                db.insert("Book",null,values);
                Toast.makeText(MainActivity.this,"插入数据成功！",Toast.LENGTH_SHORT).show();
            }
        });
        //更新数据
        Button btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price",10.99);
                db.update("Book",values,"name=?",new String[]{"The Da Vinci Code"});
                Toast.makeText(MainActivity.this,"更新数据成功！",Toast.LENGTH_SHORT).show();
            }
        });
        //删除数据
        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.delete("Book", "pages > ?", new String[]{"500"});
                    Toast.makeText(MainActivity.this, "删除数据成功！", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //查询数据
        Button btnQuery = (Button) findViewById(R.id.btnQuery);
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("Book",null,null,null,null,null,null);
                if (cursor.moveToFirst()){
                    do{
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        String msg = String.format("书名:%s,作者:%s,页数:%d,价格:%.2f",name,author,pages,price);
                        Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        });

        Button btnReplaceData = (Button) findViewById(R.id.btnReplaceData);
        btnReplaceData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.beginTransaction();
                try{
                    db.delete("Book",null,null);
//                    if(true) {
//                        throw new NullPointerException();
//                    }
                    ContentValues values = new ContentValues();
                    values.put("name","Game of Thrones");
                    values.put("author","Geogre Martin");
                    values.put("pages",720);
                    values.put("price",20.85);
                    db.insert("Book",null,values);
                    db.setTransactionSuccessful();
                    Toast.makeText(MainActivity.this,"替换数据成功！",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    db.endTransaction();
                }
            }
        });
    }
}
