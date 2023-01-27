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

	//   대댓글 추가 
	//   이 답글에 대한 정보(ReplyVO)와 어디에 대댓글인지에 대한 target을 매개변수로 받아온다.
	public void insert(ReplyVO replyVO, Long target) {
		//	   TBL_REPLY 테이블에 대댓글 관련 정보들을 넣어준다.
		String query = "INSERT INTO TBL_REPLY"
				+ "(REPLY_ID, REPLY_CONTENT, USER_ID, BOARD_ID, REPLY_GROUP, REPLY_DEPTH) "
				+ "VALUES(SEQ_REPLY.NEXTVAL, ?, ?, ?, ?, (SELECT REPLY_DEPTH + 1 FROM TBL_REPLY WHERE REPLY_ID = ?))";

		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			//	답글의 정보에서 그 사람이 쓴 댓글을 받아와서 REPLY_CONTENT에 넣어준다.
			preparedStatement.setString(1, replyVO.getReplyContent());
			//	그 사람의 ID를 UserDAO.userId를 통해서 받아온다.
			preparedStatement.setLong(2, UserDAO.userId);
			//	답글에 대한 정보인 replyVO에서 BoardId를 받아와서 쿼리문의 BOARD_ID에 넣어준다.
			preparedStatement.setLong(3, replyVO.getBoardId());

			//	처음쓴 사람의 REPLY_GROUP과 REPLY_ID는 같기 때문에 똑같이 target으로 넣어준다.
			preparedStatement.setLong(4, target);
			preparedStatement.setLong(5, target);
			//	INSERT이므로 executeUpdate를 해준다.
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

	//   댓글 추가
	//   댓글에 대한 정보를 담고있는 ReplyVO를 받아온다.
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

	//   댓글 전체 조회
	public ArrayList<ReplyDTO> selectAll(){
		String query = "SELECT NVL(REPLY_COUNT, 0) REPLY_COUNT, REPLY_ID, REPLY_CONTENT, R2.USER_ID, BOARD_ID, REPLY_REGISTER_DATE, REPLY_UPDATE_DATE, " 
				+ "R2.REPLY_GROUP, REPLY_DEPTH, " 
				+ "USER_IDENTIFICATION, USER_NAME, USER_PASSWORD, " 
				+ "USER_PHONE, USER_NICKNAME, USER_EMAIL, USER_ADDRESS, USER_BIRTH, " 
				+ "USER_GENDER, USER_RECOMMENDER_ID " 
				//	COUNT(REPLY_ID) - 1에서 -1을 해주는 이유는 내 자신의 댓글까지 나오기 때문에 대댓글을 볼 용도라 내 댓글은 빼고 select 해준다.
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

	// 댓글 삭제
	public void delete(Long target) {
		//	대댓글이 달린 댓글을 삭제할 때 모든 댓글을 삭제해준다.
		String deleteAll = "DELETE FROM TBL_REPLY WHERE REPLY_GROUP = ? ";
		//	대댓글이 달려 있는지를 확인한다.
		String getDepth = "SELECT REPLY_DEPTH FROM TBL_REPLY WHERE REPLY_ID = ? ";
		//	대댓글을 삭제한다.	
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




	//  댓글 수정
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

