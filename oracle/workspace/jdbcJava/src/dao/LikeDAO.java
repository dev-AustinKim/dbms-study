package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import domain.BoardVO;
import domain.UserVO;

public class LikeDAO {
   public Connection connection;
   public PreparedStatement preparedStatement;
   public ResultSet resultSet;
   
   
   
// 좋아요 중복체크
   public boolean checkLike(Long boardId) {
      String query = "SELECT USER_ID FROM TBL_LIKE WHERE BOARD_ID = ?";
      boolean check = false;
      int count = 0;
      connection = DBConnecter.getConnection();
      
      try {
         preparedStatement = connection.prepareStatement(query);
         preparedStatement.setLong(1, boardId);
         
         resultSet = preparedStatement.executeQuery();
         
         while (resultSet.next()) {
            if (resultSet.getLong(1) == UserDAO.userId) {count++;}
         }
         
         if (count < 1) {check = true;}
         
      } catch (SQLException e) {
         System.out.println("checkLike(Long) SQL문 오류");
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
   
   
//   누가 게시글에 좋아요(추천)를 하면 추가해주는 메소드 - TBL_LIKE
   public void insert(Long boardId) {
      String query = "INSERT INTO TBL_LIKE (LIKE_ID, USER_ID, BOARD_ID) VALUES(SEQ_LIKE.NEXTVAL, ?, ?) ";
      boolean check = false;
//      int result = 0;
      
      connection = DBConnecter.getConnection();
      try {
         preparedStatement = connection.prepareStatement(query);
         preparedStatement.setLong(1, UserDAO.userId);
         preparedStatement.setLong(2, boardId);
         
         /* result = */preparedStatement.executeUpdate();
         
//         if (result > 0) {
//            check = true;
//         }
            
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
//      return check;
   }
   
   
//   게시글에 좋아요한 사람들
   public ArrayList<UserVO> likeWho(Long boardId) {
      String query = "SELECT U.USER_ID, USER_IDENTIFICATION, USER_NAME, "
            + "USER_PASSWORD, USER_PHONE, USER_NICKNAME, USER_EMAIL, "
            + "USER_ADDRESS, USER_BIRTH, USER_GENDER, USER_RECOMMENDER_ID "
            + "FROM TBL_LIKE L JOIN TBL_USER U "
            + "ON L.USER_ID = U.USER_ID AND BOARD_ID = ?";
      ArrayList<UserVO> likes = new ArrayList<>();
      UserVO userVO = null;
      
      connection = DBConnecter.getConnection();
      
      try {
         preparedStatement = connection.prepareStatement(query);
         preparedStatement.setLong(1, boardId);
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
            
            likes.add(userVO);
         }
         
         
         
      } catch (SQLException e) {
         System.out.println("likeWho(Long) SQL문 오류");
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
      
      
      return likes;
   }
   
//   내가 좋아요한 게시글들
   public ArrayList<BoardVO> likeBoard() {
      String query = "SELECT B.BOARD_ID, BOARD_TITLE, BOARD_CONTENT, BOARD_REGISTER_DATE, BOARD_UPDATE_DATE, B.USER_ID, B.BOARD_LIKE "
            + "FROM TBL_LIKE L JOIN TBL_BOARD B "
            + "ON L.BOARD_ID = B.BOARD_ID "
            + "AND L.USER_ID = ?";
      ArrayList<BoardVO> myLikes = new ArrayList<>();
      BoardVO boardVO = null;
      int index = 0;
      
      connection = DBConnecter.getConnection();
      
      try {
         preparedStatement = connection.prepareStatement(query);
         preparedStatement.setLong(1, UserDAO.userId);
         resultSet = preparedStatement.executeQuery();
         
         while (resultSet.next()) {
            index = 0;
            boardVO = new BoardVO();
            
            boardVO.setBoardId(resultSet.getLong(++index));
            boardVO.setBoardTitle(resultSet.getString(++index));
            boardVO.setBoardContent(resultSet.getString(++index));
            boardVO.setBoardRegisterDate(resultSet.getString(++index));
            boardVO.setBoardUpdateDate(resultSet.getString(++index));
            boardVO.setUserId(resultSet.getLong(++index));
            boardVO.setBoardLike(resultSet.getLong(++index));
            
            
            myLikes.add(boardVO);
         }
         
         
         
      } catch (SQLException e) {
         System.out.println("likeBoard() SQL문 오류");
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
      
      
      return myLikes;
   }
}