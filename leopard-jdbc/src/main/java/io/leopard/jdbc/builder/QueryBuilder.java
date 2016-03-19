package io.leopard.jdbc.builder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

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

	private Map<String, Object> whereMap = new LinkedHashMap<String, Object>();

	public QueryBuilder(String tableName) {
		this.tableName = tableName;
	}

	public QueryBuilder range(String fieldName, TimeRange range) {
		this.rangeFieldName = fieldName;
		this.range = range;
		return this;
	}

	public QueryBuilder addWhere(String fieldName, String value) {
		whereMap.put(fieldName, value);
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

	protected String getRangeSQL(StatementParameter param) {
		StringBuilder rangeSQL = new StringBuilder();
		if (this.range != null) {
			if (range.getStartTime() != null) {
				rangeSQL.append(this.rangeFieldName + ">=?");
				param.setDate(range.getStartTime());
			}

			if (range.getEndTime() != null) {
				if (rangeSQL.length() > 0) {
					rangeSQL.append(" and ");
				}
				rangeSQL.append(this.rangeFieldName + "<=?");
				param.setDate(range.getEndTime());
			}
		}

		return rangeSQL.toString();
	}

	protected String getWhereSQL(StatementParameter param) {
		StringBuilder whereSQL = new StringBuilder();
		for (Entry<String, Object> entry : this.whereMap.entrySet()) {
			String fieldName = entry.getKey();
			Object value = entry.getValue();
			if (whereSQL.length() > 0) {
				whereSQL.append(" and ");
			}
			whereSQL.append(fieldName).append("=?");
			param.setObject(value.getClass(), value);
		}

		return whereSQL.toString();
	}

	public <T> Paging<T> queryForPaging(Jdbc jdbc, Class<T> elementType) {
		StatementParameter param = new StatementParameter();
		StringBuilder sb = new StringBuilder();

		sb.append("select * from " + tableName);
		StringBuilder where = new StringBuilder();

		{
			String rangeSQL = this.getRangeSQL(param);
			if (rangeSQL.length() > 0) {
				where.append(rangeSQL);
			}
			String whereSQL = this.getWhereSQL(param);
			if (whereSQL.length() > 0) {
				if (where.length() > 0) {
					where.append(" and ");
				}
				where.append(whereSQL);
			}

		}

		if (where.length() > 0) {
			sb.append(" where " + where.toString());
		}

		sb.append(" order by " + orderFieldName + " desc");

		sb.append(" limit ?,?");
		param.setInt(limitStart);
		param.setInt(limitSize);

		String sql = sb.toString();
		// System.out.println("sql:" + sql);
		return jdbc.queryForPaging(sql, elementType, param);

	}

}
