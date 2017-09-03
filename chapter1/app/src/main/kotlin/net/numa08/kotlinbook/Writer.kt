package net.numa08.kotlinbook

import java.io.File

interface Writer {

    // 出力するデータは存在しなければならないのでNon-Nullとする
    fun write(formatter: Formatter)

}

class StdOutWriter: Writer {
    override fun write(formatter: Formatter) {
        println(formatter.format())
    }
}

class FileWriter(
        private val file: File
): Writer {

    init {
        require(file.parentFile?.exists() ?: true)
    }

    override fun write(formatter: Formatter) {
        file.createNewFile()
        file.printWriter().use {
            it.println(formatter.format())
        }
    }
}