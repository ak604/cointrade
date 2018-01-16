package datasrc

import scala.concurrent.Future
import scala.concurrent.duration._

import javax.inject.Inject
import play.api.mvc._
import play.api.libs.ws._
import play.api.http.HttpEntity

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent.ExecutionContext
import javax.inject.Singleton
import javafx.beans.value.ObservableValue
import scala.concurrent._
import play.api.libs.json._
import play.api.Logger

@Singleton
class CoinMarketCap  @Inject() (ws: WSClient , val ec: ExecutionContext )extends DataSrcTrait {

	val baseUrl = "https://graphs.coinmarketcap.com/currencies/"
			def priceInTimeRange( coin:String , startTime : Long, endTime : Long) : Future[List[Double]]={
					implicit val eclocal : ExecutionContext = ec
	        val url= baseUrl + "/"+coin+"/"+startTime+"/"+endTime
	        Logger.debug("JSON "+ url)
					val request: WSRequest = ws.url(url)
					request.get().map{ response =>
					val temp =(response.json \ "price_usd").get.as[JsArray].value.map{ ele =>
					  ele.as[JsArray].value(1).as[Double]
					}
					temp.toList
					}
	}  
}