package score

import data._
import javax.inject._
import services._
import scala.concurrent.ExecutionContext
import play.api.Logger

@Singleton
class Calc @Inject()(val coinPrice : CoinPrice, val ec: ExecutionContext) {
  
  val interval=5;
  def topN(n :Int)={
    
    val scoreMap: List[(String,Int)] = Nil
    Coins.lst.take(3).foreach{coin=>
      scoreMap++List(coin, score(coin))
    }
    scoreMap
  }
  
 
  def score(coin:String):Int={
      
     implicit val localec : ExecutionContext= ec
    
     val div:Int=60/interval;
      val values = coinPrice.lastNHours(coin, 12, interval);
      val normalized = values.map{lst =>
         lst.zipWithIndex
        .filter { case(value,i) => ((i+div)%div <5)  }
        .map{ case(e,_)=> e}
        .grouped(5)
        .map{ lst5 => 
          (lst5.sum/5)
        }.toList
        
      }
      normalized.foreach{f =>
       Logger.debug( f.mkString("-"))
      }
      5      
  }
}