package io.leopard.data.kit.rank;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

}
