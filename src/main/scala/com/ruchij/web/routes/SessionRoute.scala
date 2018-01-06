package com.ruchij.web.routes

import akka.http.scaladsl.model.{HttpMethods, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.ruchij.services.AuthenticationService
import com.ruchij.web.requests.LoginUser
import com.ruchij.web.utils.ResponseUtils._
import com.ruchij.web.utils.RouteUtils._

import scala.util.{Failure, Success}

object SessionRoute
{
  def apply(authenticationService: AuthenticationService): Route =
    path("session") {
      post {
        entity(as[LoginUser]) {
          userCredentials =>
            onComplete(authenticationService.login(userCredentials.username, userCredentials.password)) {
              case Success(authToken) => corsHeaders {
                jsonResponse(StatusCodes.Created, authToken.sanitize)
              }

              case Failure(exception) => corsHeaders {
                errorHandler(exception)
              }
            }
        }
      } ~
      corsPreflightResponse(HttpMethods.POST)
    }
}
