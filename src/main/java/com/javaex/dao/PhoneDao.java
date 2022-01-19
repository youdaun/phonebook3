package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.PersonVo;

public class PhoneDao {

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "phonedb";
	private String pw = "phonedb";

	private void getConnection() {
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName(driver);

			// 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);

		} catch (SQLException e) {
			System.out.println("error:" + e);

		}

	}

	private void close() {
		// 5. 자원정리
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	public void PersonInsert(PersonVo pvo) {

		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " insert into person ";
			query += " values (seq_person_id.nextval, ?, ?, ?) ";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, pvo.getName());
			pstmt.setString(2, pvo.getHp());
			pstmt.setString(3, pvo.getCompany());

			int count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println("[" + count + "건 등록되었습니다.]");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

	}

	public void PersonUpdate(int index, PersonVo pvo) {

		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " update person ";
			query += " set    name = ?, ";
			query += "        hp = ?, ";
			query += "        company = ? ";
			query += " where  person_id = ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, pvo.getName());
			pstmt.setString(2, pvo.getHp());
			pstmt.setString(3, pvo.getCompany());
			pstmt.setInt(4, index);

			int count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println("[" + count + " 건이 수정되었습니다.]");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

	}

	public void PersonDelete(int index) {

		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " delete from person ";
			query += " where person_id = ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, index);

			int count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println("[" + count + "건이 삭제되었습니다.]");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

	}

	public List<PersonVo> getPersonList() {
		return getPersonList("");
	}

	public List<PersonVo> getPersonList(String keyword) {

		getConnection();
		List<PersonVo> pList = new ArrayList<PersonVo>();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " select person_id, name, hp, company ";
			query += " from person ";
			query += " where (name like '%'||?||'%' or hp like '%'||?||'%' or company like '%'||?||'%') ";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, keyword);
			pstmt.setString(2, keyword);
			pstmt.setString(3, keyword);

			// select문은 update가 아니라 query!!
			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {

				int person_id = rs.getInt("person_id");
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");

				PersonVo pvo = new PersonVo(person_id, name, hp, company);
				pList.add(pvo);

			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return pList;

	}

	public PersonVo getPerson(int index) {

		getConnection();
		PersonVo pvo = new PersonVo();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " select person_id, name, hp, company ";
			query += " from person ";
			query += " where person_id = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, index);

			// select문은 update가 아니라 query!!
			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int person_id = rs.getInt("person_id");
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");
				
				pvo.setPerson_id(person_id);
				pvo.setName(name);
				pvo.setHp(hp);
				pvo.setCompany(company);

			}
		} catch (Exception e) {
			System.out.println("error:" + e);
		}
		close();

		return pvo;
	}
}