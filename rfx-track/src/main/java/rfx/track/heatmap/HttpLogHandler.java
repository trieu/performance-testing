package rfx.track.heatmap;

import org.vertx.java.core.http.HttpServerRequest;

import rfx.core.util.DateTimeUtil;
import rfx.core.util.StringPool;
import rfx.track.common.RealtimeTrackingUtil;
import server.http.handler.BaseHttpHandler;
import server.http.util.KafkaLogHandlerUtil;

public class HttpLogHandler implements BaseHttpHandler {
		
	private static final String PONG = "PONG";	
	private static final String FAVICON_ICO = "/favicon.ico";	
	private static final String PING = "/ping";
		

	static final String logTrack = "/user/activity";
	static final String logTrackKafkaKey = getKafkaKey(logTrack);	
	
	
	static final String getKafkaKey(String logUri){
		return logUri.replaceAll("/", "_");
	}
	
	
	@Override
	public void handle(HttpServerRequest req) {
		String uri = req.uri();		
		//System.out.println("URI: " + uri);
		
		try {
			if (uri.equalsIgnoreCase(FAVICON_ICO)) {
				KafkaLogHandlerUtil.trackingResponse(req);
			}
			else if (uri.equalsIgnoreCase(PING)) {
				req.response().end(PONG);				
			}		
			else if(uri.startsWith(logTrack)){				
				KafkaLogHandlerUtil.logAndResponseImage1px(req, logTrackKafkaKey);	
				RealtimeTrackingUtil.updateKafkaLogEvent(DateTimeUtil.currentUnixTimestamp(), "ua");
			} 
			else {
				req.response().end("Not handler found for uri:"+uri);
			}
		} catch (Exception e) {
			req.response().end("uri:"+uri + " error:"+e.getMessage());
		}
	}

	@Override
	public String getPathKey() {
		return StringPool.STAR;
	}

}
