package gangireddyp.todoapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gpalem on 1/17/16.
 */
public class CustomAdapter extends ArrayAdapter<TodoItem> {

    private final Context context;
    private final ArrayList<TodoItem> todoItems;

    String [] priorities = {"LOW", "MED", "HIGH"};

    public CustomAdapter(Context context, ArrayList<TodoItem> todoItems) {
        super(context, R.layout.todo_item, todoItems);
        this.context = context;
        this.todoItems = todoItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.todo_item, parent, false);
        TextView taskView = (TextView) rowView.findViewById(R.id.item_task);
        TextView priorityView = (TextView) rowView.findViewById(R.id.item_priority);
        taskView.setText(todoItems.get(position).task);
        Priority p = todoItems.get(position).priority;
        priorityView.setText(priorities[p.ordinal()]);

        if (p == Priority.HIGH) {
            priorityView.setTextColor(Color.RED);
        }
        else if (p == Priority.MEDIUM) {
            priorityView.setTextColor(Color.rgb(255,165,0)/*orange*/);
        }
        else {
            priorityView.setTextColor(Color.GREEN);
        }

        return rowView;
    }
}
