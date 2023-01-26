package view;

import dao.MyUserDAO;
import domain.UserVO;

public class MyTest {
	public static void main(String[] args) {
		MyUserDAO userDAO = new MyUserDAO();
		UserVO userVO = new UserVO();
		//	아이디 중복검사 o
//		if(userDAO.checkId("hds1234")) {
//			System.out.println("사용 가능한 아이디");
//			return;
//		}
//		System.out.println("중복된 아이디");

//		System.out.println(userDAO.select(1L));
		//	로그인 o
//		System.out.println(userDAO.login("kdh1234", "1234"));

		//	회원가입 o
		userVO.setUserIdentification("kdh5678");
		userVO.setUserName("김동한2");
		userVO.setUserPassword("1111");
		userVO.setUserPhone("01022222222");
		userVO.setUserNickname("한");
		userVO.setUserEmail("dsa@gamil.com");
		userVO.setUserAddress("서울 송파구");
		userVO.setUserBirth("1998-05-12");
		userVO.setUserGender("W");
		userVO.setUserRecommenderId("kdh1234");
		userDAO.signUp(userVO);

		//	암호화	o	
//		System.out.println(userDAO.encryptPassword("1234"));

		//	비밀번호 변경 o
//		userDAO.changePassword("kdh1234", "1234");


		//	회원정보 수정 o
//      userVO.setUserName("윤민우");
//      userVO.setUserPassword("5678");
//      userVO.setUserPhone("01098765431");
//      userVO.setUserNickname("민");
//      userVO.setUserEmail("ymw5678@gmail.com");
//      userVO.setUserAddress("서울시 송파구");
//      userVO.setUserId(2L);
//      userDAO.update(userVO);

		//	회원정보 조회 O
//		System.out.println(userDAO.select(2L));
		
		//   회원탈퇴 O
//		userDAO.delete(3L);

		//	아이디 찾기 O
//		System.out.println(userDAO.findId("01012341234"));

		// 나를 추천한 사람 o
//		System.out.println(userDAO.recommendedMe("hds1234"));

		// 내가 추천한 사람 o
//		System.out.println(userDAO.recommendMe("hds1234"));

		// 추천수 o
//		System.out.println(userDAO.countRecommend("hds1234"));



	}
}
