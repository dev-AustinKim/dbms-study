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

	//   팔로우 중복체크
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
			System.out.println("checkFollow(Long) SQL문 오류");
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

	//   누가 팔로우를 하면 추가해주는 메소드 - TBL_FOLLOW 
	//   매개변수로 누구를 팔로잉 할 건지를 받아온다.
	public void insert(Long followingUserId) {
		String query = "INSERT INTO TBL_FOLLOW (FOLLOW_ID, FOLLOWING_USER_ID, FOLLOWER_USER_ID) "
				+ "VALUES(SEQ_FOLLOW.NEXTVAL, ?, ?)";

		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, followingUserId);
			//         로그인한 상태에서 팔로우 하기 때문에 팔로워 아이디에 로그인 되어있는 아이디를 넣어준다.
			preparedStatement.setLong(2, UserDAO.userId); 

			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("insert(Long) SQL문 오류");
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

	//	팔로우 한번 더 눌렀을 때 언팔해주는 기능(언팔은 로그인한 상태에서만 가능)
	//	매개변수로 받는 userId는 언팔할 userId이다.
	public void delete(long userId) {
		//	로그인한 나의 아이디로 내 팔로잉 카운트를 1 감소.			
		String unFollowing ="UPDATE TBL_USER SET FOLLOWING_COUNT = (FOLLOWING_COUNT - 1) WHERE USER_ID = ?";
		//	매개변수로 받아온 userId의 팔로워 카운트를 1 감소.	
		String unFollower = "UPDATE TBL_USER SET FOLLOWER_COUNT = (FOLLOWER_COUNT-1) WHERE USER_ID = ?";

		/*언팔했을 때 TBL_FOLLOW에서도 삭제해준다. 로그인한 상태니까 FOLLOWER_USER_ID는 UserDAO.userId로 받고 
		FOLLOWING_USER_ID는 매개변수인 UserId를 사용한다.*/
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
			System.out.println("delete(long) SQL문법 오류");
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

	// 나로 로그인했을 때 팔로잉 팔로워 수 증가, 매개변수로 팔로잉 하려는 사람의 userId를 가져온다.
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
			System.out.println("followCount(long) SQL문법 오류");
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

	//	 팔로워 아이디랑 팔로잉 아이디 두 개를 받아서 각각 팔로잉 수 증가, 팔로워 수 증가.
	public void followCount(long followerUserId/*A의 입장*/, long followingUserId /*A가 팔로잉 하는 사람*/) {
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
			System.out.println("followCount(long, long) SQL문법 오류");
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


	//   내가 팔로우한 사람들 (팔로잉)
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
			//   내가 팔로우한 사람들이니까 로그인한 상태에서 찾는다고 가정할 때 로그인 되어있는 userId를 가져온다.
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
			System.out.println("following() SQL문 오류");
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


	//   나를 팔로우한 사람들 (팔로워)
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
			//   내가 팔로우한 사람들이니까 로그인한 상태에서 찾는다고 가정할 때 로그인 되어있는 userId를 가져온다.
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
			System.out.println("follower() SQL문 오류");
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