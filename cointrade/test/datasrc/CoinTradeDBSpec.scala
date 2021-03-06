package datasrc

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import scala.collection.immutable.Range
import score._
import org.scalatest._
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import org.scalatest.mockito._
import scala.concurrent._
import Matchers._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import scala.collection.immutable.Range
import models._
import score._
import constants._

class CoinTradeDBSpec extends FlatSpec with MockitoSugar{
 
  "CoinTradeDBSpec.filterConsecutive" should "filters only consecutive elements" in {
    var lst : List[MarketPrice] = List()
    val interval = AppConstants.granularity
    val rng = Range(5,10).reverse
    rng.foreach{ f=>
      lst = lst++List(new MarketPrice("bitcoin",1,1,f,30000+f*interval))
    }
    
    var expLst :List[Long] =List()
     rng.foreach{f=>
      expLst = expLst++List(0L+f)
    }
    assert(CoinTradeDB.filterConsecutive(lst,30000+9*interval)== expLst)
  }
  
  
 it should "filters out non-cosecutive elements" in {
    var lst : List[MarketPrice] = List()
    val interval = AppConstants.granularity
    val rng = Range(5,10).reverse
    rng.foreach{ f=>
      lst = lst++List(new MarketPrice("bitcoin",1,1,f,30000+f*interval))
    }
    lst=lst.updated(3, new MarketPrice("bitcoin",1,1,99,30000+3*interval+1))
    assert(CoinTradeDB.filterConsecutive(lst,30000+9*300)== List(9,8,7))
   
    lst=lst.updated(0, new MarketPrice("bitcoin",1,1,99,30000+3*interval+1))
    assert(CoinTradeDB.filterConsecutive(lst,30000+9*300)== List())
    
  }
  
   it should  "fills missing data" in {
    var lst : List[MarketPrice] = List()
    val interval = AppConstants.granularity
    val rng = Range(5,10).reverse
    rng.foreach{ f=>
      lst = lst++List(new MarketPrice("bitcoin",1,1,f,30000+f*interval))
    }
    
    assert(CoinTradeDB.fillMissingValues(lst,30000,30000+9*interval)== List(9,8,7,6,5,5,5,5,5,5))   
  }
  
   
  it should  " ignore extra data" in {
    var lst : List[MarketPrice] = List()
    val interval =AppConstants.granularity
    val rng = Range(5,10).reverse
    rng.foreach{ f=>
      lst = lst++List(new MarketPrice("bitcoin",1,1,f,30000+f*interval))
    }
    lst=lst.updated(1, new MarketPrice("bitcoin",1,1,99,30000+8*interval+1))
    assert(CoinTradeDB.fillMissingValues(lst,30000,30000+9*300)== List(9,99,7,6,5,5,5,5,5,5))
    
    lst=lst.updated(0, new MarketPrice("bitcoin",1,1,99,30000+8*interval+1))
    assert(CoinTradeDB.fillMissingValues(lst,30000,30000+9*300)== List(99,99,7,6,5,5,5,5,5,5))
  }
  
}