package me.slackti.notesmatter.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.slackti.notesmatter.model.Todo;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notesMatter.db";
    private static final String TABLE_NAME = "todo_data";
    private static final String COL0 = "ID";
    private static final String COL1 = "TITLE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTodoTable = "CREATE TABLE " + TABLE_NAME +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL1 + " TEXT)";
        db.execSQL(createTodoTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public long addData(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, todo.getTitle());

        return db.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public boolean deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_NAME, "ID = ?", new String[] {id});

        if(result == 0) {
            return false;
        } else {
            return true;
        }
    }
}
