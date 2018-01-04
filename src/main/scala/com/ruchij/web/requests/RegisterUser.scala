package com.ruchij.web.requests

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class RegisterUser(username: String, password: String, email: String)

object RegisterUser extends DefaultJsonProtocol with SprayJsonSupport
{
  implicit def jsonFormat: RootJsonFormat[RegisterUser] = jsonFormat3(RegisterUser.apply)
}
