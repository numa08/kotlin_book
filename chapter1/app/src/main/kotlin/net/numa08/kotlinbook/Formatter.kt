package net.numa08.kotlinbook

import com.google.gson.Gson
import com.google.gson.GsonBuilder

enum class Format {
    JSON, MARKDOWN, TEXT, TABLE;
}

interface Formatter {
    val items: List<Row>

    fun format(): String
}

class JSONFormatter(
        override val items: List<Row>,
        private val gson: Gson = GsonBuilder().create()) : Formatter {


    override fun format(): String = gson.toJson(items)
}

class MarkdownFormatter(
        override val items: List<Row>
) : Formatter {

    override fun format(): String {
        return items.mapNotNull { it as? Row.Item }
                .joinToString(separator = "") {
                    if (it.price == null) {
                        """
- ${it.id}
  - ${it.name}
"""
                    } else {
                        """
- ${it.id}
  - ${it.name}
  - ${it.price}
"""
                    }
                }
    }
}

class TextFormatter(
        override val items: List<Row>
) : Formatter {

    override fun format(): String = items.toString()
}

class MarkdownTableFormatter(
        override val items: List<Row>
) : Formatter {

    override fun format(): String
            = items.joinToString(separator = System.getProperty("line.separator")) {
        when (it) {
            is Row.Header -> {
                val (header, line) = it.columns.fold(StringBuilder("|") to StringBuilder("|"), { (header, line), column ->
                    (header.append("$column|")) to (line.append("---|"))
                })
                header.append(System.getProperty("line.separator")).append(line)
            }
            is Row.Item -> {
                val r = "|${it.id}|${it.name}|"
                if (it.price != null) {
                    r + "${it.price}|"
                } else {
                    r + "|"
                }
            }
        }
    }
}
