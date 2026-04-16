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

sealed abstract case class PhcId(value: String) {
  def show: String =
    value
}

object PhcId {
  def apply(id: String): Either[PhcError, PhcId] =
    if (isValidId(id)) Right(new PhcId(id) {})
    else Left(PhcError(s"Invalid PhcId: $id"))

  private def isValidId(id: String): Boolean =
    1 <= id.length && id.length <= 32 && id.forall(c =>
      ('a' <= c && c <= 'z') ||
        ('0' <= c && c <= '9') ||
        c == '-'
    )

  /**
    * Parses a [[PhcId]] with a leading `$` sign.
    */
  val parser: Parser[PhcId] =
    Parser.char('$') *> Parser
      .charIn(('a' to 'z') ++ ('0' to '9') :+ '-')
      .repAs[String](min = 1, max = 32)
      .mapFilter(apply(_).toOption)

  implicit val phcIdHash: Hash[PhcId] =
    Hash.fromUniversalHashCode

  implicit val phcIdShow: Show[PhcId] =
    Show.show(_.value)
}
