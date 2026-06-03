import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.Effort

plugins {
    id("java")
    checkstyle
    jacoco
    id("com.github.spotbugs") version "6.0.25"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("info.solidsoft.pitest") version "1.15.0"
    application
}

group = "nu.csse.sqe"
version = "1.0"

repositories {
    mavenCentral()
}

javafx {
    version = "17.0.10" // Highly stable version that fully supports Java 11
    modules = listOf("javafx.controls") // Pulls in critical UI components like Button, Scene, and Stage
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // cucumber
    testImplementation(platform("io.cucumber:cucumber-bom:7.20.1"))
    testImplementation("io.cucumber:cucumber-java")
    testImplementation("io.cucumber:cucumber-junit-platform-engine")
    testImplementation("org.easymock:easymock:5.4.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

tasks.compileJava {
    options.release = 11
}

tasks.test {
    useJUnitPlatform()
}

// Checkstyle: Google Java Style base, customized in config/checkstyle/checkstyle.xml.
// Strict from day one — `./gradlew build` fails on any violation. Existing
// violations in main-branch code are owned by each file's author; refactor PRs
// land on each owner's feature branch (e.g., `feat/jixin-card` for Card).
checkstyle {
    toolVersion = "10.18.2"
    isIgnoreFailures = false
}

tasks.withType<Checkstyle>().configureEach {
    reports {
        xml.required = false
        html.required = true
        html.stylesheet =
            resources.text.fromFile("config/xsl/checkstyle-noframes-severity-sorted.xsl")
    }
}

// SpotBugs: default effort + confidence, HTML report only. Strict from day one.
spotbugs {
    ignoreFailures = false
    showStackTraces = true
    showProgress = true
    effort = Effort.DEFAULT
    reportLevel = Confidence.DEFAULT
    maxHeapSize = "1g"
    excludeFilter.set(file("config/spotbugs/excludeFilter.xml"))
}

tasks.spotbugsMain {
    reports.create("html") {
        required = true
        outputLocation = layout.buildDirectory.file("reports/spotbugs/spotbugs-main.html")
        setStylesheet("fancy-hist.xsl")
    }
}

tasks.spotbugsTest {
    reports.create("html") {
        required = true
        outputLocation = layout.buildDirectory.file("reports/spotbugs/spotbugs-test.html")
        setStylesheet("fancy-hist.xsl")
    }
}

val cucumberRuntime by configurations.creating {
    extendsFrom(configurations["testImplementation"])
}

task("cucumber") {
    dependsOn("assemble", "compileTestJava")
    doLast {
        javaexec {
            mainClass.set("io.cucumber.core.cli.Main")
            classpath = cucumberRuntime + sourceSets.main.get().output + sourceSets.test.get().output
            args = listOf("--plugin", "pretty",
                "--glue", "domain",  // where the step definitions are.
                "src/test/resources")                    // where the feature files are.
            // Configure jacoco agent for the test coverage.
            val jacocoAgent = zipTree(configurations.jacocoAgent.get().singleFile)
                .filter { it.name == "jacocoagent.jar" }
                .singleFile
            jvmArgs = listOf("-javaagent:$jacocoAgent=destfile=$buildDir/results/jacoco/cucumber.exec,append=false")
        }
    }
}

tasks.jacocoTestReport {
    // Give jacoco the file generated with the cucumber tests for the coverage.
    executionData(files("$buildDir/jacoco/test.exec", "$buildDir/results/jacoco/cucumber.exec"))
    reports {
        xml.required.set(true)
    }
}

application {
    mainClass.set("ui.MainApp") // 👈 Points Gradle to your main class
}

// PIT mutation testing (info.solidsoft.pitest). Mirrors the Code-Coverage lab
// setup, adapted to this project: production classes live in the `domain`
// package (no group prefix), so target `domain.*` rather than the default
// "${project.group}.*". `build` runs PIT so the HTML mutation report
// (build/reports/pitest/) is regenerated on every full build.
pitest {
    targetClasses = setOf("domain.*")
    targetTests = setOf("domain.*")
    junit5PluginVersion = "1.2.1"
    pitestVersion = "1.15.0"

    threads = 4
    outputFormats = setOf("HTML")
    timestampedReports = false
    testSourceSets.set(listOf(sourceSets.test.get()))
    mainSourceSets.set(listOf(sourceSets.main.get()))
    jvmArgs.set(listOf("-Xmx1024m"))
    useClasspathFile.set(true)
    fileExtensionsToFilter.addAll("xml")
    exportLineCoverage = true
}

tasks.build {
    dependsOn(tasks.pitest)
}