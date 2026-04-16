/*
 * Copyright 2026 Magine Pro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.magine.cats.parse.phc

import cats.Hash
import cats.Show
import cats.parse.Parser

sealed abstract case class BcryptVersion(value: String) {
  def toPhc: PhcVersion

  final def show: String =
    value
}

object BcryptVersion {
  val `2`: BcryptVersion =
    new BcryptVersion("2") {
      override val toPhc: PhcVersion =
        safe(PhcVersion("97"))
    }

  val `2a`: BcryptVersion =
    new BcryptVersion("2a") {
      override val toPhc: PhcVersion =
        safe(PhcVersion("97"))
    }

  val `2b`: BcryptVersion =
    new BcryptVersion("2b") {
      override val toPhc: PhcVersion =
        safe(PhcVersion("98"))
    }

  val `2x`: BcryptVersion =
    new BcryptVersion("2x") {
      override val toPhc: PhcVersion =
        safe(PhcVersion("98"))
    }

  val `2y`: BcryptVersion =
    new BcryptVersion("2y") {
      override val toPhc: PhcVersion =
        safe(PhcVersion("98"))
    }

  def apply(version: String): Either[PhcError, BcryptVersion] =
    version match {
      case "2" => Right(`2`)
      case "2a" => Right(`2a`)
      case "2b" => Right(`2b`)
      case "2x" => Right(`2x`)
      case "2y" => Right(`2y`)
      case _ => Left(PhcError(s"Invalid BcryptVersion: $version"))
    }

  /**
    * Returns bcrypt 2a for version 97 and bcrypt 2b for version 98.
    *
    * Note bcrypt versions 2 and 2a use 97 for [[PhcVersion]], while
    * bcrypt versions 2b, 2x, and 2y use 98 for [[PhcVersion]]. This
    * means the conversion between [[Bcrypt]] and [[Phc]] is lossy.
    */
  def fromPhc(version: PhcVersion): Either[PhcError, BcryptVersion] =
    version.value match {
      case "97" => Right(`2a`)
      case "98" => Right(`2b`)
      case _ => Left(PhcError(s"Invalid BcryptVersion for PhcVersion: $version"))
    }

  /**
    * Parses a [[BcryptVersion]] with a leading `$` sign.
    */
  val parser: Parser[BcryptVersion] =
    Parser.char('$') *> Parser.oneOf(
      Parser.string("2a").as(`2a`) ::
        Parser.string("2b").as(`2b`) ::
        Parser.string("2x").as(`2x`) ::
        Parser.string("2y").as(`2y`) ::
        Parser.char('2').as(`2`) ::
        Nil
    )

  implicit val bcryptVersionHash: Hash[BcryptVersion] =
    Hash.fromUniversalHashCode

  implicit val bcryptVersionShow: Show[BcryptVersion] =
    Show.show(_.show)
}
