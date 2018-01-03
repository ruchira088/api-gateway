package com.ruchij.utils

import java.util.UUID

import com.ruchij.exceptions.EmptyOptionException
import play.api.libs.json.{Json, OWrites, Writes}

import scala.concurrent.Future
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object GeneralUtils
{
  def parse[A, B](value: A, f: A => B): Try[B] =
    try {
      Success(f(value))
    } catch
    {
      case NonFatal(exception) => Failure(exception)
    }

  def toTry[T](option: Option[T], exception: => Exception = EmptyOptionException): Try[T] =
    option.fold[Try[T]](Failure(exception))(Success(_))

  def predicate(condition: Boolean, onFalseException: => Exception): Future[Unit] =
    if (condition) Future.successful((): Unit) else Future.failed(onFalseException)

  def toJsonString[T](item: T)(implicit oWrites: Writes[T]): String =
    Json.stringify(Json.toJson(item))

  def uuid(): String = UUID.randomUUID().toString
}
