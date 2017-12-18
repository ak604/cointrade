package services

import datasrc._
import scala.concurrent._
import javax.inject._
import java.util.Calendar
import play.api.Logger

@Singleton
class CoinPriceService @Inject()(dataSrc : CoinTradeDB)  {
  
  implicit val ec= ExecutionContext.global
  def lastNHours(coin:String, nHours : Int, granularity : Long, startTime:Long):Future[List[Long]]={
    val currTime = Calendar.getInstance().getTimeInMillis()/1000;
    dataSrc.priceInTimeRange(coin,math.max( (currTime-(nHours*3600)),startTime), currTime)
  }
  
  def getLastPrice(coin:String,granularity : Long)={
    val currTime = Calendar.getInstance().getTimeInMillis()/1000;
    dataSrc.priceInTimeRange(coin,currTime-granularity*3, currTime).map{lst=>
    lst.reverse.head
    }
  }
  
}