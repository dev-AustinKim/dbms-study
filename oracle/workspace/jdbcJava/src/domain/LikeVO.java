package domain;

public class LikeVO {
   private Long likeId;
   private Long UserId;
   private Long BoardId;
   
   public LikeVO() {;}

   public Long getLikeId() {
      return likeId;
   }

   public void setLikeId(Long likeId) {
      this.likeId = likeId;
   }

   public Long getUserId() {
      return UserId;
   }

   public void setUserId(Long userId) {
      UserId = userId;
   }

   public Long getBoardId() {
      return BoardId;
   }

   public void setBoardId(Long boardId) {
      BoardId = boardId;
   }

   @Override
   public String toString() {
      return "LikeVO [likeId=" + likeId + ", UserId=" + UserId + ", BoardId=" + BoardId + "]";
   }
   
   
}