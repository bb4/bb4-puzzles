// Copyright by Barry G. Becker, 2021-2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui.selectors

import com.barrybecker4.puzzle.rubixcube.model.Orientation

import javax.swing.JComboBox


final class OrientationSelector()
  extends JComboBox[String](Orientation.PRIMARY_ORIENTATIONS.map(_.toString).toArray) {

  setSelectedIndex(0)
  setToolTipText("Slice orientation")

  def getSelectedOrientation: Orientation =
    Orientation.PRIMARY_ORIENTATIONS(this.getSelectedIndex)
}
