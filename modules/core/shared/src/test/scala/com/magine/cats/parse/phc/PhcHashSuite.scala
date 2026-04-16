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

final class PhcHashSuite extends ScalaCheckSuite {
  property("PhcHash.encodeDecodeRoundtrip") {
    val gen = arbitrary[String].filter(_.nonEmpty)
    Prop.forAll(gen) { s =>
      val encoded = ByteVector.encodeUtf8(s).map(_.toBase64NoPad)
      val obtained = encoded.flatMap(PhcHash(_)).flatMap(_.bytes.decodeUtf8)
      assertEquals(obtained, Right(s))
    }
  }

  test("PhcHash.syntax") {
    assertEquals(
      PhcHash("CWOrkoo7oJBQ/iyh7uJ0LO2aLEfrHwTWllSAxT0zRno"),
      Right(hash"CWOrkoo7oJBQ/iyh7uJ0LO2aLEfrHwTWllSAxT0zRno")
    )
  }
}
