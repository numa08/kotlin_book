class Item { var name = "" }

fun main(args: Array<String>) {
    val name = "numa"
    val item = Item().apply {
        name = "hoge"
    }
    println(item.name)
}
