plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    id("com.gradle.develocity") version("3.17.2")
}
develocity {
    buildScan {
        publishing.onlyIf { "true".equals(System.getProperty("envIsCi")) }
        uploadInBackground.set(!"true".equals(System.getProperty("envIsCi")))
        termsOfUseUrl.set("https://gradle.com/terms-of-service")
        termsOfUseAgree.set("yes")
    }
}
rootProject.name = "maligna-parent"
include("maligna", "maligna-ui")
