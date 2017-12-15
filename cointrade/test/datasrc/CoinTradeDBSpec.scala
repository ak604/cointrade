package datasrc

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import scala.collection.immutable.Range
import models._
import score._

class CoinTradeDBSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
 
  "CoinTradeDBSpec.filterConsecutive" should { "filters only consecutive elements" in {
    var lst : List[CoinPrice] = List()
    val interval = Calc.interval*60
    val rng = Range(5,10).reverse
    rng.foreach{ f=>
      lst = lst++List(new CoinPrice(1,"bitcoin",f,30000+f*interval))
    }
    
    var expLst :List[Long] =List()
     rng.foreach{f=>
      expLst = expLst++List(0L+f)
    }
    assert(CoinTradeDB.filterConsecutive(lst,30000+9*interval)== expLst)
    }
  }
  
  
  "CoinTradeDBSpec.filterConsecutive" should { "filters out non-cosecutive elements" in {
    var lst : List[CoinPrice] = List()
    val interval = Calc.interval*60
    val rng = Range(5,10).reverse
    rng.foreach{ f=>
      lst = lst++List(new CoinPrice(1,"bitcoin",f,30000+f*interval))
    }
    lst=lst.updated(3, new CoinPrice(1,"bitcoin",99,30000+3*interval+1))
    assert(CoinTradeDB.filterConsecutive(lst,30000+9*300)== List(9,8,7))
   
    lst=lst.updated(0, new CoinPrice(1,"bitcoin",99,30000+3*interval+1))
    assert(CoinTradeDB.filterConsecutive(lst,30000+9*300)== List())
    
    }
  }
  
   "CoinTradeDBSpec.fillMissingValues" should { "fills missing data" in {
    var lst : List[CoinPrice] = List()
    val interval = Calc.interval*60
    val rng = Range(5,10).reverse
    rng.foreach{ f=>
      lst = lst++List(new CoinPrice(1,"bitcoin",f,30000+f*interval))
    }
    
    assert(CoinTradeDB.fillMissingValues(lst,30000,30000+9*interval)== List(9,8,7,6,5,5,5,5,5,5))
    }
    
  }
  
   
    "CoinTradeDBSpec.fillMissingValues" should { " ignore extra data" in {
    var lst : List[CoinPrice] = List()
    val interval = Calc.interval*60
    val rng = Range(5,10).reverse
    rng.foreach{ f=>
      lst = lst++List(new CoinPrice(1,"bitcoin",f,30000+f*interval))
    }
    lst=lst.updated(1, new CoinPrice(1,"bitcoin",99,30000+8*interval+1))
    assert(CoinTradeDB.fillMissingValues(lst,30000,30000+9*300)== List(9,99,7,6,5,5,5,5,5,5))
    
    lst=lst.updated(0, new CoinPrice(1,"bitcoin",99,30000+8*interval+1))
    assert(CoinTradeDB.fillMissingValues(lst,30000,30000+9*300)== List(99,99,7,6,5,5,5,5,5,5))
    
    }
  }
  
}