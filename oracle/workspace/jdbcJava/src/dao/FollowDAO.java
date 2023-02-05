package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import domain.UserVO;

public class FollowDAO {
	public Connection connection;
	public PreparedStatement preparedStatement;
	public ResultSet resultSet;

	//   �ȷο� �ߺ�üũ
	public boolean checkFollow(Long followId) {
		String query = "SELECT FOLLOWING_USER_ID FROM TBL_FOLLOW WHERE FOLLOWER_USER_ID = ?";
		boolean check = false;
		int count = 0;
		connection = DBConnecter.getConnection();

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, UserDAO.userId);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				if (resultSet.getLong(1) == followId) {count++;}
			}

			if (count < 1) {check = true;}

		} catch (SQLException e) {
			System.out.println("checkFollow(Long) SQL�� ����");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
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
		return check;
	}

	//   ���� �ȷο츦 �ϸ� �߰����ִ� �޼ҵ� - TBL_FOLLOW 
	//   �Ű������� ������ �ȷ��� �� ������ �޾ƿ´�.
	public void insert(Long followingUserId) {
		String query = "INSERT INTO TBL_FOLLOW (FOLLOW_ID, FOLLOWING_USER_ID, FOLLOWER_USER_ID) "
				+ "VALUES(SEQ_FOLLOW.NEXTVAL, ?, ?)";

		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, followingUserId);
			//         �α����� ���¿��� �ȷο� �ϱ� ������ �ȷο� ���̵� �α��� �Ǿ��ִ� ���̵� �־��ش�.
			preparedStatement.setLong(2, UserDAO.userId); 

			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("insert(Long) SQL�� ����");
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
				throw new RuntimeException(e);
			}
		}
	}

	//	�ȷο� �ѹ� �� ������ �� �������ִ� ���(������ �α����� ���¿����� ����)
	//	�Ű������� �޴� userId�� ������ userId�̴�.
	public void delete(long userId) {
		//	�α����� ���� ���̵�� �� �ȷ��� ī��Ʈ�� 1 ����.			
		String unFollowing ="UPDATE TBL_USER SET FOLLOWING_COUNT = (FOLLOWING_COUNT - 1) WHERE USER_ID = ?";
		//	�Ű������� �޾ƿ� userId�� �ȷο� ī��Ʈ�� 1 ����.	
		String unFollower = "UPDATE TBL_USER SET FOLLOWER_COUNT = (FOLLOWER_COUNT-1) WHERE USER_ID = ?";

		/*�������� �� TBL_FOLLOW������ �������ش�. �α����� ���´ϱ� FOLLOWER_USER_ID�� UserDAO.userId�� �ް� 
		FOLLOWING_USER_ID�� �Ű������� UserId�� ����Ѵ�.*/
		String deleteFollowTable = "DELETE FROM TBL_FOLLOW WHERE FOLLOWING_USER_ID = ? AND FOLLOWER_USER_ID = ?";
		
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(unFollowing);
			preparedStatement.setLong(1, UserDAO.userId);
			preparedStatement.executeUpdate();

			preparedStatement = connection.prepareStatement(unFollower);
			preparedStatement.setLong(1, userId);
			preparedStatement.executeUpdate();
			
			preparedStatement = connection.prepareStatement(deleteFollowTable);
			preparedStatement.setLong(1, userId);
			preparedStatement.setLong(2, UserDAO.userId);			
			preparedStatement.executeUpdate();
			
		}  catch (Exception e) {
			System.out.println("delete(long) SQL���� ����");
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
				throw new RuntimeException(e);
			}
		}
	}

	// ���� �α������� �� �ȷ��� �ȷο� �� ����, �Ű������� �ȷ��� �Ϸ��� ����� userId�� �����´�.
	public void followCount(long userId) {
		String followingquery = "UPDATE TBL_USER SET FOLLOWING_COUNT = FOLLOWING_COUNT + 1"
				+ " WHERE USER_ID = ?";
		String followerquery = "UPDATE TBL_USER SET FOLLOWER_COUNT = FOLLOWER_COUNT + 1"
				+ " WHERE USER_ID = ?";
		UserVO userVO = new UserVO();
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(followingquery);
			preparedStatement.setLong(1, UserDAO.userId);
			preparedStatement.executeUpdate();

			preparedStatement = connection.prepareStatement(followerquery);
			preparedStatement.setLong(1, userId);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			System.out.println("followCount(long) SQL���� ����");
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
				throw new RuntimeException(e);
			}
		}
	}

	//	 �ȷο� ���̵�� �ȷ��� ���̵� �� ���� �޾Ƽ� ���� �ȷ��� �� ����, �ȷο� �� ����.
	public void followCount(long followerUserId/*A�� ����*/, long followingUserId /*A�� �ȷ��� �ϴ� ���*/) {
		String followingquery = "UPDATE TBL_USER SET FOLLOWING_COUNT = FOLLOWING_COUNT + 1"
				+ " WHERE USER_ID = ?";
		String followerquery = "UPDATE TBL_USER SET FOLLOWER_COUNT = FOLLOWER_COUNT + 1"
				+ " WHERE USER_ID = ?";
		UserVO userVO = new UserVO();
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(followingquery);
			preparedStatement.setLong(1, followerUserId);
			preparedStatement.executeUpdate();

			preparedStatement = connection.prepareStatement(followerquery);
			preparedStatement.setLong(1, followingUserId);
			preparedStatement.executeUpdate();
		}  catch (Exception e) {
			System.out.println("followCount(long, long) SQL���� ����");
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
				throw new RuntimeException(e);
			}
		}
	}


	//   ���� �ȷο��� ����� (�ȷ���)
	public ArrayList<UserVO> following() {
		String query = "SELECT USER_ID, USER_IDENTIFICATION, USER_NAME, USER_PASSWORD, USER_PHONE, "
				+ "USER_NICKNAME, USER_EMAIL, USER_ADDRESS, USER_BIRTH, USER_GENDER, USER_RECOMMENDER_ID, "
				+ "U.FOLLOWING_COUNT, U.FOLLOWER_COUNT FROM TBL_USER U JOIN TBL_FOLLOW F "
				+ "ON U.USER_ID = F.FOLLOWING_USER_ID AND F.FOLLOWER_USER_ID = ?";
		UserVO userVO = null;
		ArrayList<UserVO> following = new ArrayList<>();
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			//   ���� �ȷο��� ������̴ϱ� �α����� ���¿��� ã�´ٰ� ������ �� �α��� �Ǿ��ִ� userId�� �����´�.
			preparedStatement.setLong(1, UserDAO.userId);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
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
				userVO.setFollowingCount(resultSet.getLong(12));
				userVO.setFollowerCount(resultSet.getLong(13));
				following.add(userVO);
			}         

		} catch (SQLException e) {
			System.out.println("following() SQL�� ����");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
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
		return following;
	}


	//   ���� �ȷο��� ����� (�ȷο�)
	public ArrayList<UserVO> follower() {
		String query = "SELECT U.USER_ID, USER_IDENTIFICATION, USER_NAME, USER_PASSWORD, USER_PHONE, USER_NICKNAME,"
				+ " USER_EMAIL, USER_ADDRESS, USER_BIRTH, USER_GENDER, USER_RECOMMENDER_ID, U.FOLLOWING_COUNT, U.FOLLOWER_COUNT "
				+ "FROM TBL_USER U JOIN TBL_FOLLOW F "
				+ "ON U.USER_ID = F.FOLLOWER_USER_ID AND F.FOLLOWING_USER_ID = ?";
		UserVO userVO = null;
		ArrayList<UserVO> follower = new ArrayList<>();
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			//   ���� �ȷο��� ������̴ϱ� �α����� ���¿��� ã�´ٰ� ������ �� �α��� �Ǿ��ִ� userId�� �����´�.
			preparedStatement.setLong(1, UserDAO.userId);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
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
				userVO.setFollowingCount(resultSet.getLong(12));
				userVO.setFollowerCount(resultSet.getLong(13));
				follower.add(userVO);
			}         
		} catch (SQLException e) {
			System.out.println("follower() SQL�� ����");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
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
		return follower;
	}
}