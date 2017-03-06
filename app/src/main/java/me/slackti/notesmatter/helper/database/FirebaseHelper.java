package me.slackti.notesmatter.helper.database;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import me.slackti.notesmatter.listener.BasicChildEventListener;
import me.slackti.notesmatter.model.Todo;

public class FirebaseHelper {

    private static FirebaseHelper firebaseHelper;
    private static FirebaseDatabase database;

    private DatabaseReference activeRef;
    private DatabaseReference inactiveRef;

    private static final String ACTIVE = "active";
    private static final String INACTIVE = "inactive";

    private static final String TITLE= "title";
    private static final String DEADLINE = "deadline";
    private static final String POSITION = "position";

    private FirebaseHelper() {
        // Constructor made private to prevent it from being instantiated from outside
    }

    public static FirebaseHelper getInstance() {
        if(firebaseHelper == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);

            firebaseHelper = new FirebaseHelper();
        }

        return firebaseHelper;
    }

    public void updateUserReferences() {
        DatabaseReference rootRef = database.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            activeRef = rootRef.child(ACTIVE).child(user.getUid());
            inactiveRef = rootRef.child(INACTIVE).child(user.getUid());
        }
    }

    public void addActiveData(Todo todo) {
        addData(todo, activeRef);
    }

    private void addData(Todo todo, DatabaseReference ref) {
        ref.push().setValue(todo);
    }

    public void setActiveDataListener(final ArrayList<Todo> todoList, final TodoAdapter adapter) {
        activeRef.addChildEventListener(new BasicChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Todo todo = dataSnapshot.getValue(Todo.class);
                todo.setKey(dataSnapshot.getKey());

                if(todo.getPosition() == -1) {
                    todo.setPosition(todoList.size());
                    updateData(todo);
                }

                todoList.add(todo);
                adapter.notifyItemInserted(todoList.indexOf(todo));
            }

//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                sortListByPosition(todoList, adapter);
//            }
        });
        activeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sortListByPosition(todoList, adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void sortListByPosition(final ArrayList<Todo> todoList, final TodoAdapter adapter) {
        // Sort arraylist according to item's position
        Collections.sort(todoList, new Comparator<Todo>() {
            @Override
            public int compare(Todo todo1, Todo todo2) {
                return todo1.getPosition()-todo2.getPosition();
            }
        });
        adapter.notifyDataSetChanged();
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

                // So that newly completed items would be on top
                Collections.sort(todoList, new Comparator<Todo>() {
                    @Override
                    public int compare(Todo todo1, Todo todo2) {
                        return todo2.getKey().compareTo(todo1.getKey());
                    }
                });
                adapter.notifyDataSetChanged();
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
