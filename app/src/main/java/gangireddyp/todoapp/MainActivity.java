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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getName();

    private final int EDIT_REQUEST_CODE = 1;
    private final int EDIT_REQUEST_OK = 1;

    private ArrayList<String> todoItems;
    private ArrayAdapter<String> aToDoAdapter;
    private ListView lvItems;

    private EditText etEnterItem;
    private Button btnAddItem;

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
                todoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editIntent = new Intent(MainActivity.this, EditItem.class);
                editIntent.putExtra("edit_item", todoItems.get(position));
                editIntent.putExtra("position", position);
                startActivityForResult(editIntent, EDIT_REQUEST_CODE);
            }
        });

        etEnterItem = (EditText) findViewById(R.id.etEnterItem);
        btnAddItem = (Button) findViewById(R.id.btnAddItem);
    }

    public void populateArrayItems() {
        Log.d(TAG, "populateArrayItems");
        readItems();
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
        aToDoAdapter.add(etEnterItem.getText().toString());
        etEnterItem.setText("");
        writeItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == EDIT_REQUEST_OK && requestCode == EDIT_REQUEST_CODE) {
            String task = data.getExtras().getString("save_item");
            int position = data.getExtras().getInt("position");
            todoItems.set(position, task);
            aToDoAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

    private void readItems() {
        Log.d(TAG, "readItems");
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            todoItems = new ArrayList<String>(FileUtils.readLines(file));
        } catch (IOException e) {
            Log.i(TAG, "read items failed!");
        }
    }

    private void writeItems() {
        Log.d(TAG, "writeItems");
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileUtils.writeLines(file, todoItems);
        } catch (IOException e) {
            Log.i(TAG, "write items failed!");
        }
    }
}
