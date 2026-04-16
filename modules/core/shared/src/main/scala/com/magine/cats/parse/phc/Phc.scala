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

sealed abstract class Phc {
  def id: PhcId

  def withId(id: PhcId): Phc

  def version: Option[PhcVersion]

  def withVersion(version: PhcVersion): Phc

  def withVersionOption(version: Option[PhcVersion]): Phc

  def withoutVersion: Phc

  def param(name: String): Option[String]

  def params: List[PhcParam]

  def withParams(params: List[PhcParam]): Phc

  def withoutParams: Phc

  def salt: Option[PhcSalt]

  def hash: Option[PhcHash]

  def withHash(salt: PhcSalt, hash: PhcHash): Phc

  def withHashOption(salt: PhcSalt, hash: Option[PhcHash]): Phc

  def withoutHash: Phc

  def show: String
}

object Phc {
  def apply(
    id: PhcId,
    version: Option[PhcVersion],
    params: List[PhcParam],
    salt: PhcSalt,
    hash: PhcHash
  ): Phc =
    withHash(id, version, params, salt, hash)

  def withHash(
    id: PhcId,
    version: Option[PhcVersion],
    params: List[PhcParam],
    salt: PhcSalt,
    hash: PhcHash
  ): Phc =
    withHashOption(id, version, params, salt, Some(hash))

  def withHashOption(
    id: PhcId,
    version: Option[PhcVersion],
    params: List[PhcParam],
    salt: PhcSalt,
    hash: Option[PhcHash]
  ): Phc =
    PhcImpl(id, version, params, Some(salt), hash)

  def withoutHash(
    id: PhcId,
    version: Option[PhcVersion],
    params: List[PhcParam]
  ): Phc =
    PhcImpl(id, version, params, salt = None, hash = None)

  private final case class PhcImpl(
    override val id: PhcId,
    override val version: Option[PhcVersion],
    override val params: List[PhcParam],
    override val salt: Option[PhcSalt],
    override val hash: Option[PhcHash]
  ) extends Phc {
    override def withId(id: PhcId): Phc =
      copy(id = id)

    override def withVersion(version: PhcVersion): Phc =
      withVersionOption(Some(version))

    override def withVersionOption(version: Option[PhcVersion]): Phc =
      copy(version = version)

    override def withoutVersion: Phc =
      withVersionOption(None)

    override def param(name: String): Option[String] =
      params.find(_.name == name).map(_.value)

    override def withParams(params: List[PhcParam]): Phc =
      copy(params = params)

    override def withoutParams: Phc =
      withParams(List.empty)

    override def withHash(salt: PhcSalt, hash: PhcHash): Phc =
      withHashOption(salt, Some(hash))

    override def withHashOption(salt: PhcSalt, hash: Option[PhcHash]): Phc =
      copy(salt = Some(salt), hash = hash)

    override def withoutHash: Phc =
      copy(salt = None, hash = None)

    override def show: String = {
      val sb = new java.lang.StringBuilder().append('$').append(id.show)
      version.foreach(version => sb.append("$v=").append(version.show))

      val it = params.iterator
      if (it.hasNext) {
        sb.append('$').append(it.next().show)
        while (it.hasNext)
          sb.append(',').append(it.next().show): Unit
      }

      salt.foreach(salt => sb.append('$').append(salt.show))
      hash.foreach(hash => sb.append('$').append(hash.show))
      sb.toString
    }

    override def toString: String =
      productElementNames
        .zip(productIterator)
        .map { case (name, value) => s"$name = $value" }
        .mkString(s"Phc(", ", ", ")")
  }

  val parser: Parser[Phc] = {
    val id = PhcId.parser
    val version = PhcVersion.parser
    val params = PhcParams.parser
    val salt = PhcSalt.parser
    val hash = PhcHash.parser

    (id ~ version.? ~ params ~ (salt ~ hash.?).?).map {
      case (((id, version), params), Some((salt, hash))) =>
        withHashOption(id, version, params, salt, hash)
      case (((id, version), params), None) =>
        withoutHash(id, version, params)
    }
  }

  def parse(phc: String): Either[Parser.Error, Phc] =
    parser.parseAll(phc)

  implicit val phcHash: Hash[Phc] =
    Hash.fromUniversalHashCode

  implicit val phcShow: Show[Phc] =
    Show.show(_.show)
}
