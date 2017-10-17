package net.numa08.kotlinbook.chapter2.repositories

import kotlinx.coroutines.experimental.Deferred
import net.numa08.kotlinbook.chapter2.models.ApplicationInformation

interface ApplicationInformationRepository {
    fun findAllApplications(cb: ((List<ApplicationInformation>) -> Unit))
    fun findApplicationByPackageName(packageName: String, cb: ((ApplicationInformation?) -> Unit))

    fun findAllApplicationsAsync(): Deferred<List<ApplicationInformation>>
    fun findApplicationByPackageNameAsync(packageName: String): Deferred<ApplicationInformation?>
}
