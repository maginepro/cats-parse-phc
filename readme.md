# :page_facing_up: cats-parse-phc

Support for parsing and generating password hashes in the [PHC string format](https://github.com/P-H-C/phc-string-format/blob/master/phc-sf-spec.md) using [cats-parse](https://github.com/typelevel/cats-parse).

## Usage

You can add the following line to `build.sbt` to use the library.

```scala
libraryDependencies += "com.magine" %% "cats-parse-phc" % catsParsePhcVersion
```

Make sure to replace `catsParsePhcVersion` with a [release version](https://github.com/maginepro/cats-parse-phc/releases).<br>
Replace `%%` with `%%%` if you are using [Scala.js](https://www.scala-js.org) or [Scala Native](https://scala-native.org).

## Parsing

Use `Phc.parse` on a PHC string to parse its contents.

```scala
import com.magine.cats.parse.phc.Phc

Phc.parse("$argon2id$v=19$m=65536,t=2,p=1$gZiV/M1gPc22ElAH/Jh1Hw$CWOrkoo7oJBQ/iyh7uJ0LO2aLEfrHwTWllSAxT0zRno")
// Either[Parser.Error,Phc] = Right(Phc(id = PhcId(argon2id), version = Some(PhcVersion(19)), params = List(PhcParam(m,65536), PhcParam(t,2), PhcParam(p,1)), salt = Some(PhcSalt(gZiV/M1gPc22ElAH/Jh1Hw)), hash = Some(PhcHash(CWOrkoo7oJBQ/iyh7uJ0LO2aLEfrHwTWllSAxT0zRno))))
```

There is a `phc` String-interpolator for compile-time parsing.

```scala
import com.magine.cats.parse.phc.syntax._

phc"$$argon2id$$v=19$$m=65536,t=2,p=1$$gZiV/M1gPc22ElAH/Jh1Hw$$CWOrkoo7oJBQ/iyh7uJ0LO2aLEfrHwTWllSAxT0zRno"
// Phc = Phc(id = PhcId(argon2id), version = Some(PhcVersion(19)), params = List(PhcParam(m,65536), PhcParam(t,2), PhcParam(p,1)), salt = Some(PhcSalt(gZiV/M1gPc22ElAH/Jh1Hw)), hash = Some(PhcHash(CWOrkoo7oJBQ/iyh7uJ0LO2aLEfrHwTWllSAxT0zRno)))
```

## Generating

`Phc` values can be created from its constituent parts, with String-interpolators available for the parts.

```scala
import com.magine.cats.parse.phc.Phc
import com.magine.cats.parse.phc.syntax._

val phc = Phc(
  id"argon2id",
  Some(version"19"),
  params"m=65536,t=2,p=1",
  salt"gZiV/M1gPc22ElAH/Jh1Hw",
  hash"CWOrkoo7oJBQ/iyh7uJ0LO2aLEfrHwTWllSAxT0zRno"
)
// Phc = Phc(id = PhcId(argon2id), version = Some(PhcVersion(19)), params = List(PhcParam(m,65536), PhcParam(t,2), PhcParam(p,1)), salt = Some(PhcSalt(gZiV/M1gPc22ElAH/Jh1Hw)), hash = Some(PhcHash(CWOrkoo7oJBQ/iyh7uJ0LO2aLEfrHwTWllSAxT0zRno)))

phc.show
// String = $argon2id$v=19$m=65536,t=2,p=1$gZiV/M1gPc22ElAH/Jh1Hw$CWOrkoo7oJBQ/iyh7uJ0LO2aLEfrHwTWllSAxT0zRno
```

## Bcrypt

[Bcrypt](https://en.wikipedia.org/wiki/Bcrypt) hashes can be parsed and converted to/from PHC string format.

```scala
import com.magine.cats.parse.phc.Bcrypt

val bcrypt = Bcrypt.parse("$2b$10$69SrwAoAUNC5F.gtLEvrNON6VQ5EX89vNqLEqU655Oy9PeT/HRM/a")
// Either[Parser.Error,Bcrypt] = Right(Bcrypt(version = BcryptVersion(2b), cost = BcryptCost(10), salt = BcryptSalt(69SrwAoAUNC5F.gtLEvrNO), hash = BcryptHash(N6VQ5EX89vNqLEqU655Oy9PeT/HRM/a)))

val phc = bcrypt.map(_.toPhc)
// Either[Parser.Error,Phc] = Right(Phc(id = PhcId(bcrypt), version = Some(PhcVersion(98)), params = List(PhcParam(r,10)), salt = Some(PhcSalt(8/UtyCqCWPE7HAivNGxtPQ)), hash = Some(PhcHash(P8XS7GZ+/xPsNGsW877Q0/RgVBJTOBc))))

phc.map(_.show)
// Either[Parser.Error, String] = Right($bcrypt$v=98$r=10$8/UtyCqCWPE7HAivNGxtPQ$P8XS7GZ+/xPsNGsW877Q0/RgVBJTOBc)

phc.flatMap(Bcrypt.fromPhc) == bcrypt
// Boolean = true
```

Note the conversion between Bcrypt and PHC string format is only lossless for versions 2a and 2b. This is because PHC string format only uses versions 97 (for Bcrypt versions 2 and 2a) and 98 (for Bcrypt versions 2b, 2x, and 2y).

There is a `bcrypt` String-interpolator for compile-time parsing.

```scala
import com.magine.cats.parse.phc.syntax._

bcrypt"$$2a$$12$$FoNsj8vKI9vE1/ijbIoe6evjenGlzFMTv3maAc2wtCc8Jhf.GmRkW"
// Bcrypt = Bcrypt(version = BcryptVersion(2a), cost = BcryptCost(12), salt = BcryptSalt(FoNsj8vKI9vE1/ijbIoe6e), hash = BcryptHash(vjenGlzFMTv3maAc2wtCc8Jhf.GmRkW))
```
