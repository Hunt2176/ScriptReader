package Scripter

class Script(private var lines: Collection<String>): Iterable<String>
{
    val variables = mutableMapOf<ScriptVariable, String>()

    val size: Int
        get() = lines.size

    val rawLines: Collection<String>
        get() = lines

    constructor(): this(arrayListOf())

    constructor(lines: Collection<String>, variables: Map<ScriptVariable, String>) : this(lines)
    {
        this.variables.putAll(variables)
    }

    constructor(lines: Collection<String>, variables: VariableMapper) : this(lines, variables.getMap())

    override fun iterator(): Iterator<String> = lines.mapIndexed { index, _ -> getLine(index) }.iterator()

    fun addLine(line: String)
    {
        this.lines = ArrayList(this.lines).let { it.add(line); it }
    }

    fun removeLine(index: Int)
    {
        this.lines = ArrayList(this.lines).apply { this.removeAt(index) }
    }

    fun removeAll(toRemove: String)
    {
        this.lines = ArrayList(this.lines).apply { removeAll { it == toRemove } }
    }

    fun forEach(onEach: (String) -> Unit)
    {
        lines.forEachIndexed { index, _ ->
            val line = getLine(index)
            onEach(line)
        }
    }

    fun getVariableList(): Collection<String>
    {
        val toReturn = arrayListOf<String>()
        val pattern = "%\\$.+\\$%"
        val regex = Regex(pattern)

        for (i in lines)
        {
            i.split(" ").forEach { regex.findAll(it).forEach { if (!toReturn.contains(it.value)) toReturn.add(it.value) } }
        }
        for (i in variables.keys)
        {
            if (!toReturn.contains(i.toString()))
            {
                toReturn.add(i.toString())
            }
        }
        return toReturn
    }

    fun getLine(index: Int, rawLine: Boolean = false): String
    {
        if (rawLine) return lines.elementAt(index)
        return if (containsVariable(lines.elementAt(index)))
        {
            replaceVariable(lines.elementAt(index))
        }
        else
        {
            lines.elementAt(index)
        }
    }

    fun containsVariables(): Boolean
    {
        for (i in lines)
        {
            if (containsVariable(i)) return true
        }
        return false
    }

    fun addVariable(variable: ScriptVariable)
    {
        this.variables[variable] = ""
    }

    fun containsVariable(line: String): Boolean
    {
        return line.contains(Regex("%\\$.+\\$%"))
    }

    private fun containsVariable(line: String, variable: String): Boolean
    {
        return line.contains(Regex("%\\$$variable\\$%"))
    }

    private fun containsVariable(line: String, variable: ScriptVariable): Boolean
    {
        return line.contains(Regex(variable.toString()))
    }

    private fun replaceVariable(line: String): String
    {
        var toReturn = line
        if (!containsVariable(line)) return line
        for (i in variables)
        {
            val regex: String = i.key.toString()
            if (line.contains(regex))
            {
                toReturn = toReturn.replace(regex, i.value)
            }
        }
        return toReturn
    }
}