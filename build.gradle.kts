plugins {
  `java-library`
  `maven-publish`
  signing
  jacoco
  id("com.diffplug.spotless") version "6.25.0"
  id("org.jreleaser") version "1.24.0"
}

group = "com.vzaps"

version = "0.1.1"

description = "Official Java SDK for the VZaps public API"

java {
  toolchain { languageVersion.set(JavaLanguageVersion.of(11)) }
  withSourcesJar()
  withJavadocJar()
}

tasks.withType<JavaCompile>().configureEach {
  options.encoding = "UTF-8"
  options.release.set(11)
}

dependencies {
  api("com.fasterxml.jackson.core:jackson-databind:2.17.2")
  api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.2")

  testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
  testImplementation("org.assertj:assertj-core:3.26.3")
  testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
}

spotless {
  java {
    googleJavaFormat("1.22.0")
    target("src/**/*.java", "examples/**/*.java")
  }
  kotlinGradle { ktfmt() }
}

tasks.test {
  useJUnitPlatform()
  finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
  dependsOn(tasks.test)
  reports {
    xml.required.set(true)
    html.required.set(true)
  }
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
      artifactId = "vzaps-sdk"

      pom {
        name.set("VZaps Java SDK")
        description.set(project.description)
        url.set("https://github.com/VZaps/vzaps-sdk-java")
        licenses {
          license {
            name.set("MIT License")
            url.set("https://opensource.org/license/mit")
          }
        }
        developers {
          developer {
            id.set("vzaps")
            name.set("VZaps")
            email.set("dev@vzaps.com")
            organization.set("VZaps")
            organizationUrl.set("https://vzaps.com")
          }
        }
        scm {
          connection.set("scm:git:https://github.com/VZaps/vzaps-sdk-java.git")
          developerConnection.set("scm:git:ssh://git@github.com:VZaps/vzaps-sdk-java.git")
          url.set("https://github.com/VZaps/vzaps-sdk-java")
        }
      }
    }
  }
  repositories {
    maven {
      name = "staging"
      url = layout.buildDirectory.dir("staging-deploy").get().asFile.toURI()
    }
  }
}

signing {
  val signingKey = findProperty("signingKey") as String? ?: System.getenv("SIGNING_KEY")
  val signingPassword =
      findProperty("signingPassword") as String? ?: System.getenv("SIGNING_PASSWORD")
  if (!signingKey.isNullOrBlank()) {
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["mavenJava"])
  }
}

val releaseVersion = version.toString()

jreleaser {
  project {
    name.set("vzaps-sdk")
    description.set("Official Java SDK for the VZaps public API")
    website.set("https://docs.vzaps.com")
    authors.set(listOf("VZaps"))
    license.set("MIT")
    version.set(releaseVersion)
    java {
      groupId.set("com.vzaps")
      artifactId.set("vzaps-sdk")
    }
  }
  signing {
    active.set(org.jreleaser.model.Active.RELEASE)
    armored.set(true)
  }
  deploy {
    maven {
      mavenCentral {
        create("sonatype") {
          active.set(org.jreleaser.model.Active.RELEASE)
          url.set("https://central.sonatype.com/api/v1/publisher")
          stagingRepository("build/staging-deploy")
        }
      }
    }
  }
}
