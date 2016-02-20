package io.leopard.data.kit.rank;

public class UniqueCountRankTimeBucketImpl extends CountRankTimeBucketImpl implements UniqueCountRank {

	@Override
	public long incr(String member, String id, long count) {
		String key = this.key + ":unique";

		String uniqueId = member + ":" + id;

		Double time = redis.zscore(key, uniqueId);
		System.out.println("time:" + time + " id:" + id);
		if (isCurrentTimeBucket(time)) {
			// 当前时间段已经操作过
			return 0;
		}
		long result = this.incr(member, count);

		redis.zadd(key, System.currentTimeMillis(), uniqueId);
		return result;
	}

	protected long getTimeBucketMillis() {
		if (timeBucket == TimeBucket.DAY) {
			return 1000L * 60 * 60 * 24;
		}
		else {
			throw new IllegalArgumentException("未知时间段[" + timeBucket + "].");
		}
	}

	protected boolean isCurrentTimeBucket(Double time) {
		if (time == null) {
			return false;
		}

		long timeBucketMillis = this.getTimeBucketMillis();
		long millls = System.currentTimeMillis() - time.longValue();
		return millls < timeBucketMillis;
	}

}
