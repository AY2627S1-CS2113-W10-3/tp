package seedu.duke.commands;

public class UnknownCommand extends Command {
  @Override
  public String execute() {
    return "I do not know this command";
  }
}
