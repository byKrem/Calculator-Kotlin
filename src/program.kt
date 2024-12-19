fun main(){
    math()
}

fun math(){
    val calc = Calculator()
    var userInput: String?
    do{
        println("Write any math expression or type \'q\' to quit")

        userInput = readlnOrNull()
        if(userInput != null && userInput != "q"){
            print("Here is the answer: ")
            println(calc.doMath(userInput))
        }
    }while(!userInput.equals("q"))
}