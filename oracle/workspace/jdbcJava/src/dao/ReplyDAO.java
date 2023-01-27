package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import domain.ReplyDTO;
import domain.ReplyVO;

public class ReplyDAO {
	public Connection connection;
	public PreparedStatement preparedStatement;
	public ResultSet resultSet;

	//   ���� �߰� 
	//   �� ��ۿ� ���� ����(ReplyVO)�� ��� ���������� ���� target�� �Ű������� �޾ƿ´�.
	public void insert(ReplyVO replyVO, Long target) {
		//	   TBL_REPLY ���̺��� ���� ���� �������� �־��ش�.
		String query = "INSERT INTO TBL_REPLY"
				+ "(REPLY_ID, REPLY_CONTENT, USER_ID, BOARD_ID, REPLY_GROUP, REPLY_DEPTH) "
				+ "VALUES(SEQ_REPLY.NEXTVAL, ?, ?, ?, ?, (SELECT REPLY_DEPTH + 1 FROM TBL_REPLY WHERE REPLY_ID = ?))";

		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			//	����� �������� �� ����� �� ����� �޾ƿͼ� REPLY_CONTENT�� �־��ش�.
			preparedStatement.setString(1, replyVO.getReplyContent());
			//	�� ����� ID�� UserDAO.userId�� ���ؼ� �޾ƿ´�.
			preparedStatement.setLong(2, UserDAO.userId);
			//	��ۿ� ���� ������ replyVO���� BoardId�� �޾ƿͼ� �������� BOARD_ID�� �־��ش�.
			preparedStatement.setLong(3, replyVO.getBoardId());

			//	ó���� ����� REPLY_GROUP�� REPLY_ID�� ���� ������ �Ȱ��� target���� �־��ش�.
			preparedStatement.setLong(4, target);
			preparedStatement.setLong(5, target);
			//	INSERT�̹Ƿ� executeUpdate�� ���ش�.
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
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

	//   ��� �߰�
	//   ��ۿ� ���� ������ ����ִ� ReplyVO�� �޾ƿ´�.
	public void insert(ReplyVO replyVO) {
		String query = "INSERT INTO TBL_REPLY"
				+ "(REPLY_ID, REPLY_CONTENT, USER_ID, BOARD_ID, REPLY_GROUP, REPLY_DEPTH) "
				+ "VALUES(SEQ_REPLY.NEXTVAL, ?, ?, ?, SEQ_REPLY.CURRVAL, 0)";

		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, replyVO.getReplyContent());
			preparedStatement.setLong(2, UserDAO.userId);
			preparedStatement.setLong(3, replyVO.getBoardId());

			preparedStatement.executeUpdate();

		} catch (SQLException e) {
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

	//   ��� ��ü ��ȸ
	public ArrayList<ReplyDTO> selectAll(){
		String query = "SELECT NVL(REPLY_COUNT, 0) REPLY_COUNT, REPLY_ID, REPLY_CONTENT, R2.USER_ID, BOARD_ID, REPLY_REGISTER_DATE, REPLY_UPDATE_DATE, " 
				+ "R2.REPLY_GROUP, REPLY_DEPTH, " 
				+ "USER_IDENTIFICATION, USER_NAME, USER_PASSWORD, " 
				+ "USER_PHONE, USER_NICKNAME, USER_EMAIL, USER_ADDRESS, USER_BIRTH, " 
				+ "USER_GENDER, USER_RECOMMENDER_ID " 
				//	COUNT(REPLY_ID) - 1���� -1�� ���ִ� ������ �� �ڽ��� ��۱��� ������ ������ ������ �� �뵵�� �� ����� ���� select ���ش�.
				+ "FROM (SELECT REPLY_GROUP, COUNT(REPLY_ID) - 1 REPLY_COUNT FROM TBL_REPLY GROUP BY REPLY_GROUP) R1 RIGHT OUTER JOIN VIEW_REPLY_USER R2 "
				+ "ON R1.REPLY_GROUP = R2.REPLY_GROUP AND R1.REPLY_GROUP = R2.REPLY_ID";

		ReplyDTO replyDTO = null;
		int index = 0;
		ArrayList<ReplyDTO> replies = new ArrayList<ReplyDTO>();

		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {
				index = 0;
				replyDTO = new ReplyDTO();
				replyDTO.setReplyCount(resultSet.getLong(++index));
				replyDTO.setReplyId(resultSet.getLong(++index));
				replyDTO.setReplyContent(resultSet.getString(++index));
				replyDTO.setUserId(resultSet.getLong(++index));
				replyDTO.setBoardId(resultSet.getLong(++index));
				replyDTO.setReplyRegisterDate(resultSet.getString(++index));
				replyDTO.setReplyUpdateDate(resultSet.getString(++index));
				replyDTO.setReplyGroup(resultSet.getLong(++index));
				replyDTO.setReplyDepth(resultSet.getLong(++index));
				replyDTO.setUserIdentification(resultSet.getString(++index));
				replyDTO.setUserName(resultSet.getString(++index));
				replyDTO.setUserPassword(resultSet.getString(++index));
				replyDTO.setUserPhone(resultSet.getString(++index));
				replyDTO.setUserNickname(resultSet.getString(++index));
				replyDTO.setUserEmail(resultSet.getString(++index));
				replyDTO.setUserAddress(resultSet.getString(++index));
				replyDTO.setUserBirth(resultSet.getString(++index));
				replyDTO.setUserGender(resultSet.getString(++index));
				replyDTO.setUserRecommenderId(resultSet.getString(++index));
				replies.add(replyDTO);
			}

		} catch (SQLException e) {
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
		return replies;
	}

	// ��� ����
	public void delete(Long target) {
		//	������ �޸� ����� ������ �� ��� ����� �������ش�.
		String deleteAll = "DELETE FROM TBL_REPLY WHERE REPLY_GROUP = ? ";
		//	������ �޷� �ִ����� Ȯ���Ѵ�.
		String getDepth = "SELECT REPLY_DEPTH FROM TBL_REPLY WHERE REPLY_ID = ? ";
		//	������ �����Ѵ�.	
		String deleteOne = "DELETE FROM TBL_REPLY WHERE REPLY_ID = ?";
		Long depth = 0L;
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(getDepth);
			preparedStatement.setLong(1, target);
			resultSet = preparedStatement.executeQuery();

			if(resultSet.next()) {
				if(resultSet.getLong(1)==0) {
					preparedStatement = connection.prepareStatement(deleteAll);
					preparedStatement.setLong(1, target);
					preparedStatement.setLong(2, target);
					preparedStatement.executeUpdate();

				}else {
					preparedStatement = connection.prepareStatement(deleteOne);
					preparedStatement.setLong(1, target);
					preparedStatement.executeUpdate();
				}
			}

		} catch (SQLException e) {
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
	}




	//  ��� ����
	public void update(ReplyVO replyVO) {
		String query = "UPDATE TBL_REPLY "
				+ "SET REPLY_CONTENT=?, REPLY_UPDATE_DATE=SYSDATE "
				+ "WHERE REPLY_ID=?";

		connection = DBConnecter.getConnection();


		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, replyVO.getReplyContent());
			preparedStatement.setLong(2, replyVO.getReplyId());
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
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
}
