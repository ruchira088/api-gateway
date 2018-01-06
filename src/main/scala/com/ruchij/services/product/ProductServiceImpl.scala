package com.ruchij.services.product
import akka.actor.ActorSystem
import com.ruchij.models.ProductItem

import scala.concurrent.Future
import scalaz.OptionT

class ProductServiceImpl()(implicit actorSystem: ActorSystem) extends ProductService
{
  override def create(newItem: NewItem): Future[ProductItem] = ???

  override def getById(id: String): OptionT[Future, ProductItem] = ???

  override def update(id: String, item: ProductItem): Future[ProductItem] = ???

  override def delete(id: String): Future[ProductItem] = ???
}
