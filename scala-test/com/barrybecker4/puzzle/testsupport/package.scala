package com.barrybecker4.puzzle

package object testsupport {

  /**
    * This is useful to get unit tests running on windows or linux. Windows uses \r\n line endings.
    * @param s string to process
    * @param endOfLineChar (optional) what to put at the end of each line
    * @return the string without the leading margin char and with unix style line endings
    */
  def strip(s: String, endOfLineChar: String = "\n"): String =
    s.stripMargin.replaceAll(System.lineSeparator, endOfLineChar)
}
