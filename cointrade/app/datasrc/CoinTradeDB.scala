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

@Singleton
class CoinTradeDB  extends DataSrcTrait {

	    implicit val ec= ExecutionContext.global
			def priceInTimeRange( coin:String , startTime : Long, endTime : Long) : Future[List[Long]]={
					val result =Future{
						asScalaBuffer(CoinPrice.valuesBetween(coin,startTime,endTime)).toList
					}
					
					result.map{ lst =>
					 CoinTradeDB.fillMissingValues(lst.reverse,startTime,endTime).reverse
			  }
	   }	    
}

object CoinTradeDB {
  
    val interval =300
   
    def normalize(tym : Long)={
      tym- tym%interval
    }
    def fillMissingValues(lst :List[CoinPrice],startTime:Long, endTime: Long):List[Long]={
      val stTime= normalize(startTime)
      val enTime= normalize(endTime)
      val total = (enTime-stTime)%interval+1
      asScalaBuffer(JavaUtils.fillMissing(lst.asJava, stTime, enTime, interval)).toList.map{f => 
        val lng:Long=f;
        lng
      }
    }
    
    def filterConsecutive(lst :List[CoinPrice],endTime: Long) = {
       lst.zipWithIndex.takeWhile{ case(coinPrice,index) =>
			       val last = endTime - endTime%CoinTradeDB.interval
			       val expectedTimeStamp = last - interval*index
   			       expectedTimeStamp ==coinPrice.timestamp
			     }
			     .map{ f => 
			      val lng: Long =f._1.price;
			      lng
			    }
    }
}
