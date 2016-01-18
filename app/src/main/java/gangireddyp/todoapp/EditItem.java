package gangireddyp.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;

public class EditItem extends AppCompatActivity {

    private String TAG = EditItem.class.getName();

    private EditText etEditItem;
    private NumberPicker npPriority;

    private final int SAVE_REQUEST_CODE = 1;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etEditItem = (EditText) findViewById(R.id.etEditItem);
        npPriority = (NumberPicker) findViewById(R.id.npPriority);
        npPriority.setMinValue(Priority.LOW.ordinal());
        npPriority.setMaxValue(Priority.HIGH.ordinal());
        npPriority.setDisplayedValues(new String[]{"LOW", "MEDIUM", "HIGH"});
        npPriority.setWrapSelectorWheel(false);

        etEditItem.setText(getIntent().getStringExtra("edit_item"));
        etEditItem.setSelection(etEditItem.getText().length());
        npPriority.setValue(getIntent().getIntExtra("edit_priority", 0));
        position = getIntent().getIntExtra("position", 0);
        Log.i(TAG, "intents " + getIntent().getExtras().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save_icon) {
            Intent saveIntent = new Intent(EditItem.this, MainActivity.class);
            saveIntent.putExtra("save_item", etEditItem.getText().toString());
            saveIntent.putExtra("save_priority", npPriority.getValue());
            saveIntent.putExtra("position", position);
            setResult(SAVE_REQUEST_CODE, saveIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
