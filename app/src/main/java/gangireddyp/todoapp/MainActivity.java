package gangireddyp.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getName();

    private final int EDIT_REQUEST_CODE = 1;
    private final int EDIT_REQUEST_OK = 1;

    private final int ADD_REQUEST_CODE = 2;
    private final int ADD_REQUEST_OK = 2;

    private ArrayList<TodoItem> todoItems;
    private ArrayAdapter<TodoItem> aToDoAdapter;
    private ListView lvItems;

    private TodoItem taskToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem deleteTask = todoItems.get(position);
                aToDoAdapter.remove(deleteTask);
                deleteItemDB(deleteTask);
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editIntent = new Intent(MainActivity.this, EditItem.class);
                taskToEdit = todoItems.get(position);
                editIntent.putExtra("edit_item", taskToEdit.task);
                editIntent.putExtra("edit_priority", taskToEdit.priority.ordinal());
                editIntent.putExtra("position", position);
                startActivityForResult(editIntent, EDIT_REQUEST_CODE);
            }
        });

    }

    public void populateArrayItems() {
        Log.d(TAG, "populateArrayItems");
        readItemsDB();
        aToDoAdapter = new CustomAdapter(this, todoItems);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_icon) {
            Intent addIntent = new Intent(MainActivity.this, AddItem.class);
            startActivityForResult(addIntent, ADD_REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == EDIT_REQUEST_OK && requestCode == EDIT_REQUEST_CODE) {
            String task = data.getExtras().getString("save_item");
            Priority p = Priority.values()[data.getExtras().getInt("save_priority")];
            TodoItem newItem = new TodoItem(task, p);
            int position = data.getExtras().getInt("position");
            todoItems.set(position, newItem);
            aToDoAdapter.notifyDataSetChanged();
            modifyItemDB(taskToEdit, newItem);
        }
        else if (resultCode == ADD_REQUEST_OK && requestCode == ADD_REQUEST_CODE) {
            String task = data.getExtras().getString("add_item");
            Priority p = Priority.values()[data.getExtras().getInt("add_priority")];
            aToDoAdapter.add(new TodoItem(task, p));
            addItemDB(task, p);
        }
    }

    private void readItemsDB() {
        Log.i(TAG, "readItemsDB");
        List<TasksModel> taskList = new Select().from(TasksModel.class).execute();
        todoItems = new ArrayList<TodoItem>();
        for (TasksModel task : taskList) {
            todoItems.add(new TodoItem(task.task, task.priority));
        }
    }

    private void addItemDB(String task, Priority priority) {
        TasksModel row = new TasksModel(task, priority);
        row.save();
    }

    private void deleteItemDB(TodoItem item) {
        try {
            new Delete().from(TasksModel.class).where("Task = ?", item.task).execute();
        } catch (Exception e) {
            Log.e(TAG, "delete item from Database failed - " + item.task);
        }
    }

    private void modifyItemDB(TodoItem oldTask, TodoItem newTask) {
        Log.i(TAG, "modifyItemDB: " + oldTask.task);
        try {
            TasksModel row = new Select().from(TasksModel.class).where("Task = ?", oldTask.task).executeSingle();
            row.task = newTask.task;
            row.priority = newTask.priority;
            row.save();
        } catch (Exception e) {
            Log.e(TAG, "update row failed - " + oldTask.task);
        }
    }
}
