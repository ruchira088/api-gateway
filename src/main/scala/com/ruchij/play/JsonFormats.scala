package com.ruchij.play

import com.ruchij.utils.GeneralUtils
import org.joda.time.DateTime
import play.api.libs.json._

object JsonFormats
{
  implicit def jodaTimeFormat: Format[DateTime] =
    new Format[DateTime] {

      override def writes(dateTime: DateTime): JsValue = JsString(dateTime.toString)

      override def reads(jsValue: JsValue): JsResult[DateTime] =
        jsValue.validate[String].fold[JsResult[DateTime]](
          JsError(_),
          dateTimeString => GeneralUtils.parse(dateTimeString, DateTime.parse)
            .fold(
              exception => JsError(exception.getMessage),
              JsSuccess(_)
            )
        )
    }
}
