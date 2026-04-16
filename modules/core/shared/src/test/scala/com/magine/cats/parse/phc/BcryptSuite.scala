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

final class BcryptSuite extends FunSuite {
  test("Bcrypt.roundtrip") {
    roundtrips(
      "$2a$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUW",
      "$2a$12$FoNsj8vKI9vE1/ijbIoe6evjenGlzFMTv3maAc2wtCc8Jhf.GmRkW",
      "$2b$10$TVMlLRCz/GPNNkhT.Dg.dunramVbhYMErUMFQ43avBdceOL9CZDsK",
      "$2b$10$69SrwAoAUNC5F.gtLEvrNON6VQ5EX89vNqLEqU655Oy9PeT/HRM/a"
    )
  }

  test("Bcrypt.syntax") {
    assertEquals(
      Bcrypt.parse(
        "$2a$12$FoNsj8vKI9vE1/ijbIoe6evjenGlzFMTv3maAc2wtCc8Jhf.GmRkW"
      ),
      Right(
        bcrypt"$$2a$$12$$FoNsj8vKI9vE1/ijbIoe6evjenGlzFMTv3maAc2wtCc8Jhf.GmRkW"
      )
    )
  }

  def roundtrips(bcrypt: String*): Unit =
    bcrypt.foreach { bcrypt =>
      val parsed = Bcrypt.parse(bcrypt)
      assertEquals(parsed.map(_.show), Right(bcrypt))

      val roundtrip = parsed.map(_.toPhc).flatMap(Bcrypt.fromPhc)
      assertEquals(roundtrip.map(_.show), Right(bcrypt))
    }
}
