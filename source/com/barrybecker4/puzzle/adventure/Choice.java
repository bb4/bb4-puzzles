// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.adventure;

import com.barrybecker4.common.xml.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * A choice that you can make in a scene.
 * Immutable.
 *
 * @author Barry Becker
 */
public class Choice {

    private String description_;
    private String destinationScene_;

    public Choice(Node choiceNode) {
        this(DomUtil.getAttribute(choiceNode, "description"),
             DomUtil.getAttribute(choiceNode, "resultScene"));
    }

    public Choice(String desc, String dest) {
        description_ = desc;
        destinationScene_ = dest;
    }

    /**
     * @return the test shown in the choice list.
     */
    public String getDescription() {
        return description_;
    }

    /**
     * @return the name of the scene to go to if they select this choice.
     */
    public String getDestination() {
        return destinationScene_;
    }

    public void setDestination(String destName) {
        destinationScene_ = destName;
    }

    /**
     * Factory method to create a choice.
     * @return the choice instance.
     */
    public Element createElement(Document document) {
        Element choiceElem = document.createElement("choice");
        choiceElem.setAttribute("description", getDescription());
        choiceElem.setAttribute("resultScene", getDestination());
        return choiceElem;
    }
}
