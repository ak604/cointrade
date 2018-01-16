package datasrc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.CoinPrice;
import models.MarketPrice;

public class JavaUtils {

	public static List<Double> fillMissing(List<MarketPrice> coinPrices, final long startTime , final long endTime,final long interval){
		
		List<Double> res = new ArrayList<Double>();
		Iterator<MarketPrice> itr = coinPrices.iterator();
		long tym=endTime;
		MarketPrice lastCP=null;
		MarketPrice  cp =null;
		
		if(itr.hasNext()) {
			cp =itr.next();
			lastCP=cp;
			while(true) {
				if(tym==cp.timestamp ) {
					res.add(lastCP.getPrice());
					if(itr.hasNext()) {
						cp =itr.next();
						lastCP=cp;
					}
					tym-=interval;
				//	System.out.println("XX" + tym + "  "+cp.timestamp+ "  "+lastCP.price);
				}else if(cp.timestamp<tym || !itr.hasNext()) {
					res.add(lastCP.getPrice());
					lastCP=cp;
					tym-=interval;
					//System.out.println("YY" + tym + "  "+cp.timestamp+ "  "+lastCP.price);
				}else {
					if(itr.hasNext()) {
						cp =itr.next();	
						
					}
					//System.out.println(cp.price+"ZZ" + tym + "  "+cp.timestamp+ "  "+lastCP.price);
				}
				if(tym<startTime)
					break;
			}	
		}
		return res;
	}
	
	public static void main(String args[]) {
		List<CoinPrice> coinPrices= new ArrayList<CoinPrice>();
		for(long i=9;i>=5;i--) {
			CoinPrice cp = new CoinPrice("bitcoin",i,30000+i*300);
			if(i==8)
				 cp = new CoinPrice("bitcoin",99L,30000L+8*300+1);
			coinPrices.add(cp);
		}
		//JavaUtils.fillMissing(coinPrices,30000L,30000L+9*300,300L);
		
	}
}
