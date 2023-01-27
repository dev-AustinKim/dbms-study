package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import domain.UserVO;

public class MyUserDAO {

	public Connection connection;  // ���� ��ü�� ����� ���� ����
	//   public Statement statement;     // �̰� ���ο��� preparedStatement �����
	public PreparedStatement preparedStatement;   // ���� ���� ��ü
	public ResultSet resultSet;  // ��� ���̺� ��ü
	private final int KEY = 3; // ��ȣȭ�� ���� Ű��.

	//   ���̵� �ߺ��˻�
	public boolean checkId(String userIdentification) {
		String query = "SELECT COUNT(USER_ID) FROM TBL_USER WHERE USER_IDENTIFICATION = ?";
		boolean result = false;
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userIdentification);
//			select�� executeQuery, ���������� executeUpdate ����ؾ��Ѵ�. 
//			executeQuery�� ��ȸ�� �ϱ� ������ �� ��ȸ�� ����� ���� 2���� �迭�� resultSet�� �־��� �� ����Ѵ�.
//			executeUpdate�� ������Ʈ�� �ϱ� ������ �� ����� resultSet�� ���� �ʰ� �׳� executeUpdate ����Ѵ�.
			resultSet = preparedStatement.executeQuery();   

			resultSet.next();
			result = resultSet.getInt(1) == 0; // COUNT(USER_ID) == 0�� boolean ������ result�� �־��ش�.

		} catch (SQLException e) {
			System.out.println("checkId(String) SQL�� ����");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null) {
					resultSet.close();
				}
				if(preparedStatement != null) {
					preparedStatement.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return result;   // �ߺ��� ���� �� true ��밡���ϸ� true ��� �Ұ����ϸ� false
	}

	//   ȸ������
	public void signUp(UserVO userVO) {
		String query = "INSERT INTO TBL_USER"
				+ "(USER_ID, USER_IDENTIFICATION, USER_NAME, USER_PASSWORD,"
				+ "USER_PHONE, USER_NICKNAME, USER_EMAIL, USER_ADDRESS,"
				+ "USER_BIRTH, USER_GENDER, USER_RECOMMENDER_ID) "
				+ "VALUES(SEQ_USER.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
		connection = DBConnecter.getConnection();

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userVO.getUserIdentification());
			preparedStatement.setString(2, userVO.getUserName());
			preparedStatement.setString(3, encryptPassword(userVO.getUserPassword()));
			preparedStatement.setString(4, userVO.getUserPhone());
			preparedStatement.setString(5, userVO.getUserNickname());
			preparedStatement.setString(6, userVO.getUserEmail());
			preparedStatement.setString(7, userVO.getUserAddress());
			preparedStatement.setString(8, userVO.getUserBirth());
			preparedStatement.setString(9, userVO.getUserGender());
			preparedStatement.setString(10, userVO.getUserRecommenderId());
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("signUp SQL�� ����");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(preparedStatement != null) {
					preparedStatement.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException();
			}
		}

	}

	//   �α���
	public Long login(String userIdentification, String userPassword) {
		String query = "SELECT USER_ID FROM TBL_USER WHERE USER_IDENTIFICATION = ? AND USER_PASSWORD = ? ";
		Long result = 0L;
		connection = DBConnecter.getConnection();

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userIdentification);
			preparedStatement.setString(2, encryptPassword(userPassword));

			resultSet = preparedStatement.executeQuery();

//			 ���� �־�߸� ������ �� �ִ�. DB�ʿ��� null�� ���� �����ϱ� ���⼭ Ȯ���ϰ� ���̵�� ��й�ȣ�� �´� userId�� �ִٸ� if���� �����ϴ� ���̴�.
			if(resultSet.next()) { 
				result = resultSet.getLong(1);
			}

		} catch (SQLException e) {
			System.out.println("login SQL�� ����");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null) {
					resultSet.close();
				}
				if(preparedStatement != null) {
					preparedStatement.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				throw new RuntimeException();
			}
		}

		return result;
	}   

	//   ��ȣȭ -> �����δ� base64�� �̿�.
	public String encryptPassword(String userPassword) {
		String result = "";
//		�Ű������� �޾ƿ� password�� ù��° �ڸ����� �ϳ��ϳ� key���� �����༭ result�� �������ش�.
		for (int i = 0; i < userPassword.length(); i++) {
			result += userPassword.charAt(i) * KEY;
		}
		return result;
	}


	//   ȸ��Ż��
	public void delete(Long userId) {
		String query = "DELETE FROM TBL_USER WHERE USER_ID = ?";
		connection = DBConnecter.getConnection();

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, userId);

			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("delete SQL�� ����");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(preparedStatement != null) {
					preparedStatement.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				throw new RuntimeException();
			}
		}

	}

	//   ���̵� ã��
	public String findId(String userPhone) {
		String query = 
				"SELECT USER_IDENTIFICATION FROM TBL_USER WHERE USER_PHONE = ?";
		connection = DBConnecter.getConnection();
		String result = null;
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userPhone);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				result = resultSet.getString(1); // ���������� �޾ƿ� USER_IDENTIFICATION�� result�� �־��ش�.
			}

		} catch (SQLException e) {
			System.out.println("select(String) SQL�� ����");
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null) {
					resultSet.close();
				}
				if(preparedStatement != null) {
					preparedStatement.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	//   ��й�ȣ ����
	public void changePassword(String userIdentification, String userPassword) {
		String query = "UPDATE TBL_USER SET USER_PASSWORD = ? WHERE USER_IDENTIFICATION = ?";
		connection = DBConnecter.getConnection();

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, encryptPassword(userPassword));
			preparedStatement.setString(2, userIdentification);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("changePassword SQL�� ����");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(preparedStatement != null) {
					preparedStatement.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException();
			}
		}
	}

	//   ȸ������ ����
	public void update(UserVO userVO) {
		String query = "UPDATE TBL_USER "
				+ "SET USER_NAME = ?, USER_PASSWORD = ?, USER_PHONE = ?, "
				+ "USER_NICKNAME = ?, USER_EMAIL = ?, USER_ADDRESS = ? "
				+ "WHERE USER_ID = ?";

		connection = DBConnecter.getConnection();
		try {

			preparedStatement = connection.prepareStatement(query);
			// preparedStatement.setString�� ����ǥ�� ä���ִ� ���ε� ���������� ���ʿ��� ���������� ����ǥ�� ä���ش�. �� ���������� �� ���ʿ� �ִ� ����ǥ�� 1���̰� �ι�° �ִ� ����ǥ�� 2���̰�... �� �����ʿ� �ִ� ����ǥ�� ������ ��ȣ�̴�.
			preparedStatement.setString(1, userVO.getUserName()); 
			preparedStatement.setString(2, encryptPassword(userVO.getUserPassword()));
			preparedStatement.setString(3, userVO.getUserPhone());
			preparedStatement.setString(4, userVO.getUserNickname());
			preparedStatement.setString(5, userVO.getUserEmail());
			preparedStatement.setString(6, userVO.getUserAddress());
			preparedStatement.setLong(7, userVO.getUserId());

			preparedStatement.executeUpdate();


		} catch (SQLException e) {
			System.out.println("select(Long) SQL�� ����");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	//   ȸ������ ��ȸ
	public UserVO select(Long userId) {
		String query = 
				"SELECT USER_ID, USER_IDENTIFICATION, USER_NAME, USER_PASSWORD,"
						+ " USER_PHONE, USER_NICKNAME, USER_EMAIL, USER_ADDRESS, USER_BIRTH,"
						+ " USER_GENDER, USER_RECOMMENDER_ID "
						+ "FROM TBL_USER WHERE USER_ID = ?";
		UserVO userVO = null;
		connection = DBConnecter.getConnection();

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, userId);
			resultSet = preparedStatement.executeQuery();

			if(resultSet.next()) {
				userVO = new UserVO();
				userVO.setUserId(resultSet.getLong(1));
				userVO.setUserIdentification(resultSet.getString(2)); 
				userVO.setUserName(resultSet.getString(3));
				userVO.setUserPassword(resultSet.getString(4));
				userVO.setUserPhone(resultSet.getString(5));
				userVO.setUserNickname(resultSet.getString(6));
				userVO.setUserEmail(resultSet.getString(7));
				userVO.setUserAddress(resultSet.getString(8));
				userVO.setUserBirth(resultSet.getString(9));
				userVO.setUserGender(resultSet.getString(10));
				userVO.setUserRecommenderId(resultSet.getString(11));
			}

		} catch (SQLException e) {
			System.out.println("select(Long) SQL�� ����");
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null) {
					resultSet.close();
				}
				if(preparedStatement != null) {
					preparedStatement.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

		return userVO;
	}
	//  ��õ��
	public int countRecommend(String userIdentification) {
		String query = "SELECT COUNT(USER_RECOMMENDER_ID) FROM TBL_USER WHERE USER_RECOMMENDER_ID = ?";
		int result = 0;
		connection = DBConnecter.getConnection();

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userIdentification);
			resultSet = preparedStatement.executeQuery();

			resultSet.next();
			result = resultSet.getInt(1); // result�� COUNT(USER_RECOMMENDER_ID)�� ���� �־��ش�.

		} catch (SQLException e) {
			System.out.println("countRecommend(String) SQL�� ����");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}



	//  ���� ��õ�� ���
	public ArrayList<UserVO> recommendedMe(String userRecommenderId) { // �� ���̵� �޾Ƽ� ��õ�� ����� �� ���̵� ������ �� ����� ��ü ������ ��µ� ���� ��õ�� ����� ������� �𸣴ϱ� Arraylist�� ��´�.
		String query = "SELECT USER_ID, USER_IDENTIFICATION, USER_NAME, USER_PASSWORD,"
				+ " USER_PHONE, USER_NICKNAME, USER_EMAIL, USER_ADDRESS, USER_BIRTH, "
				+ "USER_GENDER, USER_RECOMMENDER_ID " + "FROM TBL_USER WHERE USER_RECOMMENDER_ID = ?";
		ArrayList<UserVO> users = new ArrayList<UserVO>();
		UserVO userVO = null;
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userRecommenderId); // ù��° ����ǥ�� userRecommenderId�� �־��ش�.
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) { // Ȥ�� �𸣴ϱ� SELECT�� ��ȸ�ϴ� ���� �ִ���, �� NULL�� �ƴ��� �׻� �˻�������.
				userVO = new UserVO();
				userVO.setUserId(resultSet.getLong(1)); // SELECT ù��° ���� USER_ID�� ��� ǥ�� resultSet�� userVO�� ù��° ���� userId�� �־��ش�.
				userVO.setUserIdentification(resultSet.getString(2)); // SELECT �ι�° ���� USER_IDENTIFICATION�� ��� ǥ�� resultSet�� userVO�� �ι�° ���� userIdentification�� �־��ش�.
				userVO.setUserName(resultSet.getString(3)); // ...
				userVO.setUserPassword(resultSet.getString(4)); // ...
				userVO.setUserPhone(resultSet.getString(5)); // ...
				userVO.setUserNickname(resultSet.getString(6)); // ...
				userVO.setUserEmail(resultSet.getString(7)); // ...
				userVO.setUserAddress(resultSet.getString(8)); // ...
				userVO.setUserBirth(resultSet.getString(9)); // ...
				userVO.setUserGender(resultSet.getString(10)); // ...
				userVO.setUserRecommenderId(resultSet.getString(11)); // ...

				users.add(userVO);
			}

		} catch (SQLException e) {
			System.out.println("recommendMe(String) SQL�� ����");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return users;
	}
	//  ���� ��õ�� ���
	public UserVO recommendMe(String userIdentification) { // �� ���̵� �Ű������� �޾Ƽ� ���� ��õ�� ����� ��ȸ�Ѵ�.
		String query = "SELECT USER_ID, USER_IDENTIFICATION, USER_NAME, USER_PASSWORD,"
				+ " USER_PHONE, USER_NICKNAME, USER_EMAIL, USER_ADDRESS, USER_BIRTH, "
				+ "USER_GENDER, USER_RECOMMENDER_ID "
				+ "FROM TBL_USER WHERE USER_IDENTIFICATION = "
				+ "(SELECT USER_RECOMMENDER_ID FROM TBL_USER WHERE USER_IDENTIFICATION = ?)";
		UserVO userVO = null;
		connection = DBConnecter.getConnection();
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userIdentification);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				userVO = new UserVO();
				userVO.setUserId(resultSet.getLong(1));
				userVO.setUserIdentification(resultSet.getString(2));
				userVO.setUserName(resultSet.getString(3));
				userVO.setUserPassword(resultSet.getString(4));
				userVO.setUserPhone(resultSet.getString(5));
				userVO.setUserNickname(resultSet.getString(6));
				userVO.setUserEmail(resultSet.getString(7));
				userVO.setUserAddress(resultSet.getString(8));
				userVO.setUserBirth(resultSet.getString(9));
				userVO.setUserGender(resultSet.getString(10));
				userVO.setUserRecommenderId(resultSet.getString(11));
			}

		} catch (SQLException e) {
			System.out.println("recommendMe(String) SQL�� ����");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

		return userVO;
	}

}