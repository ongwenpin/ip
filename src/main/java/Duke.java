import java.io.File;
import java.io.FileNotFoundException;


public class Duke {

    private Parser parser;
    private Storage storage;
    private TaskList taskList;
    private Ui ui;

    public Duke(String filePath) {
        parser = new Parser();
        storage = new Storage();
        taskList = new TaskList();
        ui = new Ui();

        try {
            ui.displayLoading();
            storage.ReadFileContent(new File(filePath), taskList);
            ui.displayLoadingSuccess();
        } catch (FileNotFoundException e) {
            ui.displayLoadingError();
        }
    }

    public void run() {
        ui.displayHello();
        boolean isExit = false;
        while (!isExit) {
            try {
                String input = ui.GetUserInput();
                Command c = parser.parse(input);
                c.execute(taskList,ui,storage);
                isExit = c.isExit();
            } catch (DukeException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public static void main(String[] args) {
        new Duke("duke.txt").run();
    }

}
