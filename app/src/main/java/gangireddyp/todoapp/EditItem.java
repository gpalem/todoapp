package gangireddyp.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItem extends AppCompatActivity {

    private EditText etEditItem;
    private Button btnSave;
    private final int SAVE_REQUEST_CODE = 1;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etEditItem = (EditText) findViewById(R.id.etEditItem);
        btnSave = (Button) findViewById(R.id.btnSave);

        etEditItem.setText(getIntent().getStringExtra("edit_item"));
        etEditItem.setSelection(etEditItem.getText().length());
        position = getIntent().getIntExtra("position", 0);
    }

    public void onSave(View view) {
        Intent saveIntent = new Intent(EditItem.this, MainActivity.class);
        saveIntent.putExtra("save_item", etEditItem.getText().toString());
        saveIntent.putExtra("position", position);
        setResult(SAVE_REQUEST_CODE, saveIntent);
        finish();
    }
}
