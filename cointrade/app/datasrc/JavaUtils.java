package datasrc;

import java.util.*;

import models.CoinPrice;
import play.api.Logger;

public class JavaUtils {

	public static List<Long> fillMissing(List<CoinPrice> coinPrices, final long startTime , final long endTime,final long interval){
		
		List<Long> res = new ArrayList<Long>();
		Iterator<CoinPrice> itr = coinPrices.iterator();
		long tym=endTime;
		long lastPrice=0;
		CoinPrice  cp =null;
		
		if(itr.hasNext()) {
			cp =itr.next();
			lastPrice=cp.price;
			while(true) {
				if(tym==cp.timestamp ) {
					res.add(lastPrice);
					if(itr.hasNext()) {
						cp =itr.next();
						lastPrice=cp.price;
					}
					tym-=interval;
				}else if(cp.timestamp<tym || !itr.hasNext()) {
					res.add(lastPrice);
					lastPrice=cp.price;
					tym-=interval;
				}else {
					if(itr.hasNext()) {
						cp =itr.next();	
					}
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
			CoinPrice cp = new CoinPrice(1L,"bitcoin",i,30000+i*300);
			if(i==8)
				 cp = new CoinPrice(1L,"bitcoin",99L,30000L+8*300+1);
			coinPrices.add(cp);
		}
		JavaUtils.fillMissing(coinPrices,30000L,30000L+9*300,300L);
		
	}
}
