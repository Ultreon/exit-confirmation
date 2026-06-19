import java.lang.System.getenv

plugins {
	id("mod-platform")
	id("maven-publish")
	id("net.neoforged.moddev.legacyforge")
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
	loader = "forge"
	dependencies {
		required("minecraft") {
			forgeLikeVersionRange = prop("deps.minecraft")
		}
		required("forge") {
			forgeLikeVersionRange.set("[1,)")
		}
	}
}

legacyForge {
	version = "${prop("deps.minecraft")}-${prop("deps.forge")}"

	validateAccessTransformers = true

	accessTransformers.from(
		rootProject.file("src/main/resources/aw/${sc.current.version}.cfg")
	)

	runs {
		register("client") {
			client()
			gameDirectory = file("run/")
			ideName = "Forge Client (${sc.current.version})"
			programArgument("--username=Dev")
		}
		register("server") {
			server()
			gameDirectory = file("run/")
			ideName = "Forge Server (${sc.current.version})"
		}
	}


	mods {
		register(prop("mod.id")) {
			sourceSet(sourceSets["main"])
		}
	}
}

mixin {
	add(sourceSets.main.get(), "${prop("mod.id")}.mixins.refmap.json")
	config("${prop("mod.id")}.mixins.json")
}

repositories {
	mavenCentral()
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
}

dependencies {
	annotationProcessor("org.spongepowered:mixin:${libs.versions.mixin.get()}:processor")

	 implementation(libs.moulberry.mixinconstraints)
	 jarJar(libs.moulberry.mixinconstraints)
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

sourceSets {
	main {
		resources.srcDir(
			"${rootDir}/versions/datagen/${sc.current.version.split("-")[0]}/src/main/generated"
		)
	}
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
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
