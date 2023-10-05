pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://jitpack.io")
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "io.papermc.paperweight.patcher") {
                useModule("com.github.PaperMC.paperweight:paperweight-patcher:4803719449") // can also be a branch, i.e. `master-SNAPSHOT`
            }
        }
    }
}

rootProject.name = "ultrapaper"

include("ultrapaper-api", "ultrapaper-server")
