package gangireddyp.todoapp;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Tasks", id = "_Task")
public class TasksModel extends Model {
    @Column(name = "Task", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String task;
    // This is an association to another activeandroid model
    @Column(name = "Category")
    public Priority priority;

    // Make sure to have a default constructor for every ActiveAndroid model
    public TasksModel(){
        super();
    }

    public TasksModel(String task, Priority priority){
        super();
        this.task = task;
        this.priority = priority;
    }
}