// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

import com.barrybecker4.puzzle.benchmark.Json.*
import org.scalatest.funsuite.AnyFunSuite

class JsonSuite extends AnyFunSuite {

  test("parses every kind of value") {
    val json = Json.parse(""" {"a": [1, 2.5, "x", true, false, null], "b": {}, "c": []} """)
    val expected = JObj(List(
      "a" -> JArr(List(JNum(1), JNum(2.5), JStr("x"), JBool(true), JBool(false), JNull)),
      "b" -> JObj(Nil),
      "c" -> JArr(Nil)
    ))
    assert(json == expected)
  }

  test("parses string escapes") {
    assert(Json.parse(""" "a\"b\\c\ndA" """) == JStr("a\"b\\c\ndA"))
  }

  test("writes pretty printed output ending with a newline") {
    assert(Json.write(JObj(List("a" -> JNum(1)))) == "{\n  \"a\": 1\n}\n")
  }

  test("whole numbers are written without a decimal point") {
    assert(Json.write(JNum(12345)).trim == "12345")
    assert(Json.write(JNum(2.5)).trim == "2.5")
  }

  test("what is written can be parsed back") {
    val original = JObj(List(
      "text" -> JStr("quote \" backslash \\ newline \n tab \t"),
      "numbers" -> JArr(List(JNum(0), JNum(-3), JNum(1.25))),
      "nested" -> JObj(List("ok" -> JBool(true), "nothing" -> JNull)),
      "empty" -> JArr(Nil)
    ))
    assert(Json.parse(Json.write(original)) == original)
  }

  test("get finds fields by name") {
    val obj = JObj(List("a" -> JNum(1), "b" -> JStr("two")))
    assert(obj.get("b") == Some(JStr("two")))
    assert(obj.get("missing").isEmpty)
  }

  test("invalid JSON is rejected") {
    List("", "{", "[1,", "{\"a\" 1}", "tru", "\"unterminated", "[1] extra", "{\"a\": }").foreach { bad =>
      intercept[IllegalArgumentException] {
        Json.parse(bad)
      }
    }
  }
}
