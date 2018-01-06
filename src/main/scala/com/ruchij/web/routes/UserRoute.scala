package com.ruchij.web.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.ruchij.services.UserService
import com.ruchij.web.requests.RegisterUser
import com.ruchij.web.utils.ResponseUtils._

import scala.util.{Failure, Success}

object UserRoute
{
  def apply(userService: UserService): Route =
    path("user") {
      post {
        entity(as[RegisterUser]) {
          registerUser =>
            onComplete(userService.create(registerUser)) {

              case Success(user) => jsonResponse(StatusCodes.Created, user.sanitize)

              case Failure(throwable) => errorHandler(throwable)
            }
        }
      }
    }
}
