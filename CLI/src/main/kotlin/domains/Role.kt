package domains

enum class Role(val role: String) {
    READ("READ"),
    WRITE("WRITE"),
    EXECUTE("EXECUTE")
}