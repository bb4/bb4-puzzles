// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui.render

import org.lwjgl.PointerBuffer
import org.lwjgl.system.{APIUtil, MemoryStack, SharedLibrary}
import org.lwjgl.system.MemoryUtil.{NULL, memAddress, memGetAddress}
import org.lwjgl.system.Platform
import org.lwjgl.system.libffi.FFICIF
import org.lwjgl.system.libffi.LibFFI.*
import org.lwjgl.system.macosx.MacOSXLibrary

import java.nio.{ByteBuffer, DoubleBuffer}


/**
  * macOS helpers for `-XstartOnFirstThread` + AWT.
  * GLFW wait/poll after AWT starts can block forever; CFRunLoopRunInMode is safe for mapping the frame.
  */
object MacAppKit {
  @volatile private var cfRunLoopRunInMode: Long = NULL
  @volatile private var cfRunLoopDefaultMode: Long = NULL

  private def ensureCfRunLoop(): Unit = {
    if (cfRunLoopRunInMode != NULL) return
    val cf: SharedLibrary = MacOSXLibrary.getWithIdentifier("com.apple.CoreFoundation")
    cfRunLoopRunInMode = cf.getFunctionAddress("CFRunLoopRunInMode")
    cfRunLoopDefaultMode = memGetAddress(cf.getFunctionAddress("kCFRunLoopDefaultMode"))
    if (cfRunLoopRunInMode == NULL || cfRunLoopDefaultMode == NULL) {
      throw new IllegalStateException("Unable to resolve CFRunLoopRunInMode")
    }
  }

  /** Brief AppKit drain so setVisible can map the NSWindow under `-XstartOnFirstThread`. */
  def pumpEvents(): Unit = {
    if (Platform.get() != Platform.MACOSX) return
    ensureCfRunLoop()
    val stack = MemoryStack.stackPush()
    try {
      val cif: FFICIF = APIUtil.apiCreateCIF(
        FFI_DEFAULT_ABI, ffi_type_sint32, ffi_type_pointer, ffi_type_double, ffi_type_uint8)
      val args: PointerBuffer = stack.mallocPointer(3)
      args.put(0, stack.npointer(cfRunLoopDefaultMode))
      val sec: DoubleBuffer = stack.mallocDouble(1)
      sec.put(0, 0.01)
      args.put(1, memAddress(sec))
      val retAfter: ByteBuffer = stack.bytes(0.toByte)
      args.put(2, memAddress(retAfter))
      val out: ByteBuffer = stack.calloc(4)
      ffi_call(cif, cfRunLoopRunInMode, out, args)
    } finally {
      stack.close()
    }
  }
}
