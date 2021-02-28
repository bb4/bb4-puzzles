package com.barrybecker4.puzzle.rubixcube.ui.jmonkey

import com.jme3.app.LegacyApplication
import com.jme3.app.SimpleApplication
import com.jme3.system.AppSettings
import com.jme3.system.JmeCanvasContext
import java.awt.BorderLayout
import java.awt.Canvas
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.concurrent.Callable
import javax.swing._


object TestCanvas {
  private var context: JmeCanvasContext = _
  private var canvas: Canvas = _
  private var app: LegacyApplication = _
  private var frame: JFrame = _
  private var canvasPanel1: JPanel = _
  private var canvasPanel2: JPanel = _
  private var currentPanel: JPanel = _
  private var tabbedPane: JTabbedPane = _
  private val appClass = "com.barrybecker4.puzzle.rubixcube.ui.jmonkey.TestRenderToTexture"

  private def createTabs(): Unit = {
    tabbedPane = new JTabbedPane
    canvasPanel1 = new JPanel
    canvasPanel1.setLayout(new BorderLayout)
    tabbedPane.addTab("jME3 Canvas 1", canvasPanel1)
    canvasPanel2 = new JPanel
    canvasPanel2.setLayout(new BorderLayout)
    tabbedPane.addTab("jME3 Canvas 2", canvasPanel2)
    frame.getContentPane.add(tabbedPane)
    currentPanel = canvasPanel1
  }

  private def createMenu(): Unit = {
    val menuBar = new JMenuBar
    frame.setJMenuBar(menuBar)
    val menuTortureMethods = new JMenu("Canvas Torture Methods")
    menuBar.add(menuTortureMethods)
    val itemRemoveCanvas = new JMenuItem("Remove Canvas")
    menuTortureMethods.add(itemRemoveCanvas)
    itemRemoveCanvas.addActionListener(new ActionListener() {
      override def actionPerformed(e: ActionEvent): Unit = {
        if (itemRemoveCanvas.getText == "Remove Canvas") {
          currentPanel.remove(canvas)
          itemRemoveCanvas.setText("Add Canvas")
        }
        else if (itemRemoveCanvas.getText == "Add Canvas") {
          currentPanel.add(canvas, BorderLayout.CENTER)
          itemRemoveCanvas.setText("Remove Canvas")
        }
      }
    })
    val itemHideCanvas = new JMenuItem("Hide Canvas")
    menuTortureMethods.add(itemHideCanvas)
    itemHideCanvas.addActionListener(new ActionListener() {
      override def actionPerformed(e: ActionEvent): Unit = {
        if (itemHideCanvas.getText == "Hide Canvas") {
          canvas.setVisible(false)
          itemHideCanvas.setText("Show Canvas")
        }
        else if (itemHideCanvas.getText == "Show Canvas") {
          canvas.setVisible(true)
          itemHideCanvas.setText("Hide Canvas")
        }
      }
    })
    val itemSwitchTab = new JMenuItem("Switch to tab #2")
    menuTortureMethods.add(itemSwitchTab)
    itemSwitchTab.addActionListener(new ActionListener() {
      override def actionPerformed(e: ActionEvent): Unit = {
        if (itemSwitchTab.getText == "Switch to tab #2") {
          canvasPanel1.remove(canvas)
          canvasPanel2.add(canvas, BorderLayout.CENTER)
          currentPanel = canvasPanel2
          itemSwitchTab.setText("Switch to tab #1")
        }
        else if (itemSwitchTab.getText == "Switch to tab #1") {
          canvasPanel2.remove(canvas)
          canvasPanel1.add(canvas, BorderLayout.CENTER)
          currentPanel = canvasPanel1
          itemSwitchTab.setText("Switch to tab #2")
        }
      }
    })
    val itemSwitchLaf = new JMenuItem("Switch Look and Feel")
    menuTortureMethods.add(itemSwitchLaf)
    itemSwitchLaf.addActionListener(new ActionListener() {
      override def actionPerformed(e: ActionEvent): Unit = {
        try UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
        catch {
          case t: Throwable =>
            t.printStackTrace()
        }
        SwingUtilities.updateComponentTreeUI(frame)
        frame.pack()
      }
    })
    val itemSmallSize = new JMenuItem("Set size to (0, 0)")
    menuTortureMethods.add(itemSmallSize)
    itemSmallSize.addActionListener(new ActionListener() {
      override def actionPerformed(e: ActionEvent): Unit = {
        val preferred = frame.getPreferredSize
        frame.setPreferredSize(new Dimension(0, 0))
        frame.pack()
        frame.setPreferredSize(preferred)
      }
    })
    val itemKillCanvas = new JMenuItem("Stop/Start Canvas")
    menuTortureMethods.add(itemKillCanvas)
    itemKillCanvas.addActionListener(new ActionListener() {
      override def actionPerformed(e: ActionEvent): Unit = {
        currentPanel.remove(canvas)
        app.stop(true)
        createCanvas(appClass)
        currentPanel.add(canvas, BorderLayout.CENTER)
        frame.pack()
        startApp()
      }
    })
    val itemExit = new JMenuItem("Exit")
    menuTortureMethods.add(itemExit)
    itemExit.addActionListener(new ActionListener() {
      override def actionPerformed(ae: ActionEvent): Unit = {
        frame.dispose()
        app.stop()
      }
    })
  }

  private def createFrame(): Unit = {
    frame = new JFrame("Test")
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    frame.addWindowListener(new WindowAdapter() {
      override def windowClosed(e: WindowEvent): Unit = {
        app.stop()
      }
    })
    createTabs()
    createMenu()
  }

  def createCanvas(appClass: String): Canvas = {
    val settings = new AppSettings(true)
    settings.setWidth(1024)
    settings.setHeight(680)

    val clazz = Class.forName(appClass)
    app = clazz.getDeclaredConstructor().newInstance().asInstanceOf[LegacyApplication]

    app.setPauseOnLostFocus(false)
    app.setSettings(settings)
    app.createCanvas()
    app.startCanvas()
    context = app.getContext.asInstanceOf[JmeCanvasContext]
    canvas = context.getCanvas
    canvas.setSize(settings.getWidth, settings.getHeight)
    canvas
  }

  def startApp(): Unit = {
    app.startCanvas()
    app.enqueue(new Callable[Void]() {
      override def call: Void = {
        app match {
          case simpleApp: SimpleApplication =>
            simpleApp.getFlyByCamera.setDragToRotate(true)
          case _ =>
        }
        null
      }
    })
  }

  def main(args: Array[String]): Unit = {
//    val formatter = new JmeFormatter
//    val consoleHandler = new ConsoleHandler
//    consoleHandler.setFormatter(formatter)
//    Logger.getLogger("").removeHandler(Logger.getLogger("").getHandlers()(0))
//    Logger.getLogger("").addHandler(consoleHandler)


    SwingUtilities.invokeLater(new Runnable() {
      override def run(): Unit = {
        JPopupMenu.setDefaultLightWeightPopupEnabled(false)
        createFrame()

        canvas = createCanvas(appClass)
        currentPanel.add(canvas, BorderLayout.CENTER)

        frame.pack()
        startApp()
        frame.setLocationRelativeTo(null)
        frame.setVisible(true)
      }
    })
  }
}