class Item { var name = "" }

fun main(args: Array<String>) {
    val name = "numa"
    val item = Item().also {
        it.name = "hoge"
    }
    println(item.name)
}
