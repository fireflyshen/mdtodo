package com.github.fireflyshen.mdtodo.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.util.TextRange

class ToggleTodoAction : AnAction() {


    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val project = e.project ?: return
        val doc = editor.document

        val caret = editor.caretModel.primaryCaret
        val lineNumber = doc.getLineNumber(caret.offset)
        val lineStart = doc.getLineStartOffset(lineNumber)
        val lineEnd = doc.getLineEndOffset(lineNumber)
        val lineText = doc.getText(TextRange(lineStart, lineEnd))

        val newText = when {
            lineText.contains("- [ ]") -> lineText.replace("- [ ]", "- [x]") // 加上空格
            lineText.contains("- [x]") -> lineText.replace("- [x]", "- [ ]")
            lineText.contains("- [X]") -> lineText.replace("- [X]", "- [ ]")
            else -> return
        }

        WriteCommandAction.runWriteCommandAction(project) {
            doc.replaceString(lineStart, lineEnd, newText)
        }
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabledAndVisible = file?.extension?.lowercase() == "md"
    }
}