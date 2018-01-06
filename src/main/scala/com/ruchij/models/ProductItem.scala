package com.ruchij.models

import play.api.libs.json.{Json, OFormat}

case class ProductItem(id: String)

object ProductItem
{
  implicit def oFormat: OFormat[ProductItem] = Json.format[ProductItem]
}
