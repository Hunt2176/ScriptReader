package views

import Scripter.Script
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextArea
import javafx.scene.input.MouseButton

class NewLine
{
    @FXML lateinit var line_text: TextArea
    @FXML lateinit var complete_button: Button
    @FXML lateinit var var_list: ListView<String>

    private var onComplete: ((NewLine) -> Unit)? = null
    private var script: Script = Script()

    fun initialize()
    {
        complete_button.setOnMouseClicked {
            onComplete?.invoke(this)
        }

        var_list.setOnMouseClicked {
            if (it.button == MouseButton.PRIMARY && it.clickCount == 2)
            {
                if (var_list.selectionModel.selectedItem != null)
                {
                    line_text.text += var_list.selectionModel.selectedItem
                    line_text.requestFocus()
                    line_text.positionCaret(line_text.text.length)
                }

            }
        }
        updateFromScript()
    }

    fun setScript(script: Script)
    {
        this.script = script
        updateFromScript()
    }

    fun setOnComplete(onComplete: (NewLine) -> Unit)
    {
        this.onComplete = onComplete
    }

    fun setCurrentText(newText: String)
    {
        this.line_text.text = newText
    }

    fun getCurrentText(): String = line_text.text

    private fun updateFromScript()
    {
        var_list.items.clear()
        var_list.items.addAll(script.getVariableList())
    }
}