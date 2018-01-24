package com.ruchij.services.product
import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import com.ruchij.models.ProductItem
import play.api.libs.json.Json

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.language.postfixOps
import scalaz.OptionT

@Singleton
class ProductServiceImpl @Inject()(implicit actorSystem: ActorSystem, executionContext: ExecutionContext) extends ProductService
{
  override def create(newItem: NewItem): Future[ProductItem] =
    Http().singleRequest(
      HttpRequest(
        method = HttpMethods.POST,
        uri = Uri("https://httpbin.org/anything"),
        entity = Await.result(Marshal(newItem).to[RequestEntity], 30 seconds)
      ))
      .map(response => {
        println(response)
        ProductItem("myId")
      }
      )

  override def getById(id: String): OptionT[Future, ProductItem] = ???

  override def update(id: String, item: ProductItem): Future[ProductItem] = ???

  override def delete(id: String): Future[ProductItem] = ???
}
