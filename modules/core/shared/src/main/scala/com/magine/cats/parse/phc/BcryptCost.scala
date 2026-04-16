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
import cats.syntax.all.*

sealed abstract case class BcryptCost(value: Int) {
  final def show: String =
    value.show

  final def toPhc: PhcParam =
    safe(PhcParam("r", value.show))
}

object BcryptCost {
  def apply(cost: String): Either[PhcError, BcryptCost] =
    cost.toIntOption
      .toRight(PhcError(s"Invalid BcryptCost: $cost"))
      .flatMap(fromInt)

  def fromInt(cost: Int): Either[PhcError, BcryptCost] =
    if (4 <= cost && cost <= 31) Right(new BcryptCost(cost) {})
    else Left(PhcError(s"Invalid BcryptCost: $cost"))

  /**
    * Parses a [[BcryptCost]] with a leading `$` sign.
    */
  val parser: Parser[BcryptCost] =
    Parser.char('$') *> Parser
      .charIn('0' to '9')
      .repAs[String](min = 1)
      .mapFilter(apply(_).toOption)

  implicit val bcryptCostHash: Hash[BcryptCost] =
    Hash.fromUniversalHashCode

  implicit val bcryptCostShow: Show[BcryptCost] =
    Show.show(_.show)
}
