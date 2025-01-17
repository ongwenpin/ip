package duke.task;

/**
 * The superclass of all tasks.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected Priority taskPriority;
    public enum Priority {
        HIGH,
        MEDIUM,
        LOW;
    }

    public Task(String description) {
        this.description = description;
        this.isDone = false;
        this.taskPriority = Priority.LOW;
    }

    protected String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    protected String getSavedStatusIcon() {
        return (isDone ? "1" : "0");
    }

    /**
     * Marks the task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns a string that contains the status icon and the description of the task.
     * @return A string that contains the status icon and the description of the task.
     */
    public String taskInfo() {
        return "[] [" + getStatusIcon() + "] " + description;
    }

    /**
     * Returns a string that contains the status icon and description of the task that
     * is used for saving.
     * 
     * @return A string that contains the status icon and description of the task that
     * is used for saving.
     */
    public String taskSaveInfo() {
        return "," + getSavedStatusIcon() + "," + description;
    }

    /**
     * Returns true if the description of the task contains the specified keyword.
     * @param keyword The keyword to search for.
     * @return True if description contains the keyword, false otherwise.
     */
    public boolean containKeyword(String keyword) {
        return this.description.contains(keyword);
    }

    public String getTaskPriority() {
        if (this.taskPriority == taskPriority.HIGH) {
            return "HIGH";
        } else if (this.taskPriority == taskPriority.MEDIUM) {
            return "MEDIUM";
        } else if (this.taskPriority == taskPriority.LOW) {
            return "LOW";
        } else {
            assert false;
            return null;
        }
    }

    public void setTaskPriority(String priority) {
        if (priority.equals("high")) {
            this.taskPriority = taskPriority.HIGH;
        } else if (priority.equals("medium")) {
            this.taskPriority = taskPriority.MEDIUM;
        } else if (priority.equals("low")) {
            this.taskPriority = taskPriority.LOW;
        } else {
            assert false;
        }
    }

}
