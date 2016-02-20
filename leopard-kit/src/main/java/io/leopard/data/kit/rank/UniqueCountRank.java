package io.leopard.data.kit.rank;

/**
 * 唯一数量排名(如每个用户只能增加一次).
 * 
 * @author 阿海
 *
 */
public interface UniqueCountRank extends CountRank {

	/**
	 * 增加数量.
	 * 
	 * @param member
	 * @param id
	 *            ID:比如uid，用于去重.
	 * @param count
	 * @return
	 */
	long incr(String member, String id, long count);

	boolean delete(String member, String id);
}
