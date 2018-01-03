package hello

import java.io.{InputStream, OutputStream}

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import com.ruchij.utils.StreamUtils

class Handler extends RequestStreamHandler
{
	override def handleRequest(inputStream: InputStream, outputStream: OutputStream, context: Context): Unit =
	{
		println(StreamUtils.inputStreamToString(inputStream))
		outputStream.write("""{"statusCode": 201, "body": "{\"name\": \"Hello\"}"}""".getBytes)
	}
}
