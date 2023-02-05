package domain;

public class UserVO {

	private Long userId; // ȸ����ȣ
	private String userIdentification; // ���̵�
	private String userName; // �̸�
	private String userPassword; // ��й�ȣ
	private String userPhone; // �ڵ�����ȣ
	private String userNickname; // ����
	private String userEmail; // �̸���
	private String userAddress; // �ּ�
	private String userBirth; // ����
	private String userGender; // ����
	private String userRecommenderId; // ��õ�� ��
	//   private Long recommendCount; // ��õ ��
	private Long followingCount; // �ȷ��� ��
	private Long followerCount; // �ȷ��� ��

	public UserVO() {;}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserIdentification() {
		return userIdentification;
	}

	public void setUserIdentification(String userIdentification) {
		this.userIdentification = userIdentification;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getUserBirth() {
		return userBirth;
	}

	public void setUserBirth(String userBirth) {
		this.userBirth = userBirth;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public String getUserRecommenderId() {
		return userRecommenderId;
	}

	public void setUserRecommenderId(String userRecommenderId) {
		this.userRecommenderId = userRecommenderId;
	}
	//   public Long getRecommendCount() {
	//      return recommendCount;
	//   }
	//
	//   public void setRecommendCount(Long recommendCount) {
	//      this.recommendCount = recommendCount;
	//   }

	public Long getFollowingCount() {
		return followingCount;
	}

	public void setFollowingCount(Long followingCount) {
		this.followingCount = followingCount;
	}

	public Long getFollowerCount() {
		return followerCount;
	}

	public void setFollowerCount(Long followerCount) {
		this.followerCount = followerCount;
	}

	@Override
	public String toString() {
		return "UserVO [userId=" + userId + ", userIdentification=" + userIdentification + ", userName=" + userName
				+ ", userPassword=" + userPassword + ", userPhone=" + userPhone + ", userNickname=" + userNickname
				+ ", userEmail=" + userEmail + ", userAddress=" + userAddress + ", userBirth=" + userBirth
				+ ", userGender=" + userGender + ", userRecommenderId=" + userRecommenderId + ", followingCount="
				+ followingCount + ", followerCount=" + followerCount + "]";
	}

	


	//   @Override
	//   public String toString() {
	//      return "UserVO [userId=" + userId + ", userIdentification=" + userIdentification + ", userName=" + userName
	//            + ", userPassword=" + userPassword + ", userPhone=" + userPhone + ", userNickname=" + userNickname
	//            + ", userEmail=" + userEmail + ", userAddress=" + userAddress + ", userBirth=" + userBirth
	//            + ", userGender=" + userGender + ", userRecommenderId=" + userRecommenderId + "]";
	//   }

	//   @Override
	//   public String toString() {
	//      return "UserVO [userId=" + userId + ", userIdentification=" + userIdentification + ", userName=" + userName
	//            + ", userPassword=" + userPassword + ", userPhone=" + userPhone + ", userNickname=" + userNickname
	//            + ", userEmail=" + userEmail + ", userAddress=" + userAddress + ", userBirth=" + userBirth
	//            + ", userGender=" + userGender + ", userRecommenderId=" + userRecommenderId + ", recommendCount="
	//            + recommendCount + "]";
	//   }




}