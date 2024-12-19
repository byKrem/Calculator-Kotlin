import kotlin.math.pow

internal class Calculator{

    fun doMath(expression: String): Float?{
        val transformedExpression = transformMathExpression(expression)

        var index = 0
        while(transformedExpression.size != 1 && index < transformedExpression.size){
            if(transformedExpression[index] in "^*/+-"){
                val temp = operationResult(transformedExpression[index-2].toFloat(),
                                transformedExpression[index-1].toFloat(),
                                transformedExpression[index])

                transformedExpression.removeAt(index)
                transformedExpression.removeAt(index-1)
                transformedExpression.removeAt(index-2)
                transformedExpression.add(index-2, temp.toString())

                index -= 2
            }

            index++
        }

        return transformedExpression.last.toFloatOrNull()
    }

    private fun operationResult(num1: Float, num2:Float, op: String): Float{
        return when(op){
            "^" -> num1.pow(num2)
            "*" -> num1 * num2
            "/" -> num1 / num2
            "+" -> num1 + num2
            "-" -> num1 - num2
            else -> throw IllegalArgumentException()
        }
    }

    private fun tokenizeExpression(expression: String): ArrayList<String>{
        val result: ArrayList<String> = ArrayList()
        val buff: StringBuilder = StringBuilder()

        for(i in expression){
            if(i == ' '){
                continue
            }

            if(i.digitToIntOrNull() != null || i == '.'){
                buff.append(i)
            }
            else{
                if(buff.isNotEmpty()){
                    result.add(buff.toString())
                    buff.clear()
                }
                result.add(i.toString())
            }
        }

        if(buff.isNotEmpty()){
            result.add(buff.toString())
            buff.clear()
        }

        return result
    }

    private fun transformMathExpression(expression: String): ArrayList<String>{
        var result: ArrayList<String>

        result = tokenizeExpression(expression)
        result = prioritizeExpression(result)

        return result
    }

    private fun prioritizeExpression(expression: ArrayList<String>): ArrayList<String>{
        val result: ArrayList<String> = ArrayList()
        val buff: ArrayList<String> = ArrayList()

        for(item in expression){
            if(item.toIntOrNull() != null){
                result.add(item)
            }
            else if(item in "("){
                buff.add(item)
            }
            else if(item in ")"){
                val index = buff.indexOfLast { op -> op == "(" }
                buff.removeAt(index)

                while(buff.size >= index+1){
                    result.add(buff[index])
                    buff.removeAt(index)
                }
            }
            else if(item in "-+*/"){
                while(buff.size != 0 && operandPriority(buff.last) >= operandPriority(item)){
                    result.add(buff.last)
                    buff.removeLast()
                }
                buff.add(item)
            }
            else if(item in "^"){
                while(buff.size != 0 && operandPriority(buff.last) > operandPriority(item)){
                    result.add(buff.last)
                    buff.removeLast()
                }
                buff.add(item)
            }
            else{
                buff.add(item)
            }
        }

        // Operators priority
        // ^ - High
        // */ - Middle
        // -+ - Lowest
        for(i in 0..<buff.size){
            for(j in 0..<buff.size){
                if(operandPriority(buff[i]) > operandPriority(buff[j])){
                    val temp = buff[i]
                    buff[i] = buff[j]
                    buff[j] = temp
                }
            }
        }

        for(item in buff){
            result.add(item)
        }

        return result
    }

    private fun operandPriority(operand: String): Int{
        return when(operand){
            in "^" -> 3
            in "*/" -> 2
            in "+-" -> 1
            else -> -1
        }
    }
}