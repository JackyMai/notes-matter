package me.slackti.notesmatter.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

import me.slackti.notesmatter.model.Todo;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notesMatter.db";
    private static final String TABLE_NAME = "todo_data";

    private static final String COL0 = "ID";
    private static final String COL1 = "TITLE";
    private static final String COL2 = "POSITION";
    private static final String COL3 = "DONE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTodoTable = "CREATE TABLE " + TABLE_NAME +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL1 + " TEXT, "
                + COL2 + " INTEGER, "
                + COL3 + " INTEGER)";
        db.execSQL(createTodoTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public long addData(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, todo.getTitle());
        contentValues.put(COL2, todo.getPosition());
        contentValues.put(COL3, todo.getDone());

        return db.insert(TABLE_NAME, null, contentValues);
    }

    public boolean updateListPosition(ArrayList<Todo> todoList, int start, int end) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(int i=start; i<=end; i++) {
            Todo todo = todoList.get(i);

            if(todo.getPosition() != i) {
                todo.setPosition(i);

                ContentValues contentValues = new ContentValues();
                contentValues.put(COL0, todo.getId());
                contentValues.put(COL1, todo.getTitle());
                contentValues.put(COL2, i);
                contentValues.put(COL3, todo.getDone());

                long result = db.update(TABLE_NAME, contentValues, COL0 + " = ?", new String[] {todo.getId()});
                if(result == 0) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean updateData(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL0, todo.getId());
        contentValues.put(COL1, todo.getTitle());
        contentValues.put(COL2, todo.getPosition());
        contentValues.put(COL3, todo.getDone());

        long result = db.update(TABLE_NAME, contentValues, COL0 + " = ?", new String[] {todo.getId()});

        if(result == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_NAME, COL0 + " = ?", new String[] {id});

        if(result == 0) {
            return false;
        } else {
            return true;
        }
    }
}
