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

sealed abstract class Bcrypt {
  def version: BcryptVersion

  def withVersion(version: BcryptVersion): Bcrypt

  def cost: BcryptCost

  def withCost(cost: BcryptCost): Bcrypt

  def salt: BcryptSalt

  def hash: BcryptHash

  def withHash(salt: BcryptSalt, hash: BcryptHash): Bcrypt

  def show: String

  def toPhc: Phc
}

object Bcrypt {
  def apply(
    version: BcryptVersion,
    cost: BcryptCost,
    salt: BcryptSalt,
    hash: BcryptHash
  ): Bcrypt =
    BcryptImpl(version, cost, salt, hash)

  def fromPhc(phc: Phc): Either[PhcError, Bcrypt] =
    for {
      _ <- Either.cond(
        test = phc.id == Bcrypt.phcId,
        left = PhcError(s"Invalid Bcrypt PhcId: $phc"),
        right = ()
      )
      version <- phc.version
        .toRight(PhcError(s"Missing PhcVersion for BcryptVersion: $phc"))
        .flatMap(BcryptVersion.fromPhc)
      cost <- phc
        .param("r")
        .toRight(PhcError(s"Missing PhcParam [r] for BcryptCost: $phc"))
        .flatMap(BcryptCost(_))
      salt <- phc.salt
        .toRight(PhcError(s"Missing PhcSalt for BcryptSalt: $phc"))
        .flatMap(_.fromBase64NoPad)
        .flatMap(BcryptSalt.fromBytes)
      hash <- phc.hash
        .toRight(PhcError(s"Missing PhcHash for BcryptHash: $phc"))
        .flatMap(hash => BcryptHash.fromBytes(hash.bytes))
    } yield Bcrypt(version, cost, salt, hash)

  private final case class BcryptImpl(
    override val version: BcryptVersion,
    override val cost: BcryptCost,
    override val salt: BcryptSalt,
    override val hash: BcryptHash,
  ) extends Bcrypt {
    override def withVersion(version: BcryptVersion): Bcrypt =
      copy(version = version)

    override def withCost(cost: BcryptCost): Bcrypt =
      copy(cost = cost)

    override def withHash(salt: BcryptSalt, hash: BcryptHash): Bcrypt =
      copy(salt = salt, hash = hash)

    override def show: String =
      s"$$${version.show}$$${cost.show}$$${salt.show}${hash.show}"

    override def toString: String =
      productElementNames
        .zip(productIterator)
        .map { case (name, value) => s"$name = $value" }
        .mkString(s"Bcrypt(", ", ", ")")

    override def toPhc: Phc =
      Phc.withHash(
        id = Bcrypt.phcId,
        version = Some(version.toPhc),
        params = List(cost.toPhc),
        salt = salt.toPhc,
        hash = hash.toPhc
      )
  }

  val parser: Parser[Bcrypt] = {
    val version = BcryptVersion.parser
    val cost = BcryptCost.parser
    val salt = BcryptSalt.parser
    val hash = BcryptHash.parser

    (version ~ cost ~ salt ~ hash)
      .map { case (((version, cost), salt), hash) =>
        Bcrypt(version, cost, salt, hash)
      }
  }

  val phcId: PhcId =
    safe(PhcId("bcrypt"))

  def parse(bcrypt: String): Either[Parser.Error, Bcrypt] =
    parser.parseAll(bcrypt)

  implicit val bcryptHash: Hash[Bcrypt] =
    Hash.fromUniversalHashCode

  implicit val bcryptShow: Show[Bcrypt] =
    Show.show(_.show)
}
