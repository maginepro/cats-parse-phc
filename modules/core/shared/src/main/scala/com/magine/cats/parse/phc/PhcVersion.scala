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

sealed abstract case class PhcVersion(value: String) {
  def show: String =
    value
}

object PhcVersion {
  def apply(version: String): Either[PhcError, PhcVersion] =
    if (version.nonEmpty && version.forall(_.isDigit))
      Right(new PhcVersion(version) {})
    else
      Left(PhcError(s"Invalid PhcVersion: $version"))

  def fromInt(version: Int): Either[PhcError, PhcVersion] =
    apply(version.toString)

  def fromLong(version: Long): Either[PhcError, PhcVersion] =
    apply(version.toString)

  /**
    * Parses a [[PhcVersion]] with leading `\$v=` identification.
    */
  val parser: Parser[PhcVersion] =
    Parser.string("$v=") *> Parser
      .charIn('0' to '9')
      .repAs[String](min = 1)
      .mapFilter(apply(_).toOption)

  implicit val phcVersionHash: Hash[PhcVersion] =
    Hash.fromUniversalHashCode

  implicit val phcVersionShow: Show[PhcVersion] =
    Show.show(_.value)
}
