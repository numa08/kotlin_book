package net.numa08.kotlinbook

sealed class Row {

    data class Header(
            val columns: List<String>
    ) : Row()

    data class Item(
            val id: Long,
            val name: String,
            val price: Long?
    ) : Row()
}
