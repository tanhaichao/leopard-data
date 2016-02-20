package io.leopard.data.kit.rank;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import io.leopard.redis.Redis;
import redis.clients.jedis.Tuple;

/**
 * 时间段排名实现(多个时间段合并，如小时、日、周等).
 * 
 * @author 阿海
 *
 */
public class CountRankTimeBucketImpl implements CountRank, Runnable {

	private CountRankImpl totalImpl;// 总数
	private CountRankImpl currentImpl;// 当前(最后一个时间段)

	private Redis redis;

	private String key;

	private TimeBucket timeBucket;

	public void setRedis(Redis redis) {
		this.redis = redis;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Redis getRedis() {
		return redis;
	}

	public TimeBucket getTimeBucket() {
		return timeBucket;
	}

	public void setTimeBucket(TimeBucket timeBucket) {
		this.timeBucket = timeBucket;
	}

	/**
	 * 获取时间段key.
	 * 
	 * @return
	 */
	protected String getTimeBucketKey(Date date) {
		return getTimeBucketKey(timeBucket, date);
	}

	protected static String getTimeBucketKey(TimeBucket timeBucket, Date date) {
		if (timeBucket.equals(TimeBucket.HOUR)) {
			return new SimpleDateFormat("yyyyMMddHHmm").format(date);
		}
		else if (timeBucket.equals(TimeBucket.DAY)) {
			return new SimpleDateFormat("yyyyMMddHH").format(date);
		}
		else if (timeBucket.equals(TimeBucket.WEEK)) {
			// return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
			throw new IllegalArgumentException("周时间段未实现.");
		}
		else if (timeBucket.equals(TimeBucket.MONTH)) {
			return new SimpleDateFormat("yyyyMMdd").format(date);
		}
		else if (timeBucket.equals(TimeBucket.YEAR)) {
			return new SimpleDateFormat("yyyyMM").format(date);
		}
		else {
			throw new IllegalArgumentException("未知时间段[" + timeBucket + "].");
		}
	}

	public void init() {
		{
			CountRankImpl totalImpl = new CountRankImpl();
			totalImpl.setKey(key + ":total");
			totalImpl.setRedis(redis);
			this.totalImpl = totalImpl;
		}

		{

			CountRankImpl currentImpl = new CountRankImpl() {
				@Override
				public String getKey() {
					return key + ":" + getTimeBucketKey(new Date());
				}
			};
			currentImpl.setRedis(redis);
			this.currentImpl = currentImpl;
		}
	}

	@Override
	public boolean clean() {
		Set<String> keySet = redis.keys(this.key + ":*");
		if (keySet != null) {
			for (String key : keySet) {
				redis.del(key);
			}
		}
		return totalImpl.clean();
	}

	@Override
	public boolean delete(String member) {
		currentImpl.delete(member);
		return totalImpl.delete(member);
	}

	@Override
	public Double getScore(String member) {
		return totalImpl.getScore(member);
	}

	@Override
	public List<Tuple> list(int start, int size) {
		return totalImpl.list(start, size);
	}

	@Override
	public List<String> listMembers(int start, int size) {
		return totalImpl.listMembers(start, size);
	}

	@Override
	public long incr(String member, long count) {
		this.currentImpl.incr(member, count);
		return totalImpl.incr(member, count);
	}

	@Override
	public void run() {
		List<String> keyList = this.keys(new Date());
		String[] keys = new String[keyList.size() - 1 - 20];
		for (int i = 0; i < keys.length; i++) {
			keys[i] = key + ":" + keyList.get(i);
		}
		// System.out.println("keys:" + StringUtils.join(keys, ","));
		String tmpkey = key + ":union";

		// System.out.println("tmpkey:" + tmpkey);
		redis.zunionstore(tmpkey, keys);

		try {
			// 如果时间段的只有一个时，zunionstore不会产生tmpkey.
			redis.rename(tmpkey, totalImpl.getKey());
		}
		catch (Exception e) {

		}

		String expiredKey = keyList.get(keyList.size() - 1);
		redis.del(key + ":" + expiredKey);
	}

	protected List<String> keys(Date date) {
		if (timeBucket.equals(TimeBucket.HOUR)) {
			return new TimeBucketKeysHourImpl().keys(date);
		}
		else if (timeBucket.equals(TimeBucket.DAY)) {
			return new TimeBucketKeysDayImpl().keys(date);
		}
		else {
			throw new IllegalArgumentException("未知时间段[" + timeBucket + "].");
		}
	}

	public interface TimeBucketKeys {
		List<String> keys(Date date);
	}

	public static class TimeBucketKeysHourImpl implements TimeBucketKeys {
		@Override
		public List<String> keys(Date date) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			List<String> list = new ArrayList<String>();
			for (int i = 0; i <= 60; i++) {
				String key = getTimeBucketKey(TimeBucket.HOUR, cal.getTime());
				list.add(key);
				cal.add(Calendar.MINUTE, -1);
			}
			return list;
		}
	}

	public static class TimeBucketKeysDayImpl implements TimeBucketKeys {
		@Override
		public List<String> keys(Date date) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			List<String> list = new ArrayList<String>();
			for (int i = 0; i <= 24; i++) {
				String key = getTimeBucketKey(TimeBucket.DAY, cal.getTime());
				list.add(key);
				cal.add(Calendar.HOUR, -1);
			}
			return list;
		}
	}

}
