package score

import data._
import javax.inject._
import services._
import scala.concurrent.ExecutionContext
import play.api.Logger

@Singleton
class Calc @Inject()(val coinPrice : CoinPrice, val ec: ExecutionContext) {
  
  
  val weightByHour = Array(1,2,3,8,10,9,8,7,6,5,4,3,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1)
  val interval=5;
  def topN(n :Int)={
    
    val scoreMap: List[(String,Int)] = Nil
    Coins.lst.take(1).foreach{coin=>
      scoreMap++List(coin, score(coin))
    }
    scoreMap
  }
   
  
  def score(coin:String):Int={
      
     implicit val localec : ExecutionContext= ec
    
     val div:Int=60/interval;
      val values = coinPrice.lastNHours(coin, 12, interval);
      val normalizedx = values.map{lst =>
        
        val normalized=lst.zipWithIndex
        .filter { case(value,i) => ((i+div)%div <5)  }
        .map{ case(e,_)=> e}
        .grouped(5)
        .map{ lst5 => 
          (lst5.sum/5)
        }.toList
        
        val diff =normalized.drop(1).zip(normalized.dropRight(1)).map{
          case(x,y) =>  Logger.debug(x +" "+y);((1000*(y-x))/x)
        }
        
        
      }
      
      
      normalizedx.foreach{f =>
       Logger.debug( f.mkString("-"))
      }
     5
  }
}