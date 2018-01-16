package services

import datasrc._
import scala.concurrent._
import javax.inject._
import java.util.Calendar
import play.api.Logger
import constants._

@Singleton
class MarketPriceService @Inject()(dataSrc : CoinTradeDB)  {
  
  implicit val ec= ExecutionContext.global
  def lastNBlocks(coin:String, nBlocks : Int,startTime:Long):Future[List[Double]]={
    val currTime = Calendar.getInstance().getTimeInMillis()/1000;
    dataSrc.priceInTimeRange(coin,math.max( (currTime-(nBlocks*AppConstants.blocks)),startTime), currTime)
  }
  
  def getLastPrice(coin:String,granularity : Long)={
    val currTime = Calendar.getInstance().getTimeInMillis()/1000;
    dataSrc.priceInTimeRange(coin,currTime-granularity*3, currTime).map{lst=>
    lst.reverse.head
    }
  }
  
}