package io.leopard.jdbc;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;

import io.leopard.json.Json;

public class LeopardBeanPropertyRowMapper<T> implements RowMapper<T> {

	private Class<T> mappedClass;

	private Map<String, Field> mappedFields;

	public LeopardBeanPropertyRowMapper(Class<T> mappedClass) {
		this.mappedClass = mappedClass;

		mappedFields = new HashMap<String, Field>();

		Field[] fields = mappedClass.getDeclaredFields();

		for (Field field : fields) {
			mappedFields.put(field.getName(), field);
		}

	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		Assert.state(this.mappedClass != null, "Mapped class was not specified");
		T bean = BeanUtils.instantiate(this.mappedClass);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		for (int index = 1; index <= columnCount; index++) {
			String column = JdbcUtils.lookupColumnName(rsmd, index);
			column = column.replaceAll(" ", "").toLowerCase();
			Field field = this.mappedFields.get(column);

			if (field == null && column.endsWith("s")) {
				// TODO 临时实现?
				String column2 = column.substring(0, column.length() - 1) + "List";
				field = this.mappedFields.get(column2);
			}

			if (field != null) {

				Object value = getColumnValue(rs, index, field);

				field.setAccessible(true);
				try {
					field.set(bean, value);
				}
				catch (IllegalArgumentException e) {
					throw new SQLException(e.getMessage(), e);
				}
				catch (IllegalAccessException e) {
					throw new SQLException(e.getMessage(), e);
				}
			}
		}
		return bean;
	}

	protected Object getColumnValue(ResultSet rs, int index, Field field) throws SQLException {
		Class<?> requiredType = field.getType();
		JdbcUtils.getResultSetValue(rs, index, requiredType);
		Object value;
		if (String.class.equals(requiredType)) {
			value = rs.getString(index);
		}
		else if (boolean.class.equals(requiredType) || Boolean.class.equals(requiredType)) {
			value = rs.getBoolean(index);
		}
		else if (byte.class.equals(requiredType) || Byte.class.equals(requiredType)) {
			value = rs.getByte(index);
		}
		else if (short.class.equals(requiredType) || Short.class.equals(requiredType)) {
			value = rs.getShort(index);
		}
		else if (int.class.equals(requiredType) || Integer.class.equals(requiredType)) {
			value = rs.getInt(index);
		}
		else if (long.class.equals(requiredType) || Long.class.equals(requiredType)) {
			value = rs.getLong(index);
		}
		else if (float.class.equals(requiredType) || Float.class.equals(requiredType)) {
			value = rs.getFloat(index);
		}
		else if (double.class.equals(requiredType) || Double.class.equals(requiredType) || Number.class.equals(requiredType)) {
			value = rs.getDouble(index);
		}
		else if (byte[].class.equals(requiredType)) {
			value = rs.getBytes(index);
		}
		else if (java.sql.Timestamp.class.equals(requiredType) || java.util.Date.class.equals(requiredType)) {
			value = rs.getTimestamp(index);
		}
		else if (List.class.equals(requiredType)) {
			String json = rs.getString(index);
			// TODO 元素的数据类型未动态获取.
			value = Json.toListObject(json, String.class);
		}
		else {
			throw new SQLException("未知数据类型[" + requiredType.getName() + "].");
		}
		return value;
	}

}
