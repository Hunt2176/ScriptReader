package Scripter

data class ScriptVariable(private var varName: String)
{
    val rawVar: String = varName

    init
    {
        this.varName = String(varName.map { it.toUpperCase() }.toCharArray())
        this.varName = varName.split(" ").joinToString("")
        this.varName = varName.split("%").joinToString("")
        this.varName = varName.split("$").joinToString("")
    }

    override fun toString(): String = "%\$$varName\$%"
}