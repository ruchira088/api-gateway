package com.ruchij.web.routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import com.ruchij.services.UserService
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.ruchij.utils.GeneralUtils.toJsonString
import com.ruchij.web.requests.RegisterUser

import scala.util.{Failure, Success}

object UserRoute
{
  def apply(userService: UserService): Route =
    path("user") {
      post {
        entity(as[RegisterUser]) {
          registerUser =>
            onComplete(userService.create(registerUser)) {

              case Success(user) => complete(
                StatusCodes.Created,
                HttpEntity(ContentTypes.`application/json`, toJsonString(user.sanitize))
              )

              case Failure(exception) => complete(exception.getMessage)
            }
        }
      }
    }
}
