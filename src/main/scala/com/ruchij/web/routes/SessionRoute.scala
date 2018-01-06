package com.ruchij.web.routes

import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, StatusCodes}
import akka.http.scaladsl.server.Directives._
import com.google.common.net.HttpHeaders
import com.ruchij.services.AuthenticationService
import com.ruchij.utils.GeneralUtils.toJsonString
import com.ruchij.web.requests.LoginUser
import com.ruchij.web.utils.RouteUtils

import scala.util.{Failure, Success}

object SessionRoute
{
  def apply(authenticationService: AuthenticationService) =
    path("session") {
      post {
        entity(as[LoginUser]) {
          userCredentials =>
            onComplete(authenticationService.login(userCredentials.username, userCredentials.password)) {
              case Success(authToken) => RouteUtils.corsHeaders {
                complete(
                  StatusCodes.Created,
                  HttpEntity(ContentTypes.`application/json`, toJsonString(authToken.sanitize))
                )
              }

              case Failure(exception) => complete(exception.getMessage)
            }
        }
      } ~
      RouteUtils.corsPreflightResponse(HttpMethods.POST)
    }
}
