import net.numa08.kotlinbook.*
import java.io.File

object App {

    // program filename format destination
    @JvmStatic
    fun main(args: Array<String>) {
        val filePath = args.getOrExit(0, { "読み込むファイルパスを指定してください" })
        val outType = args.getOrExit(1, { "出力するフォーマットを指定してください" })
        val destination = args.getOrNull(2)
        println("$filePath, $outType, $destination")

        val content = FileLoader(File(filePath).apply {
            if (!exists()) {
                throw IllegalArgumentException("$filePath にファイルがありません")
            }
        }).load()?.let(CSVParser()::parse) ?: throw IllegalArgumentException("ファイルの中身がありません")
        val format = enumValueOf<Format>(outType.toUpperCase())
        val formatter = when(format) {
            Format.JSON -> JSONFormatter(content)
            Format.MARKDOWN -> MarkdownFormatter(content)
            Format.TEXT -> TextFormatter(content)
            Format.TABLE -> MarkdownTableFormatter(content)
        }
        val writer = if (destination == null) {
            StdOutWriter()
        } else {
            FileWriter(File(destination).also {
                it.parentFile?.mkdir()
            })
        }
        writer.write(formatter)
    }

    private inline fun Array<String>.getOrExit(index: Int, lazyMessage: () -> String): String {
        val item = getOrNull(index)
        if (item != null) {
            return item
        }
        System.err.println(lazyMessage())
        System.exit(-1)
        return ""
    }

}