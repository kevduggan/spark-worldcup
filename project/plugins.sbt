resolvers += Classpaths.sbtPluginReleases

resolvers += Resolver.sonatypeRepo("releases")

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.0.4")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.5.0")