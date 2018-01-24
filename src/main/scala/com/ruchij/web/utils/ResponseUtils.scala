package com.ruchij.web.utils

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.StandardRoute
import com.ruchij.exceptions.{InvalidCredentials, UsernameAlreadyExistsException}
import com.ruchij.utils.GeneralUtils.toJsonString
import play.api.libs.json.{Json, Writes}

object ResponseUtils
{
  case class ResponseError(errorMessage: String)

  object ResponseError
  {
    implicit def oFormat = Json.format[ResponseError]
  }

  def errorResponse(statusCode: StatusCode, throwable: Throwable): StandardRoute =
    jsonResponse(statusCode, ResponseError(throwable.getMessage))

  def errorHandler: PartialFunction[Throwable, StandardRoute] =
  {
    case InvalidCredentials => errorResponse(StatusCodes.Unauthorized, InvalidCredentials)
    case throwable: UsernameAlreadyExistsException => errorResponse(StatusCodes.Conflict, throwable)
    case throwable => errorResponse(StatusCodes.InternalServerError, throwable)
  }

  def jsonResponse[T](statusCode: StatusCode, response: T)(implicit writes: Writes[T]): StandardRoute =
    complete(statusCode, HttpEntity(ContentTypes.`application/json`, toJsonString(response)))
}
