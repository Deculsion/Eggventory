package commands;

import duke.TaskList;
import duke.commands.AddCommand;
import duke.enums.CommandType;
import duke.exceptions.BadInputException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddCommandTest {
    TaskList newList = new TaskList();
    @Test
    public void execute_addTodoTask_success() throws BadInputException {
        new AddCommand(CommandType.TODO, "Test TODO", "").execute(newList);
        assertEquals("Test TODO", newList.getTask(0).getDescription());
    }

    @Test
    public void execute_addDeadlineTask_success() throws BadInputException {
        new AddCommand(CommandType.DEADLINE, "Test DEADLINE", "15/12/2019 1500").execute(newList);
        assertEquals( "[D] [\u2718] Test DEADLINE " +"(by: Sun Dec 15 15:00:00 SGT 2019)"
                , newList.getTask(0).toString());
    }

    @Test
    public void execute_addEventTask_success() throws BadInputException {
        new AddCommand(CommandType.EVENT, "Test EVENT", "15/12/2019 1500 to 17/12/2019 1500").execute(newList);
        assertEquals( "[E] [\u2718] Test EVENT " + "(at: Sun Dec 15 15:00:00 SGT 2019 "
                + "to Tue Dec 17 15:00:00 SGT 2019)", newList.getTask(0).toString());
    }
}