plugins {
    java
    `maven-publish`

    // keep in sync with upstream (https://github.com/PaperMC/Paper/blob/master/build.gradle.kts)
    id("io.papermc.paperweight.patcher") version "1.7.1"
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"

repositories {
    mavenCentral()
    maven(paperMavenPublicUrl) {
        content { onlyForConfigurations(configurations.paperclip.name) }
    }
}

dependencies {
    // keep in sync with upstream (https://github.com/PaperMC/Paper/blob/master/build.gradle.kts)
    remapper("net.fabricmc:tiny-remapper:0.10.3:fat")
    // keep in sync with upstream (https://github.com/PaperMC/Paper/blob/master/build.gradle.kts)
    decompiler("org.vineflower:vineflower:1.10.1")
    // keep in sync with upstream (https://github.com/PaperMC/Paper/blob/master/build.gradle.kts)
    paperclip("io.papermc:paperclip:3.0.3")
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }
}

subprojects {
    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
    }
    tasks.withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }
    tasks.withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }

    repositories {
        mavenCentral()
        maven(paperMavenPublicUrl)
    }
}

paperweight {
    serverProject = project(":josiepaper-server")

    remapRepo = paperMavenPublicUrl
    decompileRepo = paperMavenPublicUrl

    usePaperUpstream(providers.gradleProperty("paperRef")) {
        withPaperPatcher {
            apiPatchDir = layout.projectDirectory.dir("patches/api")
            apiOutputDir = layout.projectDirectory.dir("josiepaper-api")

            serverPatchDir = layout.projectDirectory.dir("patches/server")
            serverOutputDir = layout.projectDirectory.dir("josiepaper-server")
        }
        patchTasks.register("generatedApi") {
            isBareDirectory = true
            upstreamDirPath = "paper-api-generator/generated"
            patchDir = layout.projectDirectory.dir("patches/generatedApi")
            outputDir = layout.projectDirectory.dir("paper-api-generator/generated")
        }
    }
}

tasks.generateDevelopmentBundle {
    apiCoordinates = "josie.paper:josiepaper-api"
    libraryRepositories = listOf(
        "https://repo.maven.apache.org/maven2/",
        paperMavenPublicUrl,
    )
}
