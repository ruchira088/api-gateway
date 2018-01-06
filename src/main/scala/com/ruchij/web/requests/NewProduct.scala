package com.ruchij.web.requests

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class NewProduct(
             id: String,
             name: String,
             price: BigDecimal,
             title: Option[String],
             description: Option[String],
             tags: List[String]
)

object NewProduct extends DefaultJsonProtocol with SprayJsonSupport
{
  implicit def jsonFormat: RootJsonFormat[NewProduct] = jsonFormat6(NewProduct.apply)
}