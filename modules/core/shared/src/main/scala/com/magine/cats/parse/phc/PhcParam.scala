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

sealed abstract case class PhcParam(name: String, value: String) {
  def show: String =
    s"$name=$value"
}

object PhcParam {
  def apply(name: String, value: String): Either[PhcError, PhcParam] =
    if (isValidName(name))
      if (isValidValue(value)) Right(new PhcParam(name, value) {})
      else Left(PhcError(s"Invalid PhcParam value: $value"))
    else Left(PhcError(s"Invalid PhcParam name: $name"))

  private def isValidName(name: String): Boolean =
    1 <= name.length && name.length <= 32 && name.forall(c =>
      ('a' <= c && c <= 'z') ||
        ('0' <= c && c <= '9') ||
        c == '-'
    ) && name != "v"

  private def isValidValue(value: String): Boolean =
    value.nonEmpty && value.forall(c =>
      ('a' <= c && c <= 'z') ||
        ('A' <= c && c <= 'Z') ||
        ('0' <= c && c <= '9') ||
        c == '/' || c == '+' || c == '.' || c == '-'
    )

  /**
    * Parses a [[PhcParam]] with name and value separated by `=` (equals).
    */
  val parser: Parser[PhcParam] = {
    val name = Parser
      .charIn(('a' to 'z') ++ ('0' to '9') :+ '-')
      .repAs[String](min = 1, max = 32)
      .filter(_ != "v")

    val value = Parser.char('=') *> Parser
      .charIn(('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9') ++ Seq('/', '+', '.', '-'))
      .repAs[String]

    (name ~ value).mapFilter { case (name, value) => apply(name, value).toOption }
  }

  def parse(param: String): Either[Parser.Error, PhcParam] =
    parser.parseAll(param)

  implicit val phcParamHash: Hash[PhcParam] =
    Hash.fromUniversalHashCode

  implicit val phcParamShow: Show[PhcParam] =
    Show.show(_.show)
}
