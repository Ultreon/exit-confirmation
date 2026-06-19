import java.lang.System.getenv

plugins {
	id("mod-platform")
	id("maven-publish")
	id("net.fabricmc.fabric-loom-remap")
}

stonecutter {
	val (version, loader) = current.project.split('-', limit = 2)
	properties.tags(version, loader)

	replacements.string(current.parsed >= "1.21.11") {
		replace("ResourceLocation", "Identifier")
		replace("location()", "identifier()")
	}
	replacements.string(current.parsed >= "26.1.2") {
		replace("FabricDataOutput", "FabricPackOutput")
	}
}

platform {
	loader = "fabric-o"
	dependencies {
		required("minecraft") {
			fabricLikeVersionRange = prop("deps.minecraft")
		}
		required("fabric-api") {
			slug("fabric-api")
			fabricLikeVersionRange = ">=${prop("deps.fabric-api")}"
		}
		required("fabricloader") {
			fabricLikeVersionRange = ">=${prop("deps.fabric-loader")}"
		}
		optional("modmenu") {}
	}
}

loom {
	mixin {
		useLegacyMixinAp = true
		defaultRefmapName.set("${prop("mod.id")}.mixins.refmap.json")
	}
	accessWidenerPath = rootProject.file("src/main/resources/aw/${sc.current.version}.accesswidener")
	runs.named("client") {
		client()
		ideConfigGenerated(true)
		runDir = "run/"
		environment = "client"
		programArgs("--username=Dev")
		configName = "Fabric Client"
	}
	runs.named("server") {
		server()
		ideConfigGenerated(true)
		runDir = "run/"
		environment = "server"
		configName = "Fabric Server"
	}
}

fabricApi {
	configureDataGeneration {
		outputDirectory = file("${rootDir}/versions/datagen/${sc.current.version.split("-")[0]}/src/main/generated")
		client = true
	}
}

repositories {
	mavenCentral()
	strictMaven("https://maven.terraformersmc.com/", "com.terraformersmc") { name = "TerraformersMC" }
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
}

configurations.all {
	resolutionStrategy {
		force("net.fabricmc:fabric-loader:${prop("deps.fabric-loader")}")
	}
}

dependencies {
	minecraft("com.mojang:minecraft:${prop("deps.minecraft")}")
	mappings(
		loom.layered {
			officialMojangMappings()
			if (hasProperty("deps.parchment")) parchment("org.parchmentmc.data:parchment-${prop("deps.parchment")}@zip")
		})
	modImplementation("net.fabricmc:fabric-loader:${prop("deps.fabric-loader")}")
	 implementation(libs.moulberry.mixinconstraints)
	 include(libs.moulberry.mixinconstraints)
	modImplementation("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric-api")}")
	modLocalRuntime("com.terraformersmc:modmenu:${prop("deps.modmenu")}")
}

tasks.register("copyJar") {
	description = "Copy the jar to the root build directory"
	dependsOn("remapJar")
	doLast {
		copy {
			from(tasks.getByName("remapJar").outputs.files)
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
