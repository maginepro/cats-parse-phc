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

sealed abstract case class BcryptHash(bytes: ByteVector) {
  final def toBcryptBase64: String =
    bytes.toBase64(BcryptBase64Alphabet)

  final def show: String =
    toBcryptBase64

  override final def toString: String =
    s"BcryptHash($toBcryptBase64)"

  final def toPhc: PhcHash =
    PhcHash.fromBytes(bytes)
}

object BcryptHash {
  def apply(hash: String): Either[PhcError, BcryptHash] =
    fromBcryptBase64(hash)

  def fromBcryptBase64(hash: String): Either[PhcError, BcryptHash] =
    if (hash.length == 31)
      ByteVector
        .fromBase64(hash, BcryptBase64Alphabet)
        .toRight(PhcError(s"Invalid BcryptHash: $hash"))
        .map(new BcryptHash(_) {})
    else Left(PhcError(s"Invalid BcryptHash: $hash"))

  def fromBytes(bytes: ByteVector): Either[PhcError, BcryptHash] =
    if (bytes.size == 23L) Right(new BcryptHash(bytes) {})
    else Left(PhcError(s"Invalid BcryptHash: $bytes"))

  val parser: Parser[BcryptHash] =
    Parser
      .charIn(Seq('.', '/') ++ ('A' to 'Z') ++ ('a' to 'z') ++ ('0' to '9'))
      .repExactlyAs[String](31)
      .mapFilter(apply(_).toOption)

  implicit val bcryptHashHash: Hash[BcryptHash] =
    Hash.fromUniversalHashCode

  implicit val bcryptHashShow: Show[BcryptHash] =
    Show.show(_.show)
}
