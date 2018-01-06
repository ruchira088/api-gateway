package com.ruchij.web.utils

import akka.http.scaladsl.model.{HttpMethod, StatusCodes}
import akka.http.scaladsl.model.headers.{`Access-Control-Allow-Headers`, `Access-Control-Allow-Methods`, `Access-Control-Allow-Origin`}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, Route}

object RouteUtils
{
  def corsPreflightResponse(httpMethods: HttpMethod*): Route =
    options {
      respondWithHeaders(
        `Access-Control-Allow-Methods`(httpMethods.toList),
        `Access-Control-Allow-Origin`.*,
        `Access-Control-Allow-Headers`("*")
      ) {
        complete(StatusCodes.OK)
      }
    }

  def corsHeaders: Directive0 = respondWithHeaders(`Access-Control-Allow-Origin`.*)
}
