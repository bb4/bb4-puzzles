/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */

package com.barrybecker4.puzzle.tantrix.ui

import java.awt._
import javax.swing._

import com.barrybecker4.puzzle.tantrix.model.{HexTiles, TantrixBoard}
import com.barrybecker4.ui.application.ApplicationApplet
import com.barrybecker4.ui.util.GUIUtil

/**
  * An app that tries to demonstrate the use of most of the UI components in this package.
  *
  * @author Barry Becker
  */
object TantrixBoardUI {

  @throws[Exception]
  def main(args: Array[String]) {
    val applet = new TantrixBoardUI
    GUIUtil.showApplet(applet)
  }
}

class TantrixBoardUI() extends ApplicationApplet {

  protected def createMainPanel: JPanel = {
    val mainPanel = new JPanel
    val gridPanel = new TantrixViewer
    val board: TantrixBoard = new TantrixBoard(HexTiles.TILES.createOrderedList(3))

    mainPanel.setLayout(new BorderLayout)
    mainPanel.add(gridPanel, BorderLayout.CENTER)

    gridPanel.refresh(board, 0)

    mainPanel
  }

  override protected def getResourceList: java.util.List[String] = {
    val resources = new java.util.ArrayList[String](super.getResourceList)
    resources.add("com.barrybecker4.puzzle.tantrix.ui.message")
    resources
  }

  protected def createUI() {
  }
}
