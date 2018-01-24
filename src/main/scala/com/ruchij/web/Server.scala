package com.ruchij.web

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.google.inject.Guice
import com.ruchij.modules.GuiceModule
import com.ruchij.services.product.ProductService
import com.ruchij.services.{AuthenticationService, UserService}
import com.ruchij.web.routes.{ProductRoute, SessionRoute, UserRoute}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor, Promise}
import scala.sys

object Server
{
  val HTTP_PORT = 8000

  def main(args: Array[String]): Unit =
  {
    implicit val actorSystem: ActorSystem = ActorSystem("akka-http-web-server")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

    val injector = Guice.createInjector(new GuiceModule())

    val userService = injector.getInstance(classOf[UserService])
    val authenticationService = injector.getInstance(classOf[AuthenticationService])
    val productService = injector.getInstance(classOf[ProductService])

    val routes =
      path("hello") {
        get {
          complete(StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, """{"message": "Hello World"}"""))
        }
      } ~
      UserRoute(userService) ~
      SessionRoute(authenticationService) ~
      ProductRoute(productService)

    val _ = for {
      server <- Http().bindAndHandle(routes, "0.0.0.0", HTTP_PORT)
      _ = println(s"The server is listening on port: $HTTP_PORT...")
    } yield server

    Await.ready(Promise[Unit].future, Duration.Inf)
  }
}
