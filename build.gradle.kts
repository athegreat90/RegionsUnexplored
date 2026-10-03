plugins {
    kotlin("jvm") version "2.1.21"
    id("earth.terrarium.cloche") version "0.19.11"
}

repositories {
    cloche.librariesMinecraft()
    mavenCentral()
    cloche {
        main()
        mavenNeoforgedMeta()
        mavenNeoforged()
        mavenFabric()
    }
    maven("https://api.modrinth.com/maven")
    maven("https://maven.terraformersmc.com/")
    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
}

group = "net.regions_unexplored"
version = "0.7.0+beta2-26.3"

// Required dependencies
val lithostitchedVersion = "2.0.4"
val apollibFabricVersion = "1.2.4"
val apollibNeoforgeVersion = "1.2.5"

// Optional dependencies
// wikiful has no published build past 26.2 yet; usages below are commented out until 26.3 is available.
val wikifulVersion = "0.3.2"

cloche {
    metadata {
        modId = "regions_unexplored"
        name = "Regions Unexplored"
        description = "A stack of new biomes spread across the Overworld and Nether!"
        license = "All Rights Reserved"
        icon = "pack.png"

        url = "https://modrinth.com/mod/regions-unexplored"
        issues = "https://github.com/Apollounknowndev/RegionsUnexplored/issues"
        sources = "https://github.com/Apollounknowndev/RegionsUnexplored"

        author("Apollo")
        author("UHQ_Games")
        contributor("KelloVerra (Texture Artist)")
        contributor("KirboSoftware")
        contributor("voidsongdragonfly")
    }

    common {
        mixins.from(file("src/common/main/regions_unexplored.mixins.json"))
        accessWideners.from(file("src/common/main/regions_unexplored.accesswidener"))

        dependencies {
            compileOnly("org.spongepowered:mixin:0.8.5")
            implementation("de.marhali:json5-java:3.0.0")
            implementation("com.electronwill.night-config:core:3.8.3")
            implementation("com.electronwill.night-config:toml:3.8.3")
            modCompileOnlyApi("maven.modrinth:lithostitched:$lithostitchedVersion-neoforge-26.3")
            modCompileOnlyApi("maven.modrinth:apollib:$apollibNeoforgeVersion-neoforge-26.3")
        }

        data()

        metadata {
            dependencies {
                dependency {
                    modId = "lithostitched"
                    version(lithostitchedVersion)
                }
            }
        }
    }

    fabric {
        mixins.from(file("src/fabric/main/regions_unexplored.fabric.mixins.json"))

        loaderVersion = "0.19.5"
        minecraftVersion = "26.3"

        dependencies {
            fabricApi("0.161.0")

            include("de.marhali:json5-java:3.0.0")
            include("com.electronwill.night-config:core:3.8.3")
            include("com.electronwill.night-config:toml:3.8.3")

            // world-preview-prime has no published build past 26.1.2 yet.
            // modRuntimeOnly("maven.modrinth:world-preview-prime:2.0.0-fabric-26.1")
            modImplementation("maven.modrinth:lithostitched:$lithostitchedVersion-fabric-26.3")
            // Lithostitched jar-in-jars apollib, but the dev runtime classpath doesn't extract
            // nested jars, so it needs declaring explicitly or VillagerTypeMixin throws
            // NoClassDefFoundError on dev.worldgen.apollib.config.ApollibCopyable at startup.
            modImplementation("maven.modrinth:apollib:$apollibFabricVersion-fabric-26.3")
            // wikiful has no published build past 26.2 yet.
            // modImplementation("maven.modrinth:wikiful:$wikifulVersion-fabric-26.1")

            modImplementation("com.terraformersmc:modmenu:21.0.0")
        }

        datagenDirectory = file("src/common/main/generated")
        datagenClientDirectory = file("src/common/main/generated")

        includedClient()
        runs {
            client()
            server()
        }

        metadata {
            entrypoint("main") {
                value = "net.regions_unexplored.RegionsUnexploredFabric"
            }
            entrypoint("client") {
                value = "net.regions_unexplored.client.RegionsUnexploredFabricClient"
            }
            entrypoint("modmenu") {
                value = "net.regions_unexplored.compat.ModMenuIntegration"
            }
        }
    }

    neoforge {
        mixins.from(file("src/neoforge/main/regions_unexplored.neoforge.mixins.json"))
        loaderVersion = "26.3.0.41-beta"
        minecraftVersion = "26.3"

        dependencies {
            legacyClasspath("de.marhali:json5-java:3.0.0")
            include("de.marhali:json5-java:3.0.0")
            // world-preview-prime has no published build past 26.1.2 yet.
            // modRuntimeOnly("maven.modrinth:world-preview-prime:2.0.0-neoforge-26.1")
            modApi("maven.modrinth:lithostitched:$lithostitchedVersion-neoforge-26.3")
            modApi("maven.modrinth:apollib:$apollibNeoforgeVersion-neoforge-26.3")
            // wikiful has no published build past 26.2 yet.
            // modApi("maven.modrinth:wikiful:$wikifulVersion-neoforge-26.1")
        }


        data {
            dependencies {

            }
        }

        datagenDirectory = file("src/common/main/generated")
        datagenClientDirectory = file("src/common/main/generated")

        runs {
            client()
            server()
            clientData()
        }

        metadata {
            withToml {
                withContents {
                    @Suppress("UNCHECKED_CAST")
                    val mods = this["mods"] as? MutableList<Any?>
                    if (mods != null) {
                        for (i in mods.indices) {
                            val mod = mods[i] as? Map<*, *> ?: continue
                            if (mod.containsKey("logoFile")) {
                                val rebuilt = LinkedHashMap<Any?, Any?>()
                                for ((key, value) in mod) {
                                    if (key == "logoFile") {
                                        rebuilt["iconFile"] = value
                                    } else {
                                        rebuilt[key] = value
                                    }
                                }
                                mods[i] = rebuilt
                            }
                        }
                    }
                }
            }
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.addAll(listOf("-Xmaxerrs", "10000"))
}

val generateData = tasks.named("runNeoforgeClientData") {
    doLast {
        // The old provider cache cannot remove files from renamed registries.
        // Only prune these after the replacement registries were generated.
        val data = file("src/common/main/generated/data/regions_unexplored")
        check(data.resolve("worldgen/feature").isDirectory)
        check(data.resolve("worldgen/material_rule").isDirectory)
        delete(data.resolve("worldgen/configured_feature"), data.resolve("lithostitched/surface_rule"))
    }
}

tasks.withType<Jar>().configureEach {
    mustRunAfter(generateData)
}
