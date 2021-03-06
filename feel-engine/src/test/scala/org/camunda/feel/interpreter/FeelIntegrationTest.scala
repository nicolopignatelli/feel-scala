package org.camunda.feel.interpreter

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.camunda.feel._
import org.camunda.feel.parser.FeelParser
import org.camunda.feel.parser.FeelParser._
import org.camunda.feel.spi.VariableContext._

trait FeelIntegrationTest {

  val interpreter = new FeelInterpreter

  def eval(expression: String, variables: Map[String, Any] = Map()): Val = {
	  FeelParser.parseExpression(expression) match {
      case Success(exp, _) => {
        interpreter.eval(exp)(Context(StaticVariableContext(variables)))
      }
      case e: NoSuccess => {
        ValError(s"failed to parse expression '$expression':\n$e")
      }
    }
  }

  def date(date: String): Date = date

  def time(time: String): Time = time

  def yearMonthDuration(duration: String): YearMonthDuration = duration

  def dayTimeDuration(duration: String): DayTimeDuration = duration

}
