package BestBotEuWest.command;

import java.util.List;

public interface ICommand {

    String getName();

    //String standardizedHelp();

    default List<String> getAliases() {
        return List.of();
    }
}
