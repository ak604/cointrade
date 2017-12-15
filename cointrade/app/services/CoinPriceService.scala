package services

import datasrc._
import scala.concurrent._
import javax.inject._
import java.util.Calendar

@Singleton
class CoinPriceService @Inject()(dataSrc : CoinTradeDB)  {
  
  def lastNHours(coin:String, nHours : Int, granularityInMin : Long, startTime:Long):Future[List[Long]]={
    
    val currTime = Calendar.getInstance().getTimeInMillis()/1000;
    
    dataSrc.priceInTimeRange(coin,math.max( (currTime-(nHours*3600)),startTime), currTime)
  }
  
}