package net.numa08.kotlinbook.chapter2.repositories

import net.numa08.kotlinbook.chapter2.models.ProcessInformation

interface ProcessInformationRepository {

    fun findProcessInformationByName(name: String, cb: ((ProcessInformation) -> Unit))

}