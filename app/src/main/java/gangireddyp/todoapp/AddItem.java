package gangireddyp.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;

public class AddItem extends AppCompatActivity {

    private EditText etAddItem;
    private NumberPicker npPriority;

    private final int ADD_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etAddItem = (EditText) findViewById(R.id.etEnterItem);

        npPriority = (NumberPicker) findViewById(R.id.npPriority);
        npPriority.setMinValue(Priority.LOW.ordinal());
        npPriority.setMaxValue(Priority.HIGH.ordinal());
        npPriority.setDisplayedValues(new String[]{"LOW", "MEDIUM", "HIGH"});
        npPriority.setValue(Priority.MEDIUM.ordinal());
        npPriority.setWrapSelectorWheel(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
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
            Intent saveIntent = new Intent(AddItem.this, MainActivity.class);
            saveIntent.putExtra("add_item", etAddItem.getText().toString());
            saveIntent.putExtra("add_priority", npPriority.getValue());
            setResult(ADD_REQUEST_CODE, saveIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
