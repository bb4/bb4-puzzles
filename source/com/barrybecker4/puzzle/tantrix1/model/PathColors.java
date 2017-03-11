// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.model;

import java.util.ArrayList;

/**
 * The list of 6 edge colors starting from the right side and going counter clockwise
 *
 * @author Barry Becker
 */
class PathColors extends ArrayList<PathColor> {

      PathColors(PathColor c1, PathColor c2, PathColor c3, PathColor c4, PathColor c5, PathColor c6)  {
          super(6);
          add(c1);
          add(c2);
          add(c3);
          add(c4);
          add(c5);
          add(c6);
      }
}
