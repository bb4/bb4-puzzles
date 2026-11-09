// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

import com.barrybecker4.common.app.AppContext
import org.scalatest.funsuite.AnyFunSuite

class BenchmarkAppContextSuite extends AnyFunSuite {

  test("ensureInitialized makes AppContext ready and algorithm labels resolvable") {
    BenchmarkAppContext.ensureInitialized()
    assert(AppContext.isInitialized)
    assert(AppContext.getLabel("GENETIC_SEARCH").nonEmpty)
    assert(AppContext.getLabel("FAST_SHORTEST").nonEmpty)
    assert(AppContext.getLabel("SHAPE_THEN_ASSIGN").nonEmpty)
    assert(AppContext.getLabel("FAST_BACKTRACKING").nonEmpty)
  }

  test("ensureInitialized can be called more than once") {
    BenchmarkAppContext.ensureInitialized()
    BenchmarkAppContext.ensureInitialized()
    assert(AppContext.isInitialized)
  }
}
