package io.leopard.data.kit.rank;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class CountRankTimeBucketImplTest {

	private CountRankTimeBucketImpl countRankTimeBucketImpl = new CountRankTimeBucketImpl();

	@Test
	public void getTimeBucketKey() {
		Date date = new Date();
		countRankTimeBucketImpl.setTimeBucket(TimeBucket.MINUTE);
		Assert.assertEquals("201602202315", countRankTimeBucketImpl.getTimeBucketKey(date));
		countRankTimeBucketImpl.setTimeBucket(TimeBucket.HOUR);
		Assert.assertEquals("2016022023", countRankTimeBucketImpl.getTimeBucketKey(date));
		countRankTimeBucketImpl.setTimeBucket(TimeBucket.DAY);
		Assert.assertEquals("20160220", countRankTimeBucketImpl.getTimeBucketKey(date));
		countRankTimeBucketImpl.setTimeBucket(TimeBucket.MONTH);
		Assert.assertEquals("201602", countRankTimeBucketImpl.getTimeBucketKey(date));
		countRankTimeBucketImpl.setTimeBucket(TimeBucket.YEAR);
		Assert.assertEquals("2016", countRankTimeBucketImpl.getTimeBucketKey(date));
	}

}