pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://www.jitpack.io")}
    }
}

rootProject.name = "Bilibili_neko"
include(":app")
//基础依赖
include(":lib_common")
include(":lib_framework")
include(":lib_glide")
include(":lib_network")
include(":lib_stater")
//业务模块分类
include(":mod_login")
include(":mod_subscription")
include(":mod_video")
include(":mod_search")
include(":mod_user")
include(":lib_room")
