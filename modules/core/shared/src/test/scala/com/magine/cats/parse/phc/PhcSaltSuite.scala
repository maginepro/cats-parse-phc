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

import com.magine.cats.parse.phc.syntax.*
import munit.ScalaCheckSuite
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop
import scodec.bits.ByteVector

final class PhcSaltSuite extends ScalaCheckSuite {
  property("PhcSalt.encodeDecodeRoundtrip") {
    val gen = arbitrary[String].filter(_.nonEmpty)
    Prop.forAll(gen)(salt =>
      assertEquals(
        ByteVector
          .encodeUtf8(salt)
          .flatMap(PhcSalt.toBase64NoPad)
          .flatMap(_.fromBase64NoPad)
          .flatMap(_.decodeUtf8),
        Right(salt)
      )
    )
  }

  test("PhcSalt.syntax") {
    assertEquals(
      PhcSalt("gZiV/M1gPc22ElAH/Jh1Hw"),
      Right(salt"gZiV/M1gPc22ElAH/Jh1Hw")
    )
  }
}
