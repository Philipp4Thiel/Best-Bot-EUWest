package BestBotEuWest.command;

import java.util.List;

public interface ICommand {

    String getName();

    //TODO String standardizedHelp();

    default List<String> getAliases() {
        return List.of();
    }
}
