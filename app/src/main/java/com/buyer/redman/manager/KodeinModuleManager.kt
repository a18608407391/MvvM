package com.buyer.redman.manager


@Suppress("detekt.UnsafeCast")
object FeatureManager {

    private  val modules = arrayOf("","")
    private const val PackagePrefix = "com.buyer"
    val kodeinModules = modules
        .map { "$PackagePrefix.$it.KodeinModule" }
        .map {
            try {
                Class.forName(it).kotlin.objectInstance as KodeinServiceModuleProvide
            } catch (e: ClassNotFoundException) {
                throw ClassNotFoundException("Kodein module class not found $it")
            }
        }
        .map { it.kodeinModule }
}