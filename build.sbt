val catsParseVersion = "1.1.0"
val catsVersion = "2.13.0"
val literallyVersion = "1.2.0"
val munitScalaCheckVersion = "1.2.0"
val scala213Version = "2.13.18"
val scala3Version = "3.3.7"
val scodecBitsVersion = "1.2.4"

inThisBuild(
  Seq(
    crossScalaVersions := Seq(scala213Version, scala3Version),
    developers := List(tlGitHubDev("vlovgr", "Viktor Rudebeck")),
    githubWorkflowJavaVersions := Seq(JavaSpec.temurin("17")),
    githubWorkflowTargetBranches := Seq("**"),
    licenses := Seq(License.Apache2),
    organization := "com.magine",
    organizationName := "Magine Pro",
    scalaVersion := scala3Version,
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    startYear := Some(2026),
    tlBaseVersion := "2.0",
    tlCiHeaderCheck := true,
    tlCiScalafixCheck := true,
    tlCiScalafmtCheck := true,
    tlFatalWarnings := true,
    tlJdkRelease := Some(8),
    versionScheme := Some("early-semver")
  )
)

lazy val root = tlCrossRootProject
  .aggregate(core)

lazy val core = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .in(file("modules/core"))
  .settings(
    name := "cats-parse-phc",
    libraryDependencies ++= Seq(
      "org.scodec" %%% "scodec-bits" % scodecBitsVersion,
      "org.typelevel" %%% "cats-core" % catsVersion,
      "org.typelevel" %%% "cats-kernel" % catsVersion,
      "org.typelevel" %%% "cats-parse" % catsParseVersion,
      "org.typelevel" %%% "literally" % literallyVersion,
      "org.scalameta" %%% "munit-scalacheck" % munitScalaCheckVersion % Test
    )
  )
