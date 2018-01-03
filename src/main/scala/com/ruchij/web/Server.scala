package com.ruchij.web

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.google.inject.Guice
import com.ruchij.modules.GuiceModule
import com.ruchij.services.UserService
import com.ruchij.web.requests.RegisterUser
import play.api.libs.json.Json

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor, Promise}
import scala.util.{Failure, Success}

object Server
{
  val HTTP_PORT = 8001

  def main(args: Array[String]): Unit =
  {
    implicit val actorSystem: ActorSystem = ActorSystem("akka-http-web-server")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

    val injector = Guice.createInjector(new GuiceModule())

    val userService = injector.getInstance(classOf[UserService])

    val routes =
      path("hello") {
        get {
          complete(StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, """{"message": "Hello World"}"""))
        }
      } ~
      path("user") {
        post {
          entity(as[RegisterUser]) {
            registerUser => {
              onComplete(userService.create(registerUser)) {

                case Success(user) => complete(
                  StatusCodes.Created,
                  HttpEntity(ContentTypes.`application/json`, Json.stringify(Json.toJson(user.sanitize)))
                )

                case Failure(exception) => complete(exception.getMessage)
              }
            }
          }
        }
      }

    val _ = for {
      server <- Http().bindAndHandle(routes, "0.0.0.0", HTTP_PORT)
      _ = println(s"The server is listening on port: $HTTP_PORT...")
    } yield server

    Await.ready(Promise[Unit].future, Duration.Inf)
  }
}
