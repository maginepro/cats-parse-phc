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
import scodec.bits.ByteVector

sealed abstract case class BcryptSalt(bytes: ByteVector) {
  final def toBcryptBase64: String =
    bytes.toBase64(BcryptBase64Alphabet)

  final def show: String =
    toBcryptBase64

  override final def toString: String =
    s"BcryptSalt($toBcryptBase64)"

  final def toPhc: PhcSalt =
    safe(PhcSalt.toBase64NoPad(bytes))
}

object BcryptSalt {
  def apply(hash: String): Either[PhcError, BcryptSalt] =
    fromBcryptBase64(hash)

  def fromBcryptBase64(hash: String): Either[PhcError, BcryptSalt] =
    if (hash.length == 22)
      ByteVector
        .fromBase64(hash, BcryptBase64Alphabet)
        .toRight(PhcError(s"Invalid BcryptSalt: $hash"))
        .map(new BcryptSalt(_) {})
    else Left(PhcError(s"Invalid BcryptSalt: $hash"))

  def fromBytes(bytes: ByteVector): Either[PhcError, BcryptSalt] =
    if (bytes.size == 16L) Right(new BcryptSalt(bytes) {})
    else Left(PhcError(s"Invalid BcryptSalt: $bytes"))

  /**
    * Parses a [[BcryptSalt]] with a leading `$` sign.
    */
  val parser: Parser[BcryptSalt] =
    Parser.char('$') *> Parser
      .charIn(Seq('.', '/') ++ ('A' to 'Z') ++ ('a' to 'z') ++ ('0' to '9'))
      .repExactlyAs[String](22)
      .mapFilter(apply(_).toOption)

  implicit val bcryptSaltHash: Hash[PhcSalt] =
    Hash.fromUniversalHashCode

  implicit val bcryptSaltShow: Show[PhcSalt] =
    Show.show(_.show)
}
