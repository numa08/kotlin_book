class Item { var name = "" }

fun main(@Suppress("UNUSED_PARAMETER") args: Array<String>) {
    val name = "numa"
    val item = Item().also {
        it.name = "hoge"
    }
    println(item.name)
    println(name)
}

main(arrayOf<String>())
