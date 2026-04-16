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
import munit.FunSuite

final class PhcSuite extends FunSuite {
  test("Phc.parseShowRoundtrip") {
    roundtrips(
      "$argon2id$v=19$m=65536,t=2,p=1$gZiV/M1gPc22ElAH/Jh1Hw$CWOrkoo7oJBQ/iyh7uJ0LO2aLEfrHwTWllSAxT0zRno",
      "$argon2id$v=1$gZiV/M1gPc22ElAH/Jh1Hw$CWOrkoo7oJBQ/iyh7uJ0LO2aLEfrHwTWllSAxT0zRno",
      "$argon2id$gZiV/M1gPc22ElAH/Jh1Hw$CWOrkoo7oJBQ/iyh7uJ0LO2aLEfrHwTWllSAxT0zRno",
      "$argon2id$gZiV/M1gPc22ElAH/Jh1Hw",
      "$argon2id$m=65536,t=2,p=1$gZiV/M1gPc22ElAH/Jh1Hw",
      "$argon2id$m=65536,t=2,p=1",
      "$argon2id",
      "$argon2id$v=19$t=3,m=4096,p=1$kWrEYCehhSi/oACgEp+x0g$1ktYoey8htWFAogDJPqocb+BjWzMCNQmEcVHpgPvkDU",
      "$argon2i$v=19$m=20,t=5,p=2$YWJjZGVmZ2g$ofbPZ/PKZ+20npB3CfQo16DcoVfBAGeHgQ",
      "$argon2d$v=19$m=20,t=5,p=2$YWJjZGVmZ2g$+4w43jvuj098fzDQhf66chupETuLCTOIqQ",
      "$argon2id$v=19$m=32,t=3,p=4$YWJjZGVmZ2g$BHGzonyRk5fqnDzYc27Y4RaCJPQFjbGxvuXFLyQW",
      "$bcrypt$v=98$r=10$ZOz3nZnj9eKD7Npwm40d1w$XKFAAt31RXowEKMIHbyaECKcuzZlfj4",
    )
  }

  test("Phc.syntax") {
    assertEquals(
      Phc.parse(
        "$argon2id$v=19$m=65536,t=2,p=1$gZiV/M1gPc22ElAH/Jh1Hw$CWOrkoo7oJBQ/iyh7uJ0LO2aLEfrHwTWllSAxT0zRno"
      ),
      Right(
        phc"$$argon2id$$v=19$$m=65536,t=2,p=1$$gZiV/M1gPc22ElAH/Jh1Hw$$CWOrkoo7oJBQ/iyh7uJ0LO2aLEfrHwTWllSAxT0zRno"
      )
    )
  }

  def roundtrips(phc: String*): Unit =
    phc.foreach(phc =>
      assertEquals(
        Phc.parse(phc).map(_.show),
        Right(phc)
      )
    )
}
