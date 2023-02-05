package domain;

public class FollowVO {
   private Long followId;
   private Long userId;
   private Long BoardId;
   
   
   
   public FollowVO() {;}
   
   public Long getFollowId() {
      return followId;
   }
   public void setFollowId(Long followId) {
      this.followId = followId;
   }
   public Long getUserId() {
      return userId;
   }
   public void setUserId(Long userId) {
      this.userId = userId;
   }
   public Long getBoardId() {
      return BoardId;
   }
   public void setBoardId(Long boardId) {
      BoardId = boardId;
   }

   @Override
   public String toString() {
      return "FollowVO [followId=" + followId + ", userId=" + userId + ", BoardId=" + BoardId + "]";
   }
   
}