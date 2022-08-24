import java.util.ArrayList;

public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    @Override
    public void execute(TaskList task, Ui ui, Storage storage) {
        ArrayList<Task> taskList = task.getListOfTasks();
        ui.displayTaskList(taskList);
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
