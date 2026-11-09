// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.common.i18n.MessageContext
import com.barrybecker4.ui.util.Log

/**
  * Headless entry points construct puzzle [[com.barrybecker4.puzzle.common.AlgorithmEnum]] values, which call
  * [[AppContext.getLabel]] for each case. Without an initialized message context (and logger), that throws.
  */
object BenchmarkAppContext {

  /**
    * Bundles that hold algorithm enum labels for every puzzle the benchmark constructs.
    * Shared keys (GENETIC_SEARCH, …) live in common.ui; puzzle-specific ones (SHAPE_THEN_ASSIGN,
    * FAST_BACKTRACKING, …) live in each puzzle's ui.message.
    */
  private val MESSAGE_BUNDLES: List[String] = List(
    MessageContext.COMMON_BUNDLE,
    "com.barrybecker4.puzzle.common.ui.message",
    "com.barrybecker4.puzzle.tantrix.ui.message",
    "com.barrybecker4.puzzle.redpuzzle.ui.message",
    "com.barrybecker4.puzzle.slidingpuzzle.ui.message"
  )

  /** Idempotent: safe to call from both the parent JVM and each worker JVM. */
  def ensureInitialized(): Unit = {
    if (!AppContext.isInitialized) {
      AppContext.initialize("ENGLISH", MESSAGE_BUNDLES, new Log)
    }
  }
}
