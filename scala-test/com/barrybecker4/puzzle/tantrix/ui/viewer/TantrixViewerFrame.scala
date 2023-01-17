package com.barrybecker4.puzzle.tantrix.ui.viewer

import com.barrybecker4.puzzle.tantrix.model.TantrixBoard
import com.barrybecker4.puzzle.tantrix.ui.TantrixViewer
import com.barrybecker4.ui.application.ApplicationFrame

import java.awt.{BorderLayout, Dimension}
import javax.swing.JPanel


class TantrixViewerFrame(board: TantrixBoard)

  extends ApplicationFrame("Tantrix Tile Viewer") {
  this.setPreferredSize(new Dimension(800, 700))
  this.getContentPane.add(createContent(board))
  this.pack()
  this.setVisible(true)


  private def createContent(board: TantrixBoard) = {
    val mainPanel = new JPanel(new BorderLayout())
    val viewer = new TantrixViewer()
    viewer.refresh(board, 0)
    mainPanel.add(viewer, BorderLayout.CENTER)
    mainPanel
  }
}
