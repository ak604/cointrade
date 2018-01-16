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
import models._
import play.api.libs.functional.syntax._


@Singleton
class Bittrex  @Inject() (ws: WSClient , val ec: ExecutionContext ) {

  case class MarketCaseClass( MarketName:String, MarketCurrency:String , BaseCurrency :String , MinTradeSize:Double, IsActive:Boolean)
  
  implicit val marketReads: Reads[MarketCaseClass] = (
     (JsPath \ "MarketName").read[String] and 
     (JsPath \ "MarketCurrency").read[String] and 
     (JsPath \ "BaseCurrency").read[String] and
     (JsPath \ "MinTradeSize").read[Double] and 
     (JsPath \ "IsActive").read[Boolean]
   )(MarketCaseClass.apply _)

	val baseUrl = "https://bittrex.com/api/v1.1/public/"

	def getMarkets() : Future[List[Market]]={
			implicit val eclocal : ExecutionContext = ec
					val url= baseUrl + "getMarkets"
					val request: WSRequest = ws.url(url)
					request.get().map{ response =>
					val temp =(response.json \ "result").get.as[JsArray].value.map{ ele =>
					ele.as[MarketCaseClass]
					}
					temp.toList.map{ value => new Market(value.MarketName,value.MarketCurrency, value.BaseCurrency, value.MinTradeSize, value.IsActive) 
					}
	}
	}
  
  
}