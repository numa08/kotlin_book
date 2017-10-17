package net.numa08.kotlinbook.chapter2.repositories

import kotlinx.coroutines.experimental.Deferred
import net.numa08.kotlinbook.chapter2.models.ProcessInformation

interface ProcessInformationRepository {

    fun findProcessInformationByName(name: String, cb: ((ProcessInformation) -> Unit))
    fun findProcessInformationByNameAsync(name: String): Deferred<ProcessInformation>

}