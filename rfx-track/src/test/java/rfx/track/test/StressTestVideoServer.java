package rfx.track.test;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import rfx.core.util.HttpClientUtil;



public class StressTestVideoServer {
	
	@Rule
	public ContiPerfRule i = new ContiPerfRule();
	
	@Test
	@PerfTest(invocations = 20000, threads = 200)
	@Required(max = 60000, average = 10000)
	public void test20kAnd200(){		
		String url = "http://localhost:8080/ping";
		String rs = HttpClientUtil.executeGet(url);		
		Assert.assertTrue(rs.length()>3);
	}
	
	
	@Test
	@PerfTest(invocations = 100000, threads = 300)
	@Required(max = 60000, average = 10000)
	public void test100kAnd300(){		
		String url = "http://localhost:8080/user/activity";
		String rs = HttpClientUtil.executeGet(url);
		if(rs.length()>3){
			
		} else {
			System.out.println(rs);
		}
		
	}
}
