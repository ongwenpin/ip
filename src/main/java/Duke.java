import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Duke {

    static ArrayList<Task> listOfThings = new ArrayList<Task>();

    public static void main(String[] args) {

        // Special Commands
        String BYE = "bye";
        String LIST = "list";
        String MARK = "mark";
        String UNMARK = "unmark";
        String TODO = "todo";
        String DEADLINE = "deadline";
        String EVENT = "event";
        String DELETE = "delete";



        Scanner sc = new Scanner(System.in);
        try {
            File dukeFile = new File("duke.txt");
            ReadFileContent(dukeFile);
        } catch (FileNotFoundException e) {

        }

        System.out.println("Hello! I'm Duke\nWhat can I do for you?");

        while(true) {
            String userInput = sc.nextLine();
            try {
                String[] userInputSpilt = userInput.split(" ", 2);
                if (userInputSpilt[0].equals(BYE)) {
                    break;
                } else if (userInputSpilt[0].equals(LIST)) {
                    ListOut();
                } else if (userInputSpilt[0].equals(MARK)) {
                    if (userInputSpilt.length == 1) {
                        throw new MissingTargetException("mark as done");
                    } else {
                        int taskNum = Integer.valueOf(userInputSpilt[1]) - 1;
                        MarkTask(taskNum);
                    }

                } else if (userInputSpilt[0].equals(UNMARK)) {
                    if (userInputSpilt.length == 1) {
                        throw new MissingTargetException("mark as not done");
                    } else {
                        int taskNum = Integer.valueOf(userInputSpilt[1]) - 1;
                        UnmarkTask(taskNum);
                    }

                } else if (userInputSpilt[0].equals(DELETE)) {
                    if (userInputSpilt.length == 1) {
                        throw new MissingTargetException("delete");
                    } else {
                        int taskNum = Integer.valueOf(userInputSpilt[1]) - 1;
                        DeleteTask(taskNum);
                    }
                } else if (userInputSpilt[0].equals(TODO)) {
                    if (userInputSpilt.length == 1) {
                        throw new MissingDescriptionException("todo");
                    } else {
                        ToDo currToDo = new ToDo(userInputSpilt[1]);
                        AddToList(currToDo);
                    }
                } else if (userInputSpilt[0].equals(DEADLINE)) {
                    if (userInputSpilt.length == 1) {
                        throw new MissingDescriptionException("deadline");
                    } else {
                        String[] deadlineSpilt = userInputSpilt[1].split("/by ", 2);
                        if (deadlineSpilt.length == 1) {
                            throw new MissingDeadlineDescriptionException();
                        } else {
                            try {
                                String[] deadlineDateTimeSpilt = deadlineSpilt[1].split(" ", 2);
                                if (deadlineDateTimeSpilt.length == 1) {
                                    LocalDate localDate = LocalDate.parse(deadlineDateTimeSpilt[0],
                                            DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                    Deadline currDeadline =  new Deadline(deadlineSpilt[0], localDate, null);
                                    AddToList(currDeadline);
                                } else {
                                    LocalDate localDate = LocalDate.parse(deadlineDateTimeSpilt[0],
                                            DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                    LocalTime localTime = LocalTime.parse(deadlineDateTimeSpilt[1],
                                            DateTimeFormatter.ofPattern("HHmm"));
                                    Deadline currDeadline =  new Deadline(deadlineSpilt[0], localDate, localTime);
                                    AddToList(currDeadline);
                                }
                            } catch (DateTimeException e) {
                                System.out.println("OOPS! The date and time format for deadline is incorrect\n" +
                                        "FORMAT: yyyy-MM-dd HHmm / yyyy-MM-dd");
                            }

                        }
                    }

                } else if (userInputSpilt[0].equals(EVENT)) {
                    if (userInputSpilt.length == 1) {
                        throw new MissingDescriptionException("event");
                    } else {
                        String[] eventSpilt = userInputSpilt[1].split("/at ", 2);
                        if (eventSpilt.length == 1) {
                            throw new MissingEventDescriptionException();
                        } else {
                            Event currEvent =  new Event(eventSpilt[0], eventSpilt[1]);
                            AddToList(currEvent);
                        }

                    }

                } else {
                    throw new InvalidInputException();
                }
            }
            catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            }

            catch (MissingDescriptionException e) {
                System.out.println(e.getMessage());
            }

            catch (MissingDeadlineDescriptionException e) {
                System.out.println(e.getMessage());
            }

            catch (MissingEventDescriptionException e) {
                System.out.println(e.getMessage());
            }

            catch (MissingTargetException e) {
                System.out.println(e.getMessage());
            }

        }
        AddToSaveList();
        Bye();
    }

    public static void ReadFileContent(File file) throws FileNotFoundException {
        Scanner s = new Scanner(file);
        int curr = 0;
        while(s.hasNext()) {
            String currLine = s.nextLine();
            String[] spiltCurrLine = currLine.split(",", 2);
            if (spiltCurrLine[0].equals("T")) {
                String[] spiltCurrTodo = spiltCurrLine[1].split(",", 2);
                ToDo currTodo = new ToDo(spiltCurrTodo[1]);
                if (spiltCurrTodo[0].equals("1")) {
                    currTodo.markAsDone();
                } else {
                    currTodo.markAsNotDone();
                }
                AddToListQuiet(currTodo);
            } else if (spiltCurrLine[0].equals("D")) {
                String[] spiltCurrDeadline = spiltCurrLine[1].split(",", 4);
                LocalDate localDate = null;
                LocalTime localTime = null;
                if (spiltCurrDeadline.length == 4) {
                    localDate = LocalDate.parse(spiltCurrDeadline[2]);
                    localTime = LocalTime.parse(spiltCurrDeadline[3]);
                } else {
                    localDate = LocalDate.parse(spiltCurrDeadline[2]);
                }
                Deadline currDeadline = new Deadline(spiltCurrDeadline[1], localDate, localTime);
                if (spiltCurrDeadline[0].equals("1")) {
                    currDeadline.markAsDone();
                } else {
                    currDeadline.markAsNotDone();
                }
                AddToListQuiet(currDeadline);
            } else if (spiltCurrLine[0].equals("E")) {
                String[] spiltCurrEvent = spiltCurrLine[1].split(",", 3);
                Event currEvent =  new Event(spiltCurrEvent[1], spiltCurrEvent[2]);
                if (spiltCurrEvent[0] .equals("1")) {
                    currEvent.markAsDone();
                } else {
                    currEvent.markAsNotDone();
                }
                AddToListQuiet(currEvent);
            } else {
                System.out.println("error");
                continue;
            }
        }

    }

    public static void WriteFileContent(String filePath, String textToWrite) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        fw.write(textToWrite);
        fw.close();
    }

    public static void AppendFileContent(String filePath, String textToAppend) throws IOException {
        FileWriter fw = new FileWriter(filePath, true);
        fw.write(textToAppend);
        fw.close();
    }

    public static void AddToListQuiet(Task task) {
        listOfThings.add(task);
    }

    public static void AddToList(Task task) {
        listOfThings.add(task);
        System.out.println("--------------------------------");
        System.out.println("Got it. I've added this task:\n " + task.TaskInfo());
        if (listOfThings.size() == 1) {
            System.out.println("Now you have " + listOfThings.size() + " task in the list.");
        } else {
            System.out.println("Now you have " + listOfThings.size() + " tasks in the list.");
        }
        System.out.println("--------------------------------");
    }

    public static void AddToSaveList() {
        try {
            for (int i = 0; i < listOfThings.size(); i++) {
                if (i == 0) {
                    WriteFileContent("duke.txt", listOfThings.get(i).TaskSaveInfo());
                } else {
                    AppendFileContent("duke.txt", "\n" + listOfThings.get(i).TaskSaveInfo());
                }
            }
        } catch (IOException e) {
            System.out.println("Cannot save task to hard disk" + e.getMessage());
        }
    }

    public static void ListOut() {
        int size = listOfThings.size();
        System.out.println("--------------------------------");
        System.out.println("Here are the tasks in your list");
        for (int i = 0; i < size; i++) {
            System.out.println((i + 1) + ". " + listOfThings.get(i).TaskInfo());
        }
        System.out.println("--------------------------------");
    }

    public static void MarkTask(int taskNum) {
        try {
            Task currentTask = listOfThings.get(taskNum);
            currentTask.markAsDone();
            System.out.println("--------------------------------");
            System.out.println("Nice! I've marked this task as done:\n " + currentTask.TaskInfo());
            System.out.println("--------------------------------");
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("OOPS!!! The task you are trying to mark does not exist");
        }

    }

    public static void UnmarkTask(int taskNum) {
        try {
            Task currentTask = listOfThings.get(taskNum);
            currentTask.markAsNotDone();
            System.out.println("--------------------------------");
            System.out.println("Ok, I've marked this task as not done yet:\n " + currentTask.TaskInfo());
            System.out.println("--------------------------------");
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("OOPS!!! The task you are trying to mark as not done does not exist");
        }

    }

    public static void DeleteTask(int taskNum) {
        try {
            Task currentTask = listOfThings.remove(taskNum);
            System.out.println("--------------------------------");
            System.out.println("Noted. I've removed this task:\n " + currentTask.TaskInfo());
            if (listOfThings.size() == 1) {
                System.out.println("Now you have " + listOfThings.size() + " task in the list.");
            } else {
                System.out.println("Now you have " + listOfThings.size() + " tasks in the list.");
            }
            System.out.println("--------------------------------");
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("OOPS!!! The task you are trying to delete does not exist");
        }
    }

    public static void Bye() {
        System.out.println("--------------------------------");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("--------------------------------");
    }
}
