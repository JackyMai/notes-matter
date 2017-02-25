package me.slackti.notesmatter.database;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import me.slackti.notesmatter.adapter.HistoryAdapter;
import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.model.Todo;

public class FirebaseHelper {

    private static FirebaseHelper firebaseHelper;
    private static FirebaseDatabase database;

    private DatabaseReference rootRef = database.getReference();
    private DatabaseReference activeRef = rootRef.child(ACTIVE);
    private DatabaseReference inactiveRef = rootRef.child(INACTIVE);

    private static final String ACTIVE = "active";
    private static final String INACTIVE = "inactive";

    private static final String TITLE= "title";
    private static final String DEADLINE = "deadline";
    private static final String POSITION = "position";

    private FirebaseHelper() {}

    public static FirebaseHelper getInstance() {
        if(firebaseHelper == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);

            firebaseHelper = new FirebaseHelper();
        }
        return firebaseHelper;
    }

    public String addActiveData(Todo todo) {
        return addData(todo, activeRef);
    }

    public String addInactiveData(Todo todo) {
        return addData(todo, inactiveRef);
    }

    private String addData(Todo todo, DatabaseReference ref) {
        DatabaseReference pushRef = ref.push();
        pushRef.setValue(todo);
        return pushRef.getKey();
    }

    public void setActiveDataListener(final ArrayList<Todo> todoList, final TodoAdapter adapter) {
        activeRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Todo todo = dataSnapshot.getValue(Todo.class);
                todo.setKey(dataSnapshot.getKey());

                if(todo.getPosition() == -1) {
                    todo.setPosition(todoList.size());
                    updateData(todo);
                }

                todoList.add(todo);

                adapter.notifyItemInserted(todoList.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // Sort arraylist according to item's position
                Collections.sort(todoList, new Comparator<Todo>() {
                    @Override
                    public int compare(Todo todo1, Todo todo2) {
                        return todo1.getPosition()-todo2.getPosition();
                    }
                });

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        activeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Sort arraylist according to item's position
                Collections.sort(todoList, new Comparator<Todo>() {
                    @Override
                    public int compare(Todo todo1, Todo todo2) {
                        return todo1.getPosition()-todo2.getPosition();
                    }
                });

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void retrieveInactiveData(final ArrayList<Todo> todoList, final HistoryAdapter adapter) {
        inactiveRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot todoSnapshot: dataSnapshot.getChildren()) {
                    Todo todo = todoSnapshot.getValue(Todo.class);
                    todo.setKey(todoSnapshot.getKey());

                    if(todo.getPosition() == -1) {
                        todo.setPosition(todoList.size());
                        updateData(todo);
                    }

                    todoList.add(todo);

                    adapter.notifyItemInserted(todoList.size()-1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void updateData(Todo todo) {
        activeRef.child(todo.getKey()).setValue(todo);
    }

    public void removeData(Todo todo) {
        activeRef.child(todo.getKey()).removeValue();
    }

    private void removeData(Todo todo, DatabaseReference ref) {
        ref.child(todo.getKey()).removeValue();
    }

    public void updateActiveItemPositions(ArrayList<Todo> todoList, int start, int end) {
        updateItemPositions(todoList, activeRef, start, end);
    }

    public void updateInactiveItemPositions(ArrayList<Todo> todoList, int start, int end) {
        updateItemPositions(todoList, inactiveRef, start, end);
    }

    private void updateItemPositions(ArrayList<Todo> todoList, DatabaseReference ref, int start, int end) {
        for(int i=start; i<=end; i++) {
            Todo todo = todoList.get(i);

            if(todo.getPosition() != i) {
                todo.setPosition(i);

                ref.child(todo.getKey()).child(POSITION).setValue(i);
            }
        }
    }

    public void moveActiveData(Todo todo) {
        moveData(todo, activeRef, inactiveRef);
    }

    public void moveInactiveData(Todo todo) {
        todo.setPosition(-1);
        moveData(todo, inactiveRef, activeRef);
    }

    private void moveData(Todo todo, DatabaseReference fromRef, DatabaseReference toRef) {
        removeData(todo, fromRef);
        addData(todo, toRef);
    }
}
