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

sealed abstract case class PhcSalt(value: String) {

  /**
    * Return a new `ByteVector` by first decoding the
    * salt using Base64 encoding without `=` padding.
    */
  final def fromBase64NoPad: Either[PhcError, ByteVector] =
    ByteVector
      .fromBase64(value, Base64NoPad)
      .toRight(PhcError(s"Failed Base64NoPad decoding: $value"))

  final def show: String =
    value
}

object PhcSalt {

  /**
    * Return a new [[PhcSalt]] from an encoded salt.
    */
  def apply(salt: String): Either[PhcError, PhcSalt] =
    if (isValidSalt(salt)) Right(new PhcSalt(salt) {})
    else Left(PhcError(s"Invalid PhcSalt: $salt"))

  /**
    * Return a new [[PhcSalt]] by first encoding the
    * salt using Base64 encoding without `=` padding.
    */
  def toBase64NoPad(salt: ByteVector): Either[PhcError, PhcSalt] =
    apply(salt.toBase64NoPad)

  private def isValidSalt(salt: String): Boolean =
    salt.nonEmpty && salt.forall(c =>
      ('a' <= c && c <= 'z') ||
        ('A' <= c && c <= 'Z') ||
        ('0' <= c && c <= '9') ||
        c == '/' || c == '+' || c == '.' || c == '-'
    )

  /**
    * Parses a [[PhcSalt]] with a leading `$` sign.
    */
  val parser: Parser[PhcSalt] =
    Parser.char('$') *> Parser
      .charIn(('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9') :+ '/' :+ '+' :+ '.' :+ '-')
      .repAs[String]
      .mapFilter(apply(_).toOption)

  implicit val phcSaltHash: Hash[PhcSalt] =
    Hash.fromUniversalHashCode

  implicit val phcSaltShow: Show[PhcSalt] =
    Show.show(_.show)
}
