package services

import datasrc._
import scala.concurrent._
import javax.inject._
import java.util.Calendar

@Singleton
class CoinPrice @Inject()(dataSrc : CoinMarketCap)  {
  
  def lastNHours(coin:String, nHours : Int, granularityInMin : Int):Future[List[Int]]={
    
    val currTime = Calendar.getInstance().getTimeInMillis()
    dataSrc.priceInTimeRange(coin, currTime-(nHours*3600*1000), currTime)
  }
  
}