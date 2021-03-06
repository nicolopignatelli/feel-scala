package org.camunda.feel

import org.camunda.feel.parser.FeelParser
import org.camunda.feel.parser.FeelParser._
import org.camunda.feel.interpreter.FeelInterpreter
import org.camunda.feel.interpreter.Context
import org.camunda.feel.interpreter.DefaultValueMapper
import org.camunda.feel.interpreter._
import org.camunda.feel.parser.Exp
import org.camunda.feel.script.CompiledFeelScript
import org.camunda.feel.spi._
import org.camunda.feel.spi.DefaultFunctionProviders.EmptyFunctionProvider
import org.camunda.feel.spi.VariableContext.StaticVariableContext

/**
 * @author Philipp Ossler
 */
class FeelEngine(functionProvider: FunctionProvider = EmptyFunctionProvider, valueMapper: ValueMapper = new DefaultValueMapper) {

  val interpreter = new FeelInterpreter

  def evalExpression(expression: String, context: Map[String, Any] = Map()): EvalResult = {
    eval(FeelParser.parseExpression, expression, Context(StaticVariableContext(context), functionProvider, valueMapper))
  }

  def evalExpression(expression: String, variableContext: VariableContext): EvalResult = {
    eval(FeelParser.parseExpression, expression, Context(variableContext, functionProvider, valueMapper))
  }

  def evalUnaryTests(expression: String, context: Map[String, Any] = Map()): EvalResult = {
    eval(FeelParser.parseUnaryTests, expression, Context(StaticVariableContext(context), functionProvider, valueMapper))
  }

  def evalUnaryTests(expression: String, variableContext: VariableContext): EvalResult = {
    eval(FeelParser.parseUnaryTests, expression, Context(variableContext, functionProvider, valueMapper))
  }

  def eval(exp: ParsedExpression, context: Map[String, Any] = Map()): EvalResult = evalParsedExpression(exp, Context(StaticVariableContext(context), functionProvider, valueMapper))

  private def eval(parser: String => ParseResult[Exp], expression: String, context: Context) = parser(expression) match {
    case Success(exp, _) => evalParsedExpression(ParsedExpression(exp, expression), context)
    case e: NoSuccess => ParseFailure(s"failed to parse expression '$expression':\n$e")
  }

  private def evalParsedExpression(exp: ParsedExpression, context: Context): EvalResult = interpreter.eval(exp.expression)(context) match {
    case ValError(cause) => EvalFailure(s"failed to evaluate expression '${exp.text}':\n$cause")
    case value => EvalValue( valueMapper.unpackVal(value) )
  }

}
