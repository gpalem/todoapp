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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getName();

    private final int EDIT_REQUEST_CODE = 1;
    private final int EDIT_REQUEST_OK = 1;

    private ArrayList<String> todoItems;
    private ArrayAdapter<String> aToDoAdapter;
    private ListView lvItems;

    private EditText etEnterItem;
    private Button btnAddItem;

    private String taskToEdit;

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
                String deleteTask = todoItems.get(position);
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
                editIntent.putExtra("edit_item", taskToEdit);
                editIntent.putExtra("position", position);
                startActivityForResult(editIntent, EDIT_REQUEST_CODE);
            }
        });

        etEnterItem = (EditText) findViewById(R.id.etEnterItem);
        btnAddItem = (Button) findViewById(R.id.btnAddItem);
    }

    public void populateArrayItems() {
        Log.d(TAG, "populateArrayItems");
        readItemsDB();
        aToDoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View view) {
        Log.d(TAG, "onAddItem");
        String task = etEnterItem.getText().toString();
        aToDoAdapter.add(task);
        etEnterItem.setText("");
        addItemDB(task, Priority.HIGH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == EDIT_REQUEST_OK && requestCode == EDIT_REQUEST_CODE) {
            String task = data.getExtras().getString("save_item");
            int position = data.getExtras().getInt("position");
            todoItems.set(position, task);
            aToDoAdapter.notifyDataSetChanged();
            modifyItemDB(taskToEdit, task, Priority.HIGH);
        }
    }

    private void readItemsDB() {
        Log.i(TAG, "readItemsDB");
        List<TasksModel> taskList = new Select().from(TasksModel.class).execute();
        todoItems = new ArrayList<String>();
        for (TasksModel task : taskList) {
            todoItems.add(task.task);
        }
    }

    private void addItemDB(String task, Priority priority) {
        TasksModel row = new TasksModel(task, priority);
        row.save();
    }

    private void deleteItemDB(String task) {
        try {
            new Delete().from(TasksModel.class).where("Task = ?", task).execute();
        } catch (Exception e) {
            Log.e(TAG, "delete item from Database failed - " + task);
        }
    }

    private void modifyItemDB(String oldTask, String newTask, Priority priority) {
        Log.i(TAG, "modifyItemDB: " + oldTask);
        try {
            TasksModel row = new Select().from(TasksModel.class).where("Task = ?", oldTask).executeSingle();
            row.task = newTask;
            row.priority = priority;
            row.save();
        } catch (Exception e) {
            Log.e(TAG, "update row failed - " + oldTask);
        }
    }
}
