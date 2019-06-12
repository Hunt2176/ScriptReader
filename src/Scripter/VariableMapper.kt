package Scripter

class VariableMapper(vararg variables: Pair<String, String>)
{
    private val map = mutableMapOf<ScriptVariable, String>()

    init
    {
        variables.forEach { map[ScriptVariable(it.first)] = it.second }
    }

    fun addVariable(variable: ScriptVariable, value: String)
    {
        map[variable] = value
    }

    fun addVariable(variableValue: Pair<String, String>)
    {
        addVariable(variableValue.first, variableValue.second)
    }

    fun addVariable(variable: String, value: String)
    {
        map[ScriptVariable(variable)] = value
    }

    fun removeVariable(variable: ScriptVariable)
    {
        map.remove(variable)
    }

    fun removeVariable(variable: String)
    {
        map.remove(ScriptVariable(variable))
    }

    fun getMap(): Map<ScriptVariable, String> = map.toMap()
}