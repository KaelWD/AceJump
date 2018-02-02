package com.johnlindquist.acejump.control

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAwareAction
import com.johnlindquist.acejump.label.Pattern.LINE_MARK
import com.johnlindquist.acejump.label.Pattern.ALL_WORDS
import com.johnlindquist.acejump.search.Finder
import com.johnlindquist.acejump.search.getNameOfFileInEditor
import com.johnlindquist.acejump.view.Model.editor
import java.awt.event.KeyEvent

/**
 * Entry point for all actions. The IntelliJ Platform calls AceJump here.
 */

open class AceAction : DumbAwareAction() {
  val logger = Logger.getInstance(AceAction::class.java)
  override fun update(action: AnActionEvent) {
    action.presentation.isEnabled = action.getData(EDITOR) != null
  }

  override fun actionPerformed(e: AnActionEvent) {
    Finder.fullFileMode()
    editor = e.getData(EDITOR) ?: editor
    logger.info("Invoked on ${editor.getNameOfFileInEditor()}")
    Handler.activate()
  }
}

class AceTargetAction : AceAction() {
  override fun actionPerformed(e: AnActionEvent) =
    super.actionPerformed(e).also { Handler.toggleTargetMode(true) }
}

class AceLineAction : AceAction() {
  override fun actionPerformed(e: AnActionEvent) =
    super.actionPerformed(e).also { Finder.search(LINE_MARK) }
}

object AceKeyAction : AceAction() {
  override fun actionPerformed(e: AnActionEvent) {
    val inputEvent = e.inputEvent as? KeyEvent ?: return
    logger.info("Registered key: ${KeyEvent.getKeyText(inputEvent.keyCode)}")
    Handler.processCommand(inputEvent.keyCode)
  }
}

class AceWordAction : AceAction() {
  override fun actionPerformed(e: AnActionEvent) =
    super.actionPerformed(e).also { Finder.search(ALL_WORDS) }
}

object AceWordForwardAction : AceAction() {
  override fun actionPerformed(e: AnActionEvent) =
    super.actionPerformed(e).also { 
      Finder.forwardMode()
      Finder.search(ALL_WORDS) 
   }
}

object AceWordBackwardsAction : AceAction() {
  override fun actionPerformed(e: AnActionEvent) =
    super.actionPerformed(e).also { 
       Finder.backwardsMode()
       Finder.search(ALL_WORDS) 
    }
}
