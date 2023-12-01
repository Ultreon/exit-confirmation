import org.jetbrains.gradle.ext.Application
import org.jetbrains.gradle.ext.runConfigurations
import org.jetbrains.gradle.ext.settings
import java.nio.file.Files
import java.nio.file.StandardOpenOption

//file:noinspection GroovyUnusedCatchParameter
buildscript {
    dependencies {
        classpath("gradle.plugin.org.jetbrains.gradle.plugin.idea-ext:gradle-idea-ext:1.1.7")
    }
}

plugins {
    id("idea")
    id("java")
    id("java-library")
}

apply(plugin = "org.jetbrains.gradle.plugin.idea-ext")

group = "com.ultreon.craftmods"
version = "0.1.0"

val ultracraftVersion = "dev-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://maven.fabricmc.net/")
    maven("https://github.com/Ultreon/ultreon-data/raw/main/.mvnrepo/")
    maven("https://github.com/Ultreon/corelibs/raw/main/.mvnrepo/")
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.Ultreon.ultracraft:client:$ultracraftVersion")
    implementation("com.github.Ultreon.ultracraft:desktop:$ultracraftVersion")
    implementation("com.github.Ultreon.ultracraft:server:$ultracraftVersion")
    implementation("com.github.Ultreon.ultracraft:gameprovider:$ultracraftVersion")

    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:1.12.0")
    implementation("com.badlogicgames.gdx:gdx-platform:1.12.0:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-box2d-platform:1.12.0:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-bullet-platform:1.12.0:natives-desktop")
    implementation("com.badlogicgames.gdx-controllers:gdx-controllers-desktop:2.2.1")
    implementation("com.badlogicgames.gdx:gdx-freetype-platform:1.12.0:natives-desktop")
    implementation("com.badlogicgames.gdx-video:gdx-video-lwjgl3:1.3.2-SNAPSHOT")
}

fun setupIdea() {
    mkdir("$projectDir/build/gameutils")
    mkdir("$projectDir/run")
    mkdir("$projectDir/run/client")
    mkdir("$projectDir/run/client/alt")
    mkdir("$projectDir/run/client/main")
    mkdir("$projectDir/run/server")

    val ps = File.pathSeparator!!
    val files = configurations["runtimeClasspath"]!!.files
    files += tasks.compileJava.get().outputs.files.files

    val classPath = files.asSequence()
        .filter { it != null }
        .map { it.path }
        .joinToString(ps)

    //language=TEXT
    val conf = """
commonProperties
	fabric.development=true
    fabric.skipMcProvider=true
	fabric.log.disableAnsi=false
	log4j2.formatMsgNoLookups=true
    """.trimIndent()
    val launchFile = file("$projectDir/build/gameutils/launch.cfg")
    Files.writeString(
        launchFile.toPath(),
        conf,
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.WRITE
    )

    val cpFile = file("$projectDir/build/gameutils/classpath.txt")
    Files.writeString(
        cpFile.toPath(),
        classPath,
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.WRITE
    )

    idea {
        project {
            settings {
                withIDEADir {
                    println("Callback 1 executed with: $absolutePath")
                }

                runConfigurations {
                    create(
                        "Ultracraft Client",
                        Application::class.java
                    ) {                       // Create new run configuration "MyApp" that will run class foo.App
                        jvmArgs =
                            "-Xmx2g -Dfabric.dli.config=${launchFile.path} -Dfabric.dli.env=CLIENT -Dfabric.dli.main=net.fabricmc.loader.impl.launch.knot.KnotClient"
                        mainClass = "net.fabricmc.devlaunchinjector.Main"
                        moduleName = idea.module.name + ".main"
                        workingDirectory = "$projectDir/run/client/main/"
                        programParameters = "--gameDir=."
                    }
                    create(
                        "Ultracraft Client Alt",
                        Application::class.java
                    ) {                       // Create new run configuration "MyApp" that will run class foo.App
                        jvmArgs =
                            "-Xmx2g -Dfabric.dli.config=${launchFile.path} -Dfabric.dli.env=CLIENT -Dfabric.dli.main=net.fabricmc.loader.impl.launch.knot.KnotClient"
                        mainClass = "net.fabricmc.devlaunchinjector.Main"
                        moduleName = idea.module.name + ".main"
                        workingDirectory = "$projectDir/run/client/alt/"
                        programParameters = "--gameDir=."
                    }
                    create(
                        "Ultracraft Server",
                        Application::class.java
                    ) {                       // Create new run configuration "MyApp" that will run class foo.App
                        jvmArgs =
                            "-Xmx2g -Dfabric.dli.config=${launchFile.path} -Dfabric.dli.env=SERVER -Dfabric.dli.main=net.fabricmc.loader.impl.launch.knot.KnotClient"
                        mainClass = "net.fabricmc.devlaunchinjector.Main"
                        moduleName = idea.module.name + ".main"
                        workingDirectory = "$projectDir/run/server/"
                        programParameters = "--gameDir=."
                    }
                }
            }
        }
    }
    idea {
        module {
            isDownloadJavadoc = true
            isDownloadSources = true
        }
    }
}

this.setupIdea()
