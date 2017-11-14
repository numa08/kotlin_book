package net.numa08.kotlinbook.chapter2.repositories

import net.numa08.kotlinbook.chapter2.models.ApplicationInformation

interface ApplicationInformationRepository {
    fun findAllApplications(cb: ((List<ApplicationInformation>) -> Unit))
    fun findApplicationByPackageName(packageName: String, cb: ((ApplicationInformation?) -> Unit))
}
