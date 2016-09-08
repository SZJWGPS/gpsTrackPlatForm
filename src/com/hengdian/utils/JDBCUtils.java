package com.hengdian.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JDBCUtils {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	static Log log;

	private static String dbDriver = ""; // 数据库驱动
	private static String url = ""; // 数据库全球资源定位符
	private static String usr = ""; // 数据库登录名
	private static String pwd = ""; // 数据库登陆密码

	String daoNote = ""; // 操作信息

	static {
		log = LogFactory.getLog(JDBCUtils.class);

		Properties properties = new Properties();
		try {
			// 获取当前类所在目录下文件
			// InputStream is
			// =PrepareJDBC.class.getResourceAsStream("jdbc.properties");
			// 获取sr目录下的文件
			InputStream is = JDBCUtils.class.getClassLoader()
					.getResourceAsStream("greenplum_jdbc.properties");
			properties.load(is);
			dbDriver = properties.getProperty("dbDriver");
			url = properties.getProperty("url");
			usr = properties.getProperty("usr");
			pwd = properties.getProperty("pwd");
			is.close();
		} catch (IOException e) {
			log.error("Load jdbc.properties failed.");
			e.printStackTrace();
		}

		try {
			Class.forName(dbDriver);
		} catch (ClassNotFoundException ex) {
			log.error("Load DBDriver failed.");
		}
	}

	public JDBCUtils() {
		if (log == null) {
			log = LogFactory.getLog(JDBCUtils.class);
		}
	}

	/**
	 * 添加、更新、删除数据
	 * 
	 * @param sql
	 *            要执行的SQL语句
	 * @param val
	 *            SQL语句中占位符的匹配值
	 * @return 数据库操作信息
	 */
	public int executeUpdate(String sql, Object[] val) {
		int iCount = 0;
		try {
			if (getConnection()) {
				pstmt = conn.prepareStatement(sql);

				if (val != null) {
					// 对占位符设置值，占位符顺序从1开始，第一个参数是占位符的位置，第二个参数是占位符的值
					for (int i = 0; i < val.length; i++) {
						pstmt.setObject(i + 1, val[i]);
					}
				}

				iCount = pstmt.executeUpdate();
			}
			return iCount;
		} catch (SQLException e) {
			log.error("Execute SQL:" + sql + ".failed:\n" + e.getMessage());
			e.printStackTrace();
			return 0;
		} finally {
			closeAll();
		}
	}

	public int executeUpdateNotClose(String sql, Object[] val) {
		int iCount = 0;
		try {
			if (getConnection()) {
				conn.setAutoCommit(false);
				pstmt = conn.prepareStatement(sql);
				if (val != null) {
					// 对占位符设置值，占位符顺序从1开始，第一个参数是占位符的位置，第二个参数是占位符的值
					for (int i = 0; i < val.length; i++) {
						pstmt.setObject(i + 1, val[i]);
					}
				}

				iCount = pstmt.executeUpdate();
			}
			return iCount;
		} catch (SQLException e) {
			log.error("Execute SQL:" + sql + ".failed:\n" + e.getMessage());
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.error("Rollback failed:\n" + e.getMessage());
				e1.printStackTrace();
			} finally {
				closeAll();
			}
			return 0;
		}
	}

	public int executeUpdate(List<String> sqlList) {
		int executedCounter = 0;
		closeAll();
		try {
			if (getConnection()) {
				conn.setAutoCommit(false);
				for (String sql : sqlList) {
					pstmt = conn.prepareStatement(sql);
					pstmt.executeUpdate();
					executedCounter++;
					System.out.println(executedCounter + "++++++++++++++++");
				}
				conn.commit();
			}
			return executedCounter;
		} catch (SQLException e) {
			log.error("Execute SQL:" + sqlList.get(executedCounter + 1)
					+ ".failed:\n" + e.getMessage());
			for (int i = 0; i < sqlList.size(); i++) {
				if (i <= executedCounter) {
					System.out.println("SQL:" + sqlList.get(executedCounter)
							+ ",is executed.");
				} else {
					System.out.println("SQL:" + sqlList.get(executedCounter)
							+ ",is not executed.");
				}
			}
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.error("Rollback failed:\n" + e.getMessage());
				e1.printStackTrace();
			}
			e.printStackTrace();
			return 0;
		} finally {
			closeAll();
		}
	}

	/**
	 * 数据库查询
	 * 
	 * @param sql
	 *            要执行的SQL语句
	 * @param val
	 *            SQL语句中占位符的匹配值
	 * @return 返回查询的结果集ResultSet
	 */
	public ResultSet executeQuery(String sql, Object[] val) {
		rs = null;
		try {
			if (getConnection()) {
				pstmt = conn.prepareStatement(sql,
						ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);

				if (val != null) {
					for (int i = 1; i <= val.length; i++) {
						pstmt.setObject(i, val[i - 1]);
					}
				}
				rs = pstmt.executeQuery();
			}
		} catch (SQLException e) {
			log.error("Execute SQL:" + sql + ".failed\n" + e.getMessage());
			closeAll();
			return null;
		}
		return rs;
	}

	/**
	 * 获取数据库连接对象
	 * 
	 * @return 返回成功获取数据库连接对象与否的标识
	 */
	public boolean getConnection() {
		boolean flag = false;
		try {
			if (conn == null) {
				conn = DriverManager.getConnection(url, usr, pwd);
			}
			// connection.setAutoCommit(true);
			flag = true;
		} catch (SQLException e) {
			log.error("creatConnectionFailed!" + e.getMessage());
			e.printStackTrace();
			closeAll();
		}
		return flag;
	}

	/**
	 * 关闭数据库连接相关对象：Connection、PreparedStatement、ResultSet
	 * 
	 */
	public void closeAll() {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Failed to close ResaultSet!");
			}
			rs = null;
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Failed to close PreparedStatement!");
			}
			pstmt = null;
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Failed to close conn!");
			}
			conn = null;
		}
	}

	public boolean commit() {
		try {
			conn.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		} finally {
			closeAll();
		}
	}
}