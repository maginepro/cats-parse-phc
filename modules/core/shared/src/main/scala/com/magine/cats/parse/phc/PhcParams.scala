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

import cats.parse.Parser
import cats.parse.Parser0

object PhcParams {

  /**
    * Parses a `List` of [[PhcParam]]s with a leading `$` sign.
    *
    * Params are separated by `,` (comma). If the input cannot
    * be parsed as params, the parser instead returns an empty
    * `List` rather than a parse failure.
    */
  val parser: Parser0[List[PhcParam]] = {
    val params = PhcParam.parser.repSep(Parser.char(',')).map(_.toList)
    (Parser.char('$') *> params).backtrack.orElse(Parser.pure(List.empty))
  }

  private val parserSyntax: Parser0[List[PhcParam]] =
    PhcParam.parser.repSep0(Parser.char(','))

  /**
    * Parses a `List` of [[PhcParam]]s without leading `$` sign.
    *
    * Primarily to support `params"a=b,c=d"` String-interpolation.
    */
  def parseSyntax(params: String): Either[Parser.Error, List[PhcParam]] =
    parserSyntax.parseAll(params)
}
