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

import scodec.bits.Bases.Base64Alphabet
import scodec.bits.Bases.IgnoreChar

object BcryptBase64Alphabet extends Base64Alphabet {
  private val chars: Array[Char] =
    (Seq('.', '/') ++ ('A' to 'Z') ++ ('a' to 'z') ++ ('0' to '9')).toArray

  override val pad: Char =
    0.toChar

  override def toChar(i: Int): Char =
    chars(i)

  override def toIndex(c: Char): Int =
    c match {
      case '.' => 0
      case '/' => 1
      case c if c >= 'A' && c <= 'Z' => c - 'A' + 2
      case c if c >= 'a' && c <= 'z' => c - 'a' + 26 + 2
      case c if c >= '0' && c <= '9' => c - '0' + 26 + 26 + 2
      case c if ignore(c) => IgnoreChar
      case _ => throw new IllegalArgumentException
    }

  override def ignore(c: Char): Boolean =
    c.isWhitespace
}
