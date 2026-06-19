import java.lang.System.getenv

plugins {
	id("mod-platform")
	id("maven-publish")
	id("net.neoforged.moddev")
}

stonecutter {
	val (version, loader) = current.project.split('-', limit = 2)
	properties.tags(version, loader)

	replacements.string(current.parsed >= "1.21.11") {
		replace("ResourceLocation", "Identifier")
		replace("location()", "identifier()")
	}
}

platform {
	loader = "neoforge"
	dependencies {
		required("minecraft") {
			forgeLikeVersionRange = prop("deps.minecraft")
		}
		required("neoforge") {
			forgeLikeVersionRange.set("[1,)")
		}
	}
}

neoForge {
	version = prop("deps.neoforge")
	accessTransformers.from(rootProject.file("src/main/resources/aw/${stonecutter.current.version}.cfg"))
	validateAccessTransformers = true

	if (hasProperty("deps.parchment")) parchment {
		val (mc, ver) = prop("deps.parchment").split(':')
		mappingsVersion = ver
		minecraftVersion = mc
	}

	runs {
		register("client") {
			client()
			gameDirectory = file("run/")
			ideName = "NeoForge Client (${stonecutter.current.version})"
			programArgument("--username=Dev")
		}
		register("server") {
			server()
			gameDirectory = file("run/")
			ideName = "NeoForge Server (${stonecutter.current.version})"
		}
	}

	mods {
		register(prop("mod.id")) {
			sourceSet(sourceSets["main"])
		}
	}
	sourceSets["main"].resources.srcDir("${rootDir}/versions/datagen/${sc.current.version.split("-")[0]}/src/main/generated")
}

repositories {
	mavenCentral()
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
}

dependencies {
	implementation(libs.moulberry.mixinconstraints)
	jarJar(libs.moulberry.mixinconstraints)
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}

tasks.register("copyJar") {
	description = "Copy the jar to the root build directory"
	dependsOn("jar")
	doLast {
		copy {
			from(tasks.getByName("jar").outputs.files)
			into(rootProject.layout.buildDirectory.dir("libs"))
		}
	}
}

tasks.named("build") {
	dependsOn("copyJar")
}

publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			from(components["java"])
			artifactId = prop("mod.id")
			groupId = "dev.ultreon.mods"
		}
	}

	repositories {
		maven {
			name = "UltreonMavenReleases"
			url = uri("https://maven.ultreon.dev/releases")
			credentials {
				username = findProperty("ultreonmvn.name") as? String ?: getenv("ULTREON_MVN_NAME")
				password = findProperty("ultreonmvn.secret") as? String ?: getenv("ULTREON_MVN_SEC")
			}
			authentication {
				create("basic", BasicAuthentication::class.java )
			}
		}

		maven {
			name = "UltreonMavenSnapshots"
			url = uri("https://maven.ultreon.dev/snapshots")
			credentials {
				username = findProperty("ultreonmvn.name") as? String ?: getenv("ULTREON_MVN_NAME")
				password = findProperty("ultreonmvn.secret") as? String ?: getenv("ULTREON_MVN_SEC")
			}
			authentication {
				create("basic", BasicAuthentication::class.java)
			}
		}
	}
}
