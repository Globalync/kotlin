package org.jetbrains.kotlin.tools.projectWizard.moduleConfigurators

import org.jetbrains.kotlin.tools.projectWizard.ir.buildsystem.KotlinBuildSystemPluginIR
import org.jetbrains.kotlin.tools.projectWizard.plugins.kotlin.ModuleConfigurationData
import org.jetbrains.kotlin.tools.projectWizard.plugins.kotlin.ModuleType
import org.jetbrains.kotlin.tools.projectWizard.settings.buildsystem.Module
import org.jetbrains.kotlin.tools.projectWizard.settings.buildsystem.ModuleKind

interface JvmModuleConfigurator : ModuleConfigurator
interface AndroidModuleConfigurator : ModuleConfigurator


object MppModuleConfigurator : ModuleConfigurator {
    override val moduleType = ModuleType.jvm // TODO
    override val moduleKind = ModuleKind.multiplatform
    override val suggestedModuleName = "shared"
    override val id = "multiplatform"
    override val text = "Multiplatform"
    override val canContainSubModules = true

    override fun createKotlinPluginIR(configurationData: ModuleConfigurationData, module: Module): KotlinBuildSystemPluginIR? =
        KotlinBuildSystemPluginIR(
            KotlinBuildSystemPluginIR.Type.multiplatform,
            version = configurationData.kotlinVersion
        )
}


interface SinglePlatformModuleConfigurator : ModuleConfigurator {
    override val moduleKind get() = ModuleKind.singleplatformJvm

}

object JvmSinglePlatformModuleConfigurator : ModuleConfiguratorWithTests(),
    SinglePlatformModuleConfigurator,
    JvmModuleConfigurator {
    override val moduleType get() = ModuleType.jvm
    override val suggestedModuleName = "jvm"
    override val id = "JVM Module"

    override fun defaultTestFramework(): KotlinTestFramework = KotlinTestFramework.JUNIT4

    override val canContainSubModules = true

    override fun createKotlinPluginIR(configurationData: ModuleConfigurationData, module: Module): KotlinBuildSystemPluginIR? =
        KotlinBuildSystemPluginIR(
            KotlinBuildSystemPluginIR.Type.jvm,
            version = configurationData.kotlinVersion
        )
}


object IOSSinglePlatformModuleConfigurator :
    SinglePlatformModuleConfigurator {
    override val moduleType get() = ModuleType.jvm //todo
    override val id = "IOS Module"
    override val suggestedModuleName = "ios"
    override val greyText = "Requires Apple Xcode"
}


val ModuleType.defaultTarget
    get() = when (this) {
        ModuleType.jvm -> JvmTargetConfigurator
        ModuleType.js -> JsBrowserTargetConfigurator
        ModuleType.native -> NativeForCurrentSystemTarget
        ModuleType.common -> CommonTargetConfigurator
    }