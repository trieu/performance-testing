package rfx.track.common;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisException;
import rfx.core.configs.RedisPoolConfigs;
import rfx.core.nosql.jedis.RedisCommand;
import rfx.core.util.DateTimeUtil;

import com.google.gson.Gson;


public class RealtimeTrackingUtil {
	private static final String PREFIX = "m:";

	static Logger logger = Logger.getLogger(RealtimeTrackingUtil.class);

	public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
	public static final String DATE_HOUR_FORMAT_PATTERN = "yyyy-MM-dd-HH";
	public static final String DATE_HOUR_MINUTE_FORMAT_PATTERN = "yyyy-MM-dd-HH-mm";
	public static final String DATE_HOUR_MINUTE_SECOND_FORMAT_PATTERN = "yyyy-MM-dd-HH-mm-ss";

	private static final int AFTER_2_DAYS = 86400 * 2;
	private static final int AFTER_4_DAYS = 86400 * 4;
		
	static ShardedJedisPool jedisPool = RedisPoolConfigs.load().get("realtimeDataStats").getShardedJedisPool();
	
	public static boolean updateKafkaLogEvent(int unixtime, final String event){
		Date date = new Date(unixtime*1000L);
		final String dateStr = DateTimeUtil.formatDate(date ,DATE_FORMAT_PATTERN);
		final String dateHourStr = DateTimeUtil.formatDate(date ,DATE_HOUR_FORMAT_PATTERN);
				
		boolean commited = new RedisCommand<Boolean>(jedisPool) {
			@Override
			protected Boolean build() throws JedisException {
				String keyD = PREFIX+dateStr;
				String keyH = PREFIX+dateHourStr;
				
				Pipeline p = jedis.pipelined();				
				p.hincrBy(keyD, "kk:"+event , 1L);
				p.expire(keyD, AFTER_4_DAYS);
				p.hincrBy(keyH, "kk:"+event , 1L);
				p.expire(keyH, AFTER_2_DAYS);
				p.sync();
				return true;
			}
		}.execute();		
		return commited;
	}
	
	public static String getAllKafkaLogEvents(final String pkey){
		final String key;
		if(pkey == null){
			key = PREFIX+DateTimeUtil.formatDate(new Date() ,DATE_HOUR_FORMAT_PATTERN);
		} else {
			key = pkey;
		}
		if(key.startsWith(PREFIX)){
			String s = new RedisCommand<String>(jedisPool) {
				@Override
				protected String build() throws JedisException {
					Map<String, String> map = jedis.hgetAll(key);					
					return new Gson().toJson(map);
				}
			}.execute();
			return s;
		}
		return "No data";
	}

}
