package org.camunda.feel.example.spi

import org.camunda.feel.interpreter._
import org.camunda.feel.spi.FunctionProvider
import scala.math.BigDecimal.int2bigDecimal

class CustomScalaFunctionProvider extends FunctionProvider {
  
   val functions: Map[(String, Int), ValFunction] = Map(
        ("foo", 1) -> ValFunction(List("x"), { case List(ValNumber(x)) => ValNumber(x + 1) } ),
        ("isBlack", 0) -> ValFunction(List(), { case List(ValString(s)) => if(s == "black") ValBoolean(true) else ValBoolean(false) }, requireInputVariable = true)
      )
      
    def getFunction(functionName: String, argumentCount: Int): Option[ValFunction] = functions.get((functionName, argumentCount))
  
}