package net.numa08.kotlinbook

interface Parser {
    fun parse(content: String): List<Row>

    class IllegalFormatException(override val message: String) : Exception(message)
}

class CSVParser : Parser {

    override fun parse(content: String): List<Row> {
        val rows = content
                .split(System.getProperty("line.separator"))
        val header = rows.getOrNull(0)
                ?.let { Row.Header(it.split(",")) }
                ?: throw Parser.IllegalFormatException("1行目にヘッダーがありません")
        val items = rows.drop(1)
                .mapIndexed { index, s ->
                    val l = s.split(",")
                    val id = l.getOrElse(0,
                            { throw Parser.IllegalFormatException("${index + 1} に `id` がありません ") })
                            .toLongOrThrow { "${index + 1} が数値ではありません: $it" }
                    val name = l.getOrElse(1,
                            { throw Parser.IllegalFormatException("${index + 1} に `name` がありません") })
                    val price = l.getOrNull(2)?.toLongOrNull()
                    Row.Item(id, name, price)
                }
        return listOf(header) + items
    }

    private inline fun String.toLongOrThrow(radix: Int = 10, lazyMessage: (String) -> String): Long {
        try {
            return toLong(radix)
        } catch (e: NumberFormatException) {
            throw Parser.IllegalFormatException(lazyMessage(this))
        }
    }
}