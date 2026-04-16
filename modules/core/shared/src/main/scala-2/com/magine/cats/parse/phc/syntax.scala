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
  implicit final class PhcSyntax(val ctx: StringContext) extends AnyVal {
    def bcrypt(args: Any*): Bcrypt = macro BcryptLiteral.make

    def hash(args: Any*): PhcHash = macro PhcHashLiteral.make

    def id(args: Any*): PhcId = macro PhcIdLiteral.make

    def param(args: Any*): PhcParam = macro PhcParamLiteral.make

    def params(args: Any*): List[PhcParam] = macro PhcParamsLiteral.make

    def phc(args: Any*): Phc = macro PhcLiteral.make

    def salt(args: Any*): PhcSalt = macro PhcSaltLiteral.make

    def version(args: Any*): PhcVersion = macro PhcVersionLiteral.make
  }

  object BcryptLiteral extends Literally[Bcrypt] {
    def validate(c: Context)(s: String) = {
      import c.universe._
      Bcrypt.parse(s) match {
        case Right(_) => Right(c.Expr(q"_root_.com.magine.cats.parse.phc.Bcrypt.parse($s).toOption.get"))
        case Left(e) => Left(show"Invalid Bcrypt:\n$e")
      }
    }

    def make(c: Context)(args: c.Expr[Any]*): c.Expr[Bcrypt] =
      apply(c)(args: _*)
  }

  object PhcLiteral extends Literally[Phc] {
    def validate(c: Context)(s: String) = {
      import c.universe._
      Phc.parse(s) match {
        case Right(_) => Right(c.Expr(q"_root_.com.magine.cats.parse.phc.Phc.parse($s).toOption.get"))
        case Left(e) => Left(show"Invalid Phc:\n$e")
      }
    }

    def make(c: Context)(args: c.Expr[Any]*): c.Expr[Phc] =
      apply(c)(args: _*)
  }

  object PhcHashLiteral extends Literally[PhcHash] {
    def validate(c: Context)(s: String) = {
      import c.universe._
      PhcHash(s) match {
        case Right(_) => Right(c.Expr(q"_root_.com.magine.cats.parse.phc.PhcHash($s).toOption.get"))
        case Left(e) => Left(e.getMessage)
      }
    }

    def make(c: Context)(args: c.Expr[Any]*): c.Expr[PhcHash] =
      apply(c)(args: _*)
  }

  object PhcIdLiteral extends Literally[PhcId] {
    def validate(c: Context)(s: String) = {
      import c.universe._
      PhcId(s) match {
        case Right(_) => Right(c.Expr(q"_root_.com.magine.cats.parse.phc.PhcId($s).toOption.get"))
        case Left(e) => Left(e.getMessage)
      }
    }

    def make(c: Context)(args: c.Expr[Any]*): c.Expr[PhcId] =
      apply(c)(args: _*)
  }

  object PhcParamLiteral extends Literally[PhcParam] {
    def validate(c: Context)(s: String) = {
      import c.universe._
      PhcParam.parse(s) match {
        case Right(_) =>
          Right(c.Expr(q"_root_.com.magine.cats.parse.phc.PhcParam.parse($s).toOption.get"))
        case Left(e) => Left(show"Invalid PhcParam:\n$e")
      }
    }

    def make(c: Context)(args: c.Expr[Any]*): c.Expr[PhcParam] =
      apply(c)(args: _*)
  }

  object PhcParamsLiteral extends Literally[List[PhcParam]] {
    def validate(c: Context)(s: String) = {
      import c.universe._
      PhcParams.parseSyntax(s) match {
        case Right(_) =>
          Right(c.Expr(q"_root_.com.magine.cats.parse.phc.PhcParams.parseSyntax($s).toOption.get"))
        case Left(e) => Left(show"Invalid PhcParams:\n$e")
      }
    }

    def make(c: Context)(args: c.Expr[Any]*): c.Expr[List[PhcParam]] =
      apply(c)(args: _*)
  }

  object PhcSaltLiteral extends Literally[PhcSalt] {
    def validate(c: Context)(s: String) = {
      import c.universe._
      PhcSalt(s) match {
        case Right(_) => Right(c.Expr(q"_root_.com.magine.cats.parse.phc.PhcSalt($s).toOption.get"))
        case Left(e) => Left(e.getMessage)
      }
    }

    def make(c: Context)(args: c.Expr[Any]*): c.Expr[PhcSalt] =
      apply(c)(args: _*)
  }

  object PhcVersionLiteral extends Literally[PhcVersion] {
    def validate(c: Context)(s: String) = {
      import c.universe._
      PhcVersion(s) match {
        case Right(_) => Right(c.Expr(q"_root_.com.magine.cats.parse.phc.PhcVersion($s).toOption.get"))
        case Left(e) => Left(e.getMessage)
      }
    }

    def make(c: Context)(args: c.Expr[Any]*): c.Expr[PhcVersion] =
      apply(c)(args: _*)
  }
}
