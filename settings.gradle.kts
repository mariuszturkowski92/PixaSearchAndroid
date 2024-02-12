pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "PixabaySearch"
include(":app")
include(":common:ui")
include(":data:images")
include(":feature:search")
include(":common:utils")
include(":data:images:network")
include(":common:networking")
include(":common:navigation")
include(":data:images:local")
include(":data:images:test")
