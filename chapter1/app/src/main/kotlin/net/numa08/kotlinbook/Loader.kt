package net.numa08.kotlinbook

import java.io.File

interface Loader {

    // テキストを読み込む外部システムによっては、文字列を返さないことも想定できるので nullable とする
    fun load(): String?

}

class FileLoader(private val file: File): Loader {

    init {
        // 事前条件であるファイルの存在性を要求する
        require(file.exists())
    }

    override fun load(): String? = file.readText()
}
