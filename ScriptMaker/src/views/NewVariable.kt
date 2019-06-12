package views

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextField

class NewVariable
{
    @FXML private lateinit var var_textfield: TextField
    @FXML private lateinit var complete_button: Button

    private var onComplete: ((NewVariable) -> Unit)? = null

    fun initialize()
    {
        complete_button.setOnMouseClicked { onComplete?.invoke(this) }
    }

    fun setOnComplete(onComplete: (NewVariable) -> Unit)
    {
        this.onComplete = onComplete
    }

    fun getCurrentText(): String = var_textfield.text

}