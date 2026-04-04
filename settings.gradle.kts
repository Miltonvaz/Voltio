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

        // Repositorio de Cardinal (Para seguridad de PayPal)
        maven {
            url = uri("https://cardinalcommerceprod.jfrog.io/artifactory/android")
            credentials {
                val localProps = java.util.Properties()
                val localFile = file("local.properties")
                if (localFile.exists()) localProps.load(localFile.inputStream())
                username = localProps.getProperty("CARDINAL_USERNAME", "")
                password = localProps.getProperty("CARDINAL_PASSWORD", "")
            }
        }
    }
}
rootProject.name = "Voltio1"
include(":app")
