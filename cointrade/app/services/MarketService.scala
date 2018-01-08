package services

import datasrc._
import scala.concurrent._
import javax.inject._
import java.util.Calendar
import play.api.Logger
import constants._
import scala.util._
import models.Market
import io.ebean.Ebean

@Singleton
class MarketService @Inject()(dataSrc : Bittrex)  {
  
  implicit val ec= ExecutionContext.global
  def refreshMarket()={
    Market.truncate()
    dataSrc.getMarkets().map{ lst=>
      lst.foreach{ value=>
      Market.find.byId(value.marketName)
      value.save();
      }
      "OK"
    }
  }  
}