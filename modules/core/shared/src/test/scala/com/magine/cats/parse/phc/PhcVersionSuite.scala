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
import org.scalacheck.Gen
import org.scalacheck.Prop

final class PhcVersionSuite extends ScalaCheckSuite {
  test("PhcVersion.syntax") {
    assertEquals(
      PhcVersion("1"),
      Right(version"1")
    )
  }

  test("PhcVersion.fromInt.neg") {
    Prop.forAll(Gen.negNum[Int])(version => assert(PhcVersion.fromInt(version).isLeft))
  }

  test("PhcVersion.fromInt.nonNeg") {
    Prop.forAll(Gen.chooseNum(0, Int.MaxValue)) { version =>
      assertEquals(
        PhcVersion.fromInt(version).map(_.value),
        Right(version.toString)
      )
    }
  }

  test("PhcVersion.fromLong.neg") {
    Prop.forAll(Gen.negNum[Long])(version => assert(PhcVersion.fromLong(version).isLeft))
  }

  test("PhcVersion.fromLong.nonNeg") {
    Prop.forAll(Gen.chooseNum(0L, Long.MaxValue)) { version =>
      assertEquals(
        PhcVersion.fromLong(version).map(_.value),
        Right(version.toString)
      )
    }
  }
}
