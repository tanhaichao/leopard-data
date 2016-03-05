package io.leopard.jdbc.builder;

import io.leopard.jdbc.Jdbc;
import io.leopard.jdbc.StatementParameter;
import io.leopard.lang.Paging;
import io.leopard.lang.TimeRange;

public class QueryBuilder {

	private String tableName;

	private String rangeFieldName;
	private TimeRange range;

	private String orderFieldName;

	private Integer limitStart;
	private Integer limitSize;

	public QueryBuilder(String tableName) {
		this.tableName = tableName;
	}

	public QueryBuilder range(String fieldName, TimeRange range) {
		this.rangeFieldName = fieldName;
		this.range = range;
		return this;
	}

	public QueryBuilder order(String fieldName) {
		this.orderFieldName = fieldName;
		return this;
	}

	public QueryBuilder limit(int start, int size) {
		this.limitStart = start;
		this.limitSize = size;
		return this;
	}

	public <T> Paging<T> queryForPaging(Jdbc jdbc, Class<T> elementType) {
		StatementParameter param = new StatementParameter();
		StringBuilder sb = new StringBuilder();

		StringBuilder where = new StringBuilder();
		sb.append("select * from " + tableName);
		if (this.range != null) {
			where.append(this.rangeFieldName + ">=? and " + this.rangeFieldName + "<?");
			param.setDate(range.getStartTime());
			param.setDate(range.getEndTime());
		}

		if (where.length() > 0) {
			sb.append(" where " + where.toString());
		}
		sb.append(" order by " + orderFieldName + " desc");

		sb.append(" limit ?,?");
		param.setInt(limitStart);
		param.setInt(limitSize);

		return jdbc.queryForPaging(sb.toString(), elementType, param);

	}

}
