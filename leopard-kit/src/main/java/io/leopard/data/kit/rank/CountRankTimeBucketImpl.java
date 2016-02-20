package io.leopard.data.kit.rank;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.leopard.redis.Redis;
import redis.clients.jedis.Tuple;

/**
 * 时间段排名实现(多个时间段合并，如小时、日、周等).
 * 
 * @author 阿海
 *
 */
public class CountRankTimeBucketImpl implements CountRank {

	private CountRank totalImpl;// 总数
	private CountRank currentImpl;// 当前(最后一个时间段)

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

	public CountRank getTotalImpl() {
		return totalImpl;
	}

	public void setTotalImpl(CountRank totalImpl) {
		this.totalImpl = totalImpl;
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
		if (this.timeBucket.equals(TimeBucket.MINUTE)) {
			return new SimpleDateFormat("yyyyMMddHHmm").format(date);
		}
		else if (this.timeBucket.equals(TimeBucket.HOUR)) {
			return new SimpleDateFormat("yyyyMMddHH").format(date);
		}
		else if (this.timeBucket.equals(TimeBucket.DAY)) {
			return new SimpleDateFormat("yyyyMMdd").format(date);
		}
		else if (this.timeBucket.equals(TimeBucket.WEEK)) {
			// return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
			throw new IllegalArgumentException("周时间段未实现.");
		}
		else if (this.timeBucket.equals(TimeBucket.MONTH)) {
			return new SimpleDateFormat("yyyyMM").format(date);
		}
		else if (this.timeBucket.equals(TimeBucket.YEAR)) {
			return new SimpleDateFormat("yyyy").format(date);
		}
		else {
			throw new IllegalArgumentException("未知时间段[" + this.timeBucket + "].");
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(String member) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Double getScore(String member) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tuple> list(int start, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> listMembers(int start, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long incr(String member, long count) {
		// TODO Auto-generated method stub
		return 0;
	}

}
