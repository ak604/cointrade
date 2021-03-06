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
import collection.mutable._
import scala.collection.JavaConverters._
import scala.collection.immutable.Range
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions.asScalaBuffer
import constants._

@Singleton
class CoinTradeDB  extends DataSrcTrait {

	    implicit val ec= ExecutionContext.global
			def priceInTimeRange( market:String , startTime : Long, endTime : Long) : Future[List[Double]]={
					val result =Future{
						asScalaBuffer(MarketPrice.valuesBetween(market,startTime,endTime)).toList
						
					}
					result.map{ lst =>
					 CoinTradeDB.fillMissingValues(lst.reverse,startTime,endTime).reverse
			  }
	   }	    
}

object CoinTradeDB {
  
    def normalize(tym : Long)={
      tym- tym%AppConstants.granularity
    }
    def fillMissingValues(lst :List[MarketPrice],startTime:Long, endTime: Long):List[Double]={
      val stTime= normalize(startTime)
      val enTime= normalize(endTime)
      asScalaBuffer(JavaUtils.fillMissing(lst.asJava, stTime, enTime, AppConstants.granularity)).toList.map{f => 
        val lng:Double=f;
        lng
      }
    }
    
    def filterConsecutive(lst :List[MarketPrice],endTime: Long) = {
       lst.zipWithIndex.takeWhile{ case(coinPrice,index) =>
			       val last = endTime - endTime%AppConstants.granularity
			       val expectedTimeStamp = last - AppConstants.granularity*index
   			       expectedTimeStamp ==coinPrice.timestamp
			     }
			     .map{ f => 
			      val lng: Double =f._1.getPrice();
			      lng
			    }
    }
}
