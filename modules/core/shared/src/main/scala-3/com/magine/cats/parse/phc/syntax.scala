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

import cats.syntax.all.*
import org.typelevel.literally.Literally

object syntax {
  extension (inline ctx: StringContext) {
    inline def bcrypt(inline args: Any*): Bcrypt =
      ${ BcryptLiteral('ctx, 'args) }

    inline def hash(inline args: Any*): PhcHash =
      ${ PhcHashLiteral('ctx, 'args) }

    inline def id(inline args: Any*): PhcId =
      ${ PhcIdLiteral('ctx, 'args) }

    inline def param(inline args: Any*): PhcParam =
      ${ PhcParamLiteral('ctx, 'args) }

    inline def params(inline args: Any*): List[PhcParam] =
      ${ PhcParamsLiteral('ctx, 'args) }

    inline def phc(inline args: Any*): Phc =
      ${ PhcLiteral('ctx, 'args) }

    inline def salt(inline args: Any*): PhcSalt =
      ${ PhcSaltLiteral('ctx, 'args) }

    inline def version(inline args: Any*): PhcVersion =
      ${ PhcVersionLiteral('ctx, 'args) }
  }

  object BcryptLiteral extends Literally[Bcrypt] {
    def validate(
      s: String
    )(
      using Quotes
    ) =
      Bcrypt.parse(s) match {
        case Right(_) => Right('{ _root_.com.magine.cats.parse.phc.Bcrypt.parse(${ Expr(s) }).toOption.get })
        case Left(e) => Left(show"Invalid Bcrypt:\n$e")
      }
  }

  object PhcLiteral extends Literally[Phc] {
    def validate(
      s: String
    )(
      using Quotes
    ) =
      Phc.parse(s) match {
        case Right(_) => Right('{ _root_.com.magine.cats.parse.phc.Phc.parse(${ Expr(s) }).toOption.get })
        case Left(e) => Left(show"Invalid Phc:\n$e")
      }
  }

  object PhcHashLiteral extends Literally[PhcHash] {
    def validate(
      s: String
    )(
      using Quotes
    ) =
      PhcHash(s) match {
        case Right(_) => Right('{ _root_.com.magine.cats.parse.phc.PhcHash(${ Expr(s) }).toOption.get })
        case Left(e) => Left(e.getMessage)
      }
  }

  object PhcIdLiteral extends Literally[PhcId] {
    def validate(
      s: String
    )(
      using Quotes
    ) =
      PhcId(s) match {
        case Right(_) => Right('{ _root_.com.magine.cats.parse.phc.PhcId(${ Expr(s) }).toOption.get })
        case Left(e) => Left(e.getMessage)
      }
  }

  object PhcParamLiteral extends Literally[PhcParam] {
    def validate(
      s: String
    )(
      using Quotes
    ) =
      PhcParam.parse(s) match {
        case Right(_) =>
          Right('{ _root_.com.magine.cats.parse.phc.PhcParam.parse(${ Expr(s) }).toOption.get })
        case Left(e) => Left(show"Invalid PhcParam:\n$e")
      }
  }

  object PhcParamsLiteral extends Literally[List[PhcParam]] {
    def validate(
      s: String
    )(
      using Quotes
    ) =
      PhcParams.parseSyntax(s) match {
        case Right(_) =>
          Right('{ _root_.com.magine.cats.parse.phc.PhcParams.parseSyntax(${ Expr(s) }).toOption.get })
        case Left(e) => Left(show"Invalid PhcParams:\n$e")
      }
  }

  object PhcSaltLiteral extends Literally[PhcSalt] {
    def validate(
      s: String
    )(
      using Quotes
    ) =
      PhcSalt(s) match {
        case Right(_) => Right('{ _root_.com.magine.cats.parse.phc.PhcSalt(${ Expr(s) }).toOption.get })
        case Left(e) => Left(e.getMessage)
      }
  }

  object PhcVersionLiteral extends Literally[PhcVersion] {
    def validate(
      s: String
    )(
      using Quotes
    ) =
      PhcVersion(s) match {
        case Right(_) => Right('{ _root_.com.magine.cats.parse.phc.PhcVersion(${ Expr(s) }).toOption.get })
        case Left(e) => Left(e.getMessage)
      }
  }
}
