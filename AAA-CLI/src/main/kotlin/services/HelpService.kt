package services

class HelpService {
    private var helpMessage = """
        POSSIBLE COMMANDS
        -h - prints out help
        -login <str> -pass <str> - authentication with given credits
        -login <str> -pass <str> -res <str> -role <str> - authorization to the source with given credits and a role
        -ds <YYYY-MM-DD> -de <YYYY-MM-DD> -vol <int> - adds a session where -ds is a start date, -de is an end date and -vol is amount of consumed data
        NOTE that all arguments can be passed in any order
    """.trimIndent()

    fun printHelp() {
        println(helpMessage)
    }
}