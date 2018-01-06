package com.ruchij.web.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.ruchij.services.product.ProductService
import com.ruchij.web.requests.NewProduct
import com.ruchij.web.utils.ResponseUtils._

import scala.util.{Failure, Success}

object ProductRoute
{
  def apply(productService: ProductService): Route =
    pathPrefix("product") {
      pathEnd {
        post {
          entity(as[NewProduct]) {
            newProduct => onComplete(productService.create(newProduct)) {

              case Success(productItem) => jsonResponse(StatusCodes.Created, productItem)

              case Failure(throwable) => errorHandler(throwable)
            }
          }
        }
      } ~
      path(Segment) { productId =>
        get {
          complete(productId)
        }
      }
    }
}
