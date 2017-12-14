package score

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import scala.collection.immutable.Range

class CalcSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
 
  "Calc.repalceZeroWithLastElement" should { "corrects list values" in {
    assert(Calc.repalceZeroWithLastElement(List(1,2,3,0,4,5,6,0))==List(1,2,3,3,4,5,6,6))
    assert(Calc.repalceZeroWithLastElement(List(0,2,3,0,4,5,6,0))==List(0,2,3,3,4,5,6,6))
    }
  }
  
   "Calc.normalize" should { "normalizes into hours values" in {
      assert( Calc.normalize(Range(1, Calc.div+1).toList)==List( (Range(1,Calc.normalizeCnt+1).sum) /Calc.normalizeCnt))
      assert(Calc.normalize(Range(1,Calc.div).toList)==List( (Range(1,Calc.normalizeCnt+1).sum) /Calc.normalizeCnt))
      assert(Calc.normalize(Range(1,Calc.div+2).toList)==List( (Range(1,Calc.normalizeCnt+1).sum) /Calc.normalizeCnt, Calc.div+1))
      assert(Calc.normalize(Range(1,3).toList)==List(1))
      assert(Calc.normalize(Range(1,4).toList)==List(2))
    }
   }
   
  "Calc.positiveHike" should { "calculates hike values" in { 
    assert(Calc.positiveHike(List(1,2,4,8))==List(Calc.percentFactor, Calc.percentFactor,Calc.percentFactor))
    assert(Calc.positiveHike(List(2,1,2,4))==List(Calc.percentFactor,Calc.percentFactor))
    assert(Calc.positiveHike(List(1,2,4,2))==List())
    assert(Calc.positiveHike(List(25,50,100,102))==List(Calc.percentFactor*2/100,Calc.percentFactor, Calc.percentFactor))
    }
  }
  
}