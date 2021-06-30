package server.command;

import java.io.Serializable;

public enum NameOfCommand implements Serializable {
    HELP,
    INFO,
    SHOW,
    INSERT,
    UPDATE,
    REMOVE_KEY,
    CLEAR,
    EXECUTE_SCRIPT,
    REMOVE_LOWER,
    REMOVE_GREATER_KEY,
    REMOVE_LOWER_KEY,
    REMOVE_ANY_BY_CHAPTER,
    FILTER_GREATER_THAN_ACHIEVEMENTS,
    PRINT_FIELD_DESCENDING_CATEGORY,
    EXIT,
    NOTHING
}
