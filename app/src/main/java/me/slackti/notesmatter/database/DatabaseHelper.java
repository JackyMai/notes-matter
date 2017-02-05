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
    private static final String TABLE_ACTIVE = "todo_active";
    private static final String TABLE_INACTIVE = "todo_inactive";
    private static final int VERSION = 1;

    private static final String COL0 = "ID";
    private static final String COL1 = "TITLE";
    private static final String COL2 = "POSITION";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createActiveTable = "CREATE TABLE " + TABLE_ACTIVE +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL1 + " TEXT, "
                + COL2 + " INTEGER)";
        String createInactiveTable = "CREATE TABLE " + TABLE_INACTIVE +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL1 + " TEXT, "
                + COL2 + " INTEGER)";

        db.execSQL(createActiveTable);
        db.execSQL(createInactiveTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVE);
    }

    public Cursor getIncompleteItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ACTIVE, null);
    }

    public Cursor getCompletedItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_INACTIVE
                + " ORDER BY " + COL0 + " DESC", null);
    }

    // Add to active table by default
    public long addData(Todo todo) {
        return this.addData(todo, TABLE_ACTIVE);
    }

    private long addData(Todo todo, String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, todo.getTitle());
        contentValues.put(COL2, todo.getPosition());

        return db.insert(TABLE_NAME, null, contentValues);
    }

    public boolean updateActiveItemPositions(ArrayList<Todo> todoList, int start, int end) {
        return this.updateItemPositions(todoList, TABLE_ACTIVE, start, end);
    }

    public boolean updateInactiveItemPositions(ArrayList<Todo> todoList, int start, int end) {
        return this.updateItemPositions(todoList, TABLE_INACTIVE, start, end);
    }

    private boolean updateItemPositions(ArrayList<Todo> todoList, String TABLE_NAME, int start, int end) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(int i=start; i<=end; i++) {
            Todo todo = todoList.get(i);

            if(todo.getPosition() != i) {
                todo.setPosition(i);

                ContentValues contentValues = new ContentValues();
                contentValues.put(COL0, todo.getId());
                contentValues.put(COL1, todo.getTitle());
                contentValues.put(COL2, i);

                long result = db.update(TABLE_NAME, contentValues, COL0 + " = ?", new String[] {todo.getId()});
                if(result == 0) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean onItemDone(Todo todo) {
        return this.moveData(todo, TABLE_ACTIVE, TABLE_INACTIVE);
    }

    public boolean onItemUndone(Todo todo) {
        todo.setPosition(-1);
        return this.moveData(todo, TABLE_INACTIVE, TABLE_ACTIVE);
    }

    private boolean moveData(Todo todo, String fromTable, String toTable) {
        long resultAdd = addData(todo, toTable);
        boolean resultDelete = deleteData(todo, fromTable);

        return resultAdd != -1 && resultDelete;
    }

    public boolean updateData(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL0, todo.getId());
        contentValues.put(COL1, todo.getTitle());
        contentValues.put(COL2, todo.getPosition());

        long result = db.update(TABLE_ACTIVE, contentValues, COL0 + " = ?", new String[] {todo.getId()});

        return result != 0;
    }

    // Delete from active table by default
    public boolean deleteData(Todo todo) {
        return this.deleteData(todo, TABLE_ACTIVE);
    }

    private boolean deleteData(Todo todo, String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_NAME, COL0 + " = ?", new String[] {todo.getId()});

        return result != 0;
    }
}
