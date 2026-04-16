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
import scodec.bits.Bases.Alphabets.Base64NoPad
import scodec.bits.ByteVector

sealed abstract case class PhcHash(bytes: ByteVector) {
  final def toBase64NoPad: String =
    bytes.toBase64NoPad

  final def show: String =
    toBase64NoPad

  override final def toString: String =
    s"PhcHash($toBase64NoPad)"
}

object PhcHash {
  def apply(hash: String): Either[PhcError, PhcHash] =
    fromBase64NoPad(hash)

  def fromBase64NoPad(hash: String): Either[PhcError, PhcHash] =
    ByteVector
      .fromBase64(hash, Base64NoPad)
      .toRight(PhcError(s"Invalid PhcHash: $hash"))
      .map(fromBytes)

  def fromBytes(bytes: ByteVector): PhcHash =
    new PhcHash(bytes) {}

  /**
    * Parses a [[PhcHash]] with a leading `$` sign.
    */
  val parser: Parser[PhcHash] =
    Parser.char('$') *> Parser
      .charIn(('A' to 'Z') ++ ('a' to 'z') ++ ('0' to '9') :+ '+' :+ '/')
      .repAs[String]
      .mapFilter(apply(_).toOption)

  implicit val phcHashHash: Hash[PhcHash] =
    Hash.fromUniversalHashCode

  implicit val phcHashShow: Show[PhcHash] =
    Show.show(_.show)
}
