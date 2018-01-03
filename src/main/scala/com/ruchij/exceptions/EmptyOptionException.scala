package com.ruchij.exceptions

object EmptyOptionException extends Exception
{
  override def getMessage: String = "Empty Option."
}
