package com.ruchij.services.product

import com.ruchij.models.ProductItem
import com.ruchij.services.CrudService
import com.ruchij.web.requests.NewProduct

trait ProductService extends CrudService[ProductItem]
{
  override type NewItem = NewProduct
}